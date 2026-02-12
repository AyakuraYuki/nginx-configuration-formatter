package cc.ayakurayuki.formatter.psi

import cc.ayakurayuki.formatter.classic.HtmlTagRegex
import cc.ayakurayuki.formatter.psi.NginxBlock.Companion.BLOCK_TYPES
import cc.ayakurayuki.formatter.psi.NginxBlock.Companion.NO_ALIGN_DIRECTIVES
import cc.ayakurayuki.lang.NginxLanguage
import cc.ayakurayuki.psi.Types
import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.formatter.common.AbstractBlock

/**
 * @author Ayakura Yuki
 * @date 2026/01/30-21:05
 */
class NginxBlock(
    node: ASTNode,
    wrap: Wrap?,
    alignment: Alignment?,
    private val spacingBuilder: SpacingBuilder,
    private val settings: CodeStyleSettings,
    private val indent: Indent = Indent.getNoneIndent(),
    private val valueAlignColumn: Int = 0,
) : AbstractBlock(node, wrap, alignment) {

    companion object {
        private val SPECIAL_ALIGN_DIRECTIVES = setOf("log_format", "map", "geo")
        private val NO_ALIGN_DIRECTIVES = setOf("upstream", "location")

        private val DIRECTIVE_TYPES = setOf(
            Types.REGULAR_DIRECTIVE_STMT,
            Types.MAP_DIRECTIVE_STMT,
            Types.GEO_DIRECTIVE_STMT,
        )

        private val BLOCK_TYPES = setOf(
            Types.BLOCK_STMT,
            Types.MAP_BLOCK_STMT,
            Types.GEO_BLOCK_STMT,
            Types.LUA_BLOCK_STMT,
        )
    }

    private val customSettings = settings.getCustomSettings(NginxCodeStyleSettings::class.java)

    override fun getIndent(): Indent = indent

    // region Build children

    override fun buildChildren(): List<Block?> {
        val blocks = mutableListOf<Block>()
        val children = collectChildren()
        val childAlignColumn = resolveChildAlignColumn(children)

        for (childNode in children) {
            blocks.add(
                NginxBlock(
                    childNode,
                    null,
                    null,
                    spacingBuilder,
                    settings,
                    calculateChildIndent(childNode),
                    childAlignColumn
                )
            )
        }

        return blocks
    }

    /**
     * Collect all non-blank child nodes.
     */
    private fun collectChildren(): List<ASTNode> {
        val children = mutableListOf<ASTNode>()
        var child = node.firstChildNode
        while (child != null) {
            if (isNonBlankNode(child)) {
                children.add(child)
            }
            child = child.treeNext
        }
        return children
    }

    // endregion

    // region Value alignment

    /**
     * Determine the alignment column for all child blocks.
     *
     * Mirrors [cc.ayakurayuki.formatter.classic.performAlignment]:
     * scan qualifying directives, find max name length, compute alignment column.
     * The column is passed uniformly to all children — the actual decision of
     * whether to apply alignment happens in [getDirectiveChildSpacing]
     * (matching the classic formatter's `trimmed.endsWith(";")` filter).
     *
     * - **[NginxCodeStyleSettings.ALIGN_ACROSS_BLOCKS]**: file root computes
     *   a global **absolute** column (`max(indent + nameLen) + 1`), mirroring
     *   [cc.ayakurayuki.formatter.classic.performAlignment]'s
     *   `attributeAlignColumn = max(indentLength + splitLine[0].length + 1)`.
     *   Each block scope then converts the absolute column to a relative one
     *   by subtracting its own indent depth.
     * - **[NginxCodeStyleSettings.ALIGN_WITHIN_BLOCK]**: each block scope
     *   calculates independently; upstream blocks are excluded.
     */
    private fun resolveChildAlignColumn(children: List<ASTNode>): Int {
        val mode = customSettings.alignPropertyValuesMode
        if (mode == NginxCodeStyleSettings.ALIGN_OFF) return 0

        // Across Blocks: file root computes absolute column, each block scope
        // subtracts one indent level to convert to a relative column for its
        // children.
        if (mode == NginxCodeStyleSettings.ALIGN_ACROSS_BLOCKS) {
            if (isFileRoot()) return calculateGlobalAlignColumn()
            if (myNode.elementType in BLOCK_TYPES) {
                return maxOf(0, valueAlignColumn - indentSize())
            }
            return valueAlignColumn
        }

        // Within-block: block scopes calculate locally, non-scopes inherit
        if (!isBlockScope()) return valueAlignColumn
        if (isUpstreamBlock()) return 0
        return calculateLocalAlignColumn(children)
    }

    /**
     * Scan the entire PSI tree to find the max absolute alignment column.
     *
     * Mirrors [cc.ayakurayuki.formatter.classic.performAlignment]:
     * `attributeAlignColumn = max(indentLength + splitLine[0].length + 1)`
     *
     * In PSI, indent depth = [blockDepth] * [indentSize], so the absolute
     * column = `depth * indentSize + nameLen + 1`.
     */
    private fun calculateGlobalAlignColumn(): Int {
        val indent = indentSize()
        var maxColumn = 0
        scanDirectives(myNode) { node ->
            val depth = blockDepth(node)
            val column = depth * indent + directiveName(node).length + 1
            if (column > maxColumn) maxColumn = column
        }
        return maxColumn
    }

    /**
     * Calculate the alignment column for directives within this block only.
     */
    private fun calculateLocalAlignColumn(children: List<ASTNode>): Int {
        var maxNameLength = 0
        for (child in children) {
            if (isAlignCandidate(child)) {
                val len = directiveName(child).length
                if (len > maxNameLength) maxNameLength = len
            }
        }
        return if (maxNameLength > 0) maxNameLength + 2 else 0
    }

    /**
     * Recursively walk the PSI tree, invoking [action] for every qualifying
     * directive.
     */
    private fun scanDirectives(node: ASTNode, action: (ASTNode) -> Unit) {
        if (isAlignCandidate(node)) {
            action(node)
            return
        }
        var child = node.firstChildNode
        while (child != null) {
            if (isNonBlankNode(child)) {
                scanDirectives(child, action)
            }
            child = child.treeNext
        }
    }

    // endregion

    // region Alignment helpers

    /**
     * Check if a node qualifies for value alignment.
     *
     * Combines PSI element type guard with text-based conditions that directly
     * mirror [cc.ayakurayuki.formatter.classic.performAlignment] line 342-349:
     *
     * | classic (text)                        | PSI                                       |
     * |---------------------------------------|-------------------------------------------|
     * | `trimmed.endsWith(";")`               | [text.endsWith(";")][String.endsWith]     |
     * | `!startsWith("upstream")`             | [NO_ALIGN_DIRECTIVES] startsWith check    |
     * | `!contains("location")`               | [NO_ALIGN_DIRECTIVES] startsWith check    |
     * | `!HtmlTagRegex.containsMatchIn(line)` | [HtmlTagRegex] check                      |
     * | `!LeftBraceRegex / !RightBraceRegex`  | implied by endsWith(";")                  |
     *
     * The grammar guarantees that all non-REGULAR directives (map, geo, if, location, lua)
     * always end with `}`, so [text.endsWith(";")][String.endsWith] alone replaces:
     * `unwrapDirective()` + `findChildByType(SEMICOLON)` + `findChildByType(BLOCK_STMT)`.
     */
    private fun isAlignCandidate(node: ASTNode): Boolean {
        val type = node.elementType
        if (type != Types.DIRECTIVE_STMT && type != Types.REGULAR_DIRECTIVE_STMT) return false

        val text = node.text.trim()
        return text.endsWith(";") &&
                !text.contains('\n') &&
                !HtmlTagRegex.containsMatchIn(text) &&
                !NO_ALIGN_DIRECTIVES.any { text.startsWith(it) }
    }

    /**
     * Extract the directive name (first identifier) from a node.
     * Handles [Types.DIRECTIVE_STMT] wrappers transparently.
     */
    private fun directiveName(node: ASTNode): String {
        val target = unwrapDirective(node)
        val nameNode = target.findChildByType(Types.NAME_STMT)
            ?: target.findChildByType(Types.IDENTIFIER)
        return nameNode?.text?.trim() ?: ""
    }

    /**
     * Unwrap a [Types.DIRECTIVE_STMT] wrapper to get the inner concrete directive node.
     */
    private fun unwrapDirective(node: ASTNode): ASTNode {
        if (node.elementType == Types.DIRECTIVE_STMT) {
            var child = node.firstChildNode
            while (child != null) {
                if (isNonBlankNode(child)) {
                    return child
                }
                child = child.treeNext
            }
        }
        return node
    }

    private fun indentSize(): Int =
        settings.getCommonSettings(NginxLanguage).indentOptions?.INDENT_SIZE ?: 4

    /**
     * Count the number of [BLOCK_TYPES] ancestors of [node], representing its nesting depth.
     */
    private fun blockDepth(node: ASTNode): Int {
        var depth = 0
        var parent = node.treeParent
        while (parent != null) {
            if (parent.elementType in BLOCK_TYPES) depth++
            parent = parent.treeParent
        }
        return depth
    }

    private fun isBlockScope(): Boolean {
        return myNode.elementType in listOf(
            Types.BLOCK_STMT,
            Types.MAP_BLOCK_STMT,
            Types.GEO_BLOCK_STMT
        ) || isFileRoot()
    }

    /**
     * Check if this block is the [Types.BLOCK_STMT] inside an `upstream` directive.
     */
    private fun isUpstreamBlock(): Boolean {
        if (myNode.elementType != Types.BLOCK_STMT) return false
        val parent = myNode.treeParent ?: return false
        if (parent.elementType != Types.REGULAR_DIRECTIVE_STMT) return false
        return directiveName(parent) == "upstream"
    }

    // endregion

    // region Indent calculation

    /**
     * Matches [cc.ayakurayuki.formatter.classic.performIndentation]:
     * - Braces: no indent
     * - Block contents: normal indent
     * - Directive children: [calculateDirectiveChildIndent]
     */
    private fun calculateChildIndent(childNode: ASTNode): Indent {
        val childType = childNode.elementType
        val parentType = myNode.elementType

        if (childType == Types.LBRACE || childType == Types.RBRACE) return Indent.getNoneIndent()

        if (parentType in BLOCK_TYPES) return Indent.getNormalIndent()

        if (parentType in DIRECTIVE_TYPES) return calculateDirectiveChildIndent(childNode)

        return Indent.getNoneIndent()
    }

    /**
     * Matches [cc.ayakurayuki.formatter.classic.performIndentation]:
     * - Name (index 0) and first value (index 1): no indent
     * - Semicolons / braces: no indent
     * - Special directives (log_format, map, geo): space indent for continuation
     * - Other continuation values: normal indent
     */
    private fun calculateDirectiveChildIndent(childNode: ASTNode): Indent {
        val children = collectChildren()
        val childIndex = children.indexOf(childNode)
        if (childIndex <= 1) return Indent.getNoneIndent()

        if (childNode.elementType in listOf(Types.SEMICOLON, Types.LBRACE, Types.RBRACE)) {
            return Indent.getNoneIndent()
        }

        if (customSettings.specialAlignMultilineDirectives) {
            val name = directiveName(myNode)
            if (name in SPECIAL_ALIGN_DIRECTIVES) {
                return Indent.getSpaceIndent(calculateSpecialAlignColumn(children))
            }
        }

        return Indent.getNormalIndent()
    }

    private fun calculateSpecialAlignColumn(children: List<ASTNode>): Int {
        if (children.size < 2) return 0
        val name = children[0].text.trim()
        val firstParam = children[1].text.trim()
        val firstParamStart = if (valueAlignColumn > 0) valueAlignColumn else name.length + 1
        return firstParamStart + firstParam.length + 1
    }

    // endregion

    // region Spacing

    override fun getSpacing(child1: Block?, child2: Block): Spacing? {
        if (child1 is NginxBlock && child2 is NginxBlock && myNode.elementType in DIRECTIVE_TYPES) {
            return getDirectiveChildSpacing(child1, child2)
        }
        return spacingBuilder.getSpacing(this, child1, child2)
    }

    /**
     * Spacing between children of a directive statement.
     *
     * Value alignment is only applied when the directive has no block child,
     * matching [cc.ayakurayuki.formatter.classic.performAlignment]'s
     * `trimmed.endsWith(";")` filter — block directives like `upstream back {`
     * naturally fall through to default single-space.
     */
    private fun getDirectiveChildSpacing(child1: NginxBlock, child2: NginxBlock): Spacing? {
        val c1Type = child1.node.elementType
        val c2Type = child2.node.elementType

        // Before semicolons: no space
        if (c2Type == Types.SEMICOLON) {
            return Spacing.createSpacing(0, 0, 0, false, 0)
        }

        // Before block: space before left brace (e.g. "server {" vs "server{")
        if (c2Type in BLOCK_TYPES) {
            val spaces = if (customSettings.spaceBeforeLbrace) 1 else 0
            return Spacing.createSpacing(spaces, spaces, 0, false, 0)
        }

        // Value alignment: only for single-line directives (no block → endsWith(";"))
        if (valueAlignColumn > 0 &&
            (c1Type == Types.NAME_STMT || c1Type == Types.IDENTIFIER) &&
            myNode.findChildByType(Types.BLOCK_STMT) == null
        ) {
            val nameLength = child1.node.text.trim().length
            val spaces = maxOf(1, valueAlignColumn - nameLength)
            return Spacing.createSpacing(spaces, spaces, 0, false, 0)
        }

        // Default: single space
        return Spacing.createSpacing(1, 1, 0, true, 0)
    }

    // endregion

    override fun getChildIndent(): Indent? =
        when (myNode.elementType) {
            Types.BLOCK_STMT,
            Types.MAP_BLOCK_STMT,
            Types.GEO_BLOCK_STMT,
            Types.LUA_BLOCK_STMT,
                -> Indent.getNormalIndent()

            else -> Indent.getNoneIndent()
        }

    override fun getChildAttributes(newChildIndex: Int): ChildAttributes =
        ChildAttributes(childIndent, null)

    override fun isLeaf(): Boolean = node.firstChildNode == null

    private fun isFileRoot(): Boolean = myNode.psi.containingFile.node == myNode

    private fun isNonBlankNode(child: ASTNode): Boolean = child.elementType != TokenType.WHITE_SPACE && child.textLength > 0

}

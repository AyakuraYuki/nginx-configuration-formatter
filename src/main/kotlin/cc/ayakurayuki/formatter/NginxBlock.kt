package cc.ayakurayuki.formatter

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.util.TextRange
import com.intellij.psi.codeStyle.CodeStyleSettings
import org.jetbrains.annotations.Unmodifiable

/**
 * @author Ayakura Yuki
 * @date 2026/01/30-21:05
 */
class NginxBlock(
    private var node: ASTNode,
    private var alignment: Alignment?,
    private var indent: Indent?,
    private var settings: CodeStyleSettings
) : Block {

    companion object {
        private val LOG = logger<NginxBlock>()
    }

    override fun getTextRange(): TextRange = node.textRange

    override fun getSubBlocks(): @Unmodifiable List<Block?> {
        val blocks = mutableListOf<Block>()
        var child = node.firstChildNode

        while (child != null) {
            if (child.textLength > 0 && child.text.trim().isNotEmpty()) {
                val childIndent = calculateIndent(child)
                blocks.add(NginxBlock(child, null, childIndent, settings))
            }
            child = child.treeNext
        }

        return blocks
    }

    private fun calculateIndent(child: ASTNode): Indent {
        val text = child.text.trim()
        val parentText = node.text.trim()
        if (text == "{" || text == "}") {
            return Indent.getNoneIndent()
        }
        if (parentText.contains("{")) {
            return Indent.getNormalIndent()
        }
        return Indent.getNoneIndent()
    }

    override fun getWrap(): Wrap? = null

    override fun getIndent(): Indent? = indent

    override fun getAlignment(): Alignment? = alignment

    override fun getSpacing(child1: Block?, child2: Block): Spacing? {
        return Spacing.createSpacing(
            1,
            1,
            0,
            true,
            1
        )
    }

    override fun getChildAttributes(newChildIndex: Int): ChildAttributes {
        return ChildAttributes(Indent.getNormalIndent(), null)
    }

    override fun isIncomplete(): Boolean = false

    override fun isLeaf(): Boolean = node.firstChildNode == null

}

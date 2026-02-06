/**
 * Original work:
 *   https://github.com/meanmail-dev/nginx-intellij-plugin
 *   Copyright (c) 2020 meanmail.dev
 *   Licensed under the MIT License
 */

package cc.ayakurayuki.folding

import cc.ayakurayuki.psi.BlockStmt
import cc.ayakurayuki.psi.LuaBlockStmt
import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

class NginxFoldingBuilder : FoldingBuilderEx(), DumbAware {
    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        val descriptors: MutableList<FoldingDescriptor> = ArrayList()
        val blocks = PsiTreeUtil.findChildrenOfAnyType(
            root,
            BlockStmt::class.java,
            LuaBlockStmt::class.java
        )
        for (block in blocks) {
            // only create folding region if block has content (more than just braces)
            val start = block.textRange.startOffset + 1
            val end = block.textRange.endOffset - 1
            if (end > start) {
                descriptors.add(
                    FoldingDescriptor(block.node, TextRange(start, end))
                )
            }
        }
        return descriptors.toTypedArray()
    }

    override fun getPlaceholderText(node: ASTNode): String {
        return "..."
    }

    override fun isCollapsedByDefault(node: ASTNode): Boolean {
        return false
    }
}

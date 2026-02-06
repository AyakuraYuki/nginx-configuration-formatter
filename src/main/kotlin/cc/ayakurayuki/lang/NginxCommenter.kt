package cc.ayakurayuki.lang

import com.intellij.codeInsight.generation.IndentedCommenter

/**
 * @author Ayakura Yuki
 * @date 2026/02/06-14:39
 */
class NginxCommenter : IndentedCommenter {

    override fun forceIndentedLineComment(): Boolean = true

    override fun getLineCommentPrefix(): String = "# "

    override fun getBlockCommentPrefix(): String? = null

    override fun getBlockCommentSuffix(): String? = null

    override fun getCommentedBlockCommentPrefix(): String? = null

    override fun getCommentedBlockCommentSuffix(): String? = null

}

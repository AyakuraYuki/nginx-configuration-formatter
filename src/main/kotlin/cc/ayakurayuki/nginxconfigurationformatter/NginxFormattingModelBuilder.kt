package cc.ayakurayuki.nginxconfigurationformatter

import com.intellij.formatting.FormattingContext
import com.intellij.formatting.FormattingModel
import com.intellij.formatting.FormattingModelBuilder
import com.intellij.formatting.FormattingModelProvider
import com.intellij.lang.ASTNode
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile

/**
 * @author Ayakura Yuki
 * @date 2026/01/30-17:59
 */
class NginxFormattingModelBuilder : FormattingModelBuilder {

    companion object {
        private val LOG = logger<NginxFormattingModelBuilder>()
    }

    override fun createModel(formattingContext: FormattingContext): FormattingModel {
        val settings = formattingContext.codeStyleSettings
        val element = formattingContext.psiElement
        return FormattingModelProvider.createFormattingModelForPsiFile(
            element.containingFile,
            NginxBlock(element.node, null, null, settings),
            settings,
        )
    }

    override fun getRangeAffectingIndent(file: PsiFile?, offset: Int, elementAtOffset: ASTNode?): TextRange? = null

}

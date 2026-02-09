package cc.ayakurayuki.formatter.psi

import cc.ayakurayuki.lang.NginxLanguage
import cc.ayakurayuki.psi.Types
import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleSettings

/**
 * @author Ayakura Yuki
 * @date 2026/01/30-17:59
 */
class NginxFormattingModelBuilder : FormattingModelBuilder {

    override fun createModel(formattingContext: FormattingContext): FormattingModel {
        val settings = formattingContext.codeStyleSettings
        val element = formattingContext.psiElement

        val spacingBuilder = createSpacingBuilder(settings)
        val rootBlock = NginxBlock(element.node, null, null, spacingBuilder, settings)

        return FormattingModelProvider.createFormattingModelForPsiFile(element.containingFile, rootBlock, settings)
    }

    override fun getRangeAffectingIndent(file: PsiFile?, offset: Int, elementAtOffset: ASTNode?): TextRange? = null

    private fun createSpacingBuilder(settings: CodeStyleSettings): SpacingBuilder {
        return SpacingBuilder(settings, NginxLanguage)
            .before(Types.SEMICOLON).none()
            .after(Types.SEMICOLON).lineBreakInCode()

            .after(Types.LBRACE).lineBreakInCode()
            .before(Types.RBRACE).lineBreakInCode()

            .after(Types.COMMENT).lineBreakInCode()

            .between(Types.IDENTIFIER, Types.IDENTIFIER).spaces(1)
            .between(Types.IDENTIFIER, Types.VALUE).spaces(1)
            .between(Types.IDENTIFIER, Types.VARIABLE).spaces(1)
            .between(Types.VALUE, Types.VALUE).spaces(1)

            .after(Types.QUOTE).none()
            .before(Types.QUOTE).none()
            .after(Types.DQUOTE).none()
            .before(Types.DQUOTE).none()
    }

}

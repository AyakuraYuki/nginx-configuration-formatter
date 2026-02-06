/**
 * Original work:
 *   https://github.com/meanmail-dev/nginx-intellij-plugin
 *   Copyright (c) 2020 meanmail.dev
 *   Licensed under the MIT License
 */

package cc.ayakurayuki.syntax

import cc.ayakurayuki.lang.NginxFile
import cc.ayakurayuki.lang.NginxLanguage
import cc.ayakurayuki.psi.Types
import cc.ayakurayuki.psi.parser.NginxParser
import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet

class NginxParserDefinition : ParserDefinition {

    override fun createLexer(project: Project): Lexer {
        return NginxLexerAdapter()
    }

    override fun getWhitespaceTokens(): TokenSet {
        return WHITE_SPACES
    }

    override fun getCommentTokens(): TokenSet {
        return COMMENTS
    }

    override fun getStringLiteralElements(): TokenSet {
        return TokenSet.EMPTY
    }

    override fun createParser(project: Project): PsiParser {
        return NginxParser()
    }

    override fun getFileNodeType(): IFileElementType {
        return FILE
    }

    override fun createFile(viewProvider: FileViewProvider): PsiFile {
        return NginxFile(viewProvider)
    }

    override fun spaceExistenceTypeBetweenTokens(left: ASTNode, right: ASTNode): ParserDefinition.SpaceRequirements {
        return ParserDefinition.SpaceRequirements.MAY
    }

    override fun createElement(node: ASTNode): PsiElement {
        return Types.Factory.createElement(node)
    }

}

val WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE)
val COMMENTS = TokenSet.create(Types.COMMENT)
val FILE = IFileElementType(NginxLanguage)

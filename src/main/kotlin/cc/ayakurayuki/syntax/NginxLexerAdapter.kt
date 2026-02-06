package cc.ayakurayuki.syntax

import cc.ayakurayuki.NginxLexer
import com.intellij.lexer.FlexAdapter

/**
 * @author Ayakura Yuki
 * @date 2026/02/05-22:46
 */
class NginxLexerAdapter : FlexAdapter(NginxLexer()) {

    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        val lexer = flex as? NginxLexer
        lexer?.yyinitial()
        super.start(buffer, startOffset, endOffset, initialState)
    }

}

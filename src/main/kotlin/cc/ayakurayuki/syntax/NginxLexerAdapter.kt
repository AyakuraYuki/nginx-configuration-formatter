package cc.ayakurayuki.syntax

import cc.ayakurayuki.NginxLexer
import com.intellij.lexer.FlexAdapter
import java.io.Reader

/**
 * @author Ayakura Yuki
 * @date 2026/02/05-22:46
 */
class NginxLexerAdapter : FlexAdapter(NginxLexer(null as Reader?))

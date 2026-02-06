package cc.ayakurayuki.lang

import com.intellij.lang.Language

/**
 * @author Ayakura Yuki
 * @date 2026/01/30-17:55
 */
object NginxLanguage : Language("Nginx") {

    private fun readResolve(): Any = NginxLanguage

}

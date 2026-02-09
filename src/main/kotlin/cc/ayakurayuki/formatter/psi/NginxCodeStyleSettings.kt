package cc.ayakurayuki.formatter.psi

import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CustomCodeStyleSettings

/**
 * @author Ayakura Yuki
 * @date 2026/02/06-16:14
 */
class NginxCodeStyleSettings(settings: CodeStyleSettings) :
    CustomCodeStyleSettings("NginxCodeStyleSettings", settings) {

    companion object {
        const val ALIGN_OFF = 0 // disable alignment for directive parameters
        const val ALIGN_WITHIN_BLOCK = 1 // enable alignment for directive parameters within block
        const val ALIGN_ACROSS_BLOCKS = 2 // enable alignment for directive parameters across all blocks
    }

    // directive parameter alignment mode: 0=off, 1=within block, 2=across all blocks
    @JvmField
    var alignPropertyValuesMode: Int = ALIGN_OFF

    // add space before left brace, or not
    @JvmField
    var spaceBeforeLbrace: Boolean = true

    // multiline directives alignment, or not, such as log_format, map, geo, etc.
    @JvmField
    var specialAlignMultilineDirectives: Boolean = true

}

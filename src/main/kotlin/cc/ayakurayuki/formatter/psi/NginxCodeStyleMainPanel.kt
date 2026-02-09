package cc.ayakurayuki.formatter.psi

import cc.ayakurayuki.lang.NginxLanguage
import com.intellij.application.options.TabbedLanguageCodeStylePanel
import com.intellij.psi.codeStyle.CodeStyleSettings

/**
 * @author Ayakura Yuki
 * @date 2026/02/09-20:00
 */
class NginxCodeStyleMainPanel(currentSettings: CodeStyleSettings, settings: CodeStyleSettings) :
    TabbedLanguageCodeStylePanel(NginxLanguage, currentSettings, settings) {

    override fun initTabs(settings: CodeStyleSettings) {
        addIndentOptionsTab(settings);
        addTab(NginxFormattingPanel(settings))
    }

}

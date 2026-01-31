package cc.ayakurayuki.nginxconfigurationformatter

import com.intellij.icons.AllIcons
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.util.NlsSafe
import org.jetbrains.annotations.NonNls
import javax.swing.Icon

/**
 * @author Ayakura Yuki
 * @date 2026/01/30-17:56
 */
class NginxFileType : LanguageFileType(NginxLanguage) {

    override fun getName(): @NonNls String = NginxLanguage.id

    override fun getDescription(): @NonNls String = "Nginx configuration file"

    override fun getDefaultExtension(): @NlsSafe String = "conf"

    override fun getIcon(): Icon = AllIcons.FileTypes.Config

}

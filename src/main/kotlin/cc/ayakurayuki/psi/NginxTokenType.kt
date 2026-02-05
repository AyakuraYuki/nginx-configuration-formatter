package cc.ayakurayuki.psi

import cc.ayakurayuki.nginxconfigurationformatter.NginxLanguage
import com.intellij.psi.tree.IElementType
import org.jetbrains.annotations.NonNls

class NginxTokenType(@NonNls debugName: String) : IElementType(debugName, NginxLanguage) {
    override fun toString(): String {
        return "NginxTokenType.${super.toString()}"
    }
}

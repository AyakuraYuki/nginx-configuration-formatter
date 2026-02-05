package cc.ayakurayuki.psi

import cc.ayakurayuki.nginxconfigurationformatter.NginxLanguage
import com.intellij.psi.tree.IElementType
import org.jetbrains.annotations.NonNls

class NginxElementType(@NonNls debugName: String) : IElementType(debugName, NginxLanguage)

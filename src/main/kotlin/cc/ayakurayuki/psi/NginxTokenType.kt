/**
 * Original work:
 *   https://github.com/meanmail-dev/nginx-intellij-plugin
 *   Copyright (c) 2020 meanmail.dev
 *   Licensed under the MIT License
 */

package cc.ayakurayuki.psi

import cc.ayakurayuki.lang.NginxLanguage
import com.intellij.psi.tree.IElementType
import org.jetbrains.annotations.NonNls

class NginxTokenType(@NonNls debugName: String) : IElementType(debugName, NginxLanguage) {
    override fun toString(): String {
        return "NginxTokenType.${super.toString()}"
    }
}

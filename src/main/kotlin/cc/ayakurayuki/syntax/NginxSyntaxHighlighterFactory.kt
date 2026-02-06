/**
 * Original work:
 *   https://github.com/meanmail-dev/nginx-intellij-plugin
 *   Copyright (c) 2020 meanmail.dev
 *   Licensed under the MIT License
 */

package cc.ayakurayuki.syntax

import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

class NginxSyntaxHighlighterFactory : SyntaxHighlighterFactory() {

    override fun getSyntaxHighlighter(project: Project?, virtualFile: VirtualFile?): SyntaxHighlighter {
        return NginxSyntaxHighlighter()
    }

}

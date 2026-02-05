package cc.ayakurayuki.psi

import com.intellij.openapi.paths.WebReference
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiReferenceBase
import java.io.File

interface ReferenceElement : NamedElement {
    val ref: String
}

class Reference(element: ReferenceElement) : PsiReferenceBase<ReferenceElement>(element, null, false) {
    override fun resolve(): PsiElement? {
        return resolveFile(element.ref)
    }

    private fun resolveFile(ref: String): PsiElement? {
        if (ref.isEmpty()) return null

        if (ref.startsWith("http://") || ref.startsWith("https://")) {
            return WebReference(element, ref).resolve()
        }

        val directory = element.containingFile?.containingDirectory?.virtualFile ?: return null
        val file = resolveFile(ref, directory) ?: return null

        return PsiManager.getInstance(element.project).findFile(file)
    }

    override fun getRangeInElement(): TextRange {
        return TextRange(0, element.textLength)
    }

    override fun handleElementRename(newElementName: String): PsiElement? {
        val newOne = element.setName(newElementName)
        element.parent.node.replaceChild(element.node, newOne.node)
        return newOne
    }
}

fun resolveFile(filepath: String, base: VirtualFile): VirtualFile? {
    val target = File(filepath)

    if (target.isAbsolute) {
        return base.fileSystem.findFileByPath(filepath)
    }

    base.findFileByRelativePath(filepath)?.let { return it }

    var currentDir = base
    while (currentDir.parent != null) {
        currentDir = currentDir.parent
        if (currentDir.findChild("nginx.conf") != null) {
            // found nginx root directory, try to resolve relative to it
            currentDir.findFileByRelativePath(filepath)?.let { return it }
            break
        }
    }

    return null
}

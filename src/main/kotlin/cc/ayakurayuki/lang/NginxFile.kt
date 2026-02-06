package cc.ayakurayuki.lang

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider

class NginxFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, NginxLanguage) {

    override fun getFileType(): FileType = NginxFileType()

    override fun toString(): String = fileType.description

}

package cc.ayakurayuki.nginxconfigurationformatter

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.diagnostic.logger
import com.intellij.psi.PsiDocumentManager

/**
 * @author Ayakura Yuki
 * @date 2026/01/30-21:11
 */
class NginxFormatAction : AnAction() {

    companion object {
        private val LOG = logger<NginxFormatAction>()
    }

    override fun getActionUpdateThread(): ActionUpdateThread = super.getActionUpdateThread()

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project
        val editor = e.getData(CommonDataKeys.EDITOR)

        if (project == null || editor == null) {
            LOG.warn("warning: both project and editor are required (project: $project, editor: $editor)")
            return
        }

        val document = editor.document

        WriteCommandAction.runWriteCommandAction(project) {
            val formatted = formatNginxConfig(document.text, "    ", false)
            document.setText(formatted)
            PsiDocumentManager.getInstance(project).commitDocument(document)
        }
    }

    override fun update(e: AnActionEvent) {
        val editor = e.getData(CommonDataKeys.EDITOR)
        e.presentation.isEnabledAndVisible = editor != null
    }

}

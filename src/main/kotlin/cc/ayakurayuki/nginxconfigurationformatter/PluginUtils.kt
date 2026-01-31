package cc.ayakurayuki.nginxconfigurationformatter

import com.intellij.codeInsight.hint.HintManager
import com.intellij.codeInsight.hint.HintManagerImpl
import com.intellij.codeInsight.hint.HintUtil
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.ui.LightweightHint

private const val PLUGIN_GROUP_ID = "NginxConfigurationFormatter"

fun showNotification(project: Project, content: String, type: NotificationType) {
    NotificationGroupManager.getInstance()
        .getNotificationGroup(PLUGIN_GROUP_ID)
        .createNotification(content, type)
        .notify(project)
}

fun showHintAtCursor(editor: Editor, message: String) {
    val hintManager = HintManager.getInstance() as HintManagerImpl
    val label = HintUtil.createInformationLabel(message)
    val hint = LightweightHint(label)
    val point = HintManagerImpl.getHintPosition(
        hint,
        editor,
        editor.caretModel.visualPosition,
        HintManager.ABOVE
    )
    val flags = HintManager.HIDE_BY_ANY_KEY or
            HintManager.HIDE_BY_SCROLLING or
            HintManager.HIDE_BY_TEXT_CHANGE or
            HintManager.HIDE_BY_OTHER_HINT
    hintManager.showEditorHint(
        hint,
        editor,
        point,
        flags,
        5000,
        true,
        HintManager.ABOVE
    )
}

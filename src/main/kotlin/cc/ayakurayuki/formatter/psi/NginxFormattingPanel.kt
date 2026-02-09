package cc.ayakurayuki.formatter.psi

import cc.ayakurayuki.I18NMessageBundle.message
import cc.ayakurayuki.lang.NginxFileType
import cc.ayakurayuki.lang.NginxLanguage
import cc.ayakurayuki.syntax.NginxSyntaxHighlighter
import com.intellij.application.options.CodeStyleAbstractPanel
import com.intellij.lang.Language
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.colors.EditorColorsScheme
import com.intellij.openapi.editor.ex.EditorEx
import com.intellij.openapi.editor.highlighter.EditorHighlighter
import com.intellij.openapi.editor.highlighter.EditorHighlighterFactory
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.ui.ComboBox
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CodeStyleSettingsManager
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider
import com.intellij.ui.JBSplitter
import com.intellij.ui.TitledSeparator
import com.intellij.util.ui.FormBuilder
import java.awt.BorderLayout
import javax.swing.JCheckBox
import javax.swing.JComponent
import javax.swing.JPanel

/**
 * @author Ayakura Yuki
 * @date 2026/02/09-20:00
 */
class NginxFormattingPanel(private val settings: CodeStyleSettings) : CodeStyleAbstractPanel(settings) {

    private val spaceBeforeLbraceCheckBox = JCheckBox(message("codestyle.nginx.formatting.option.space-before-lbrace.text"))
    private val alignModeComboBox = ComboBox(
        arrayOf(
            message("codestyle.nginx.formatting.option.align-mode.opt.off"), // 0 - ALIGN_OFF
            message("codestyle.nginx.formatting.option.align-mode.opt.within-block"), // 1 - ALIGN_WITHIN_BLOCK
            message("codestyle.nginx.formatting.option.align-mode.opt.across-blocks") // 2 - ALIGN_ACROSS_BLOCKS
        )
    )
    private val specialAlignCheckBox = JCheckBox(message("codestyle.nginx.formatting.option.special-alignment-directive.text"))

    private val formattingPanel: JPanel
    private val previewEditor: EditorEx
    private val optionsPanel: JPanel = FormBuilder.createFormBuilder()
        .addComponent(TitledSeparator("Braces"))
        .setFormLeftIndent(20)
        .addComponent(spaceBeforeLbraceCheckBox)
        .setFormLeftIndent(0)
        .addComponent(TitledSeparator("Directives"))
        .setFormLeftIndent(20)
        .addLabeledComponent("Align directive parameters:", alignModeComboBox)
        .addComponent(specialAlignCheckBox)
        .setFormLeftIndent(0)
        .addComponentFillVertically(JPanel(), 0)
        .panel

    init {
        val document = EditorFactory.getInstance().createDocument("")
        previewEditor = EditorFactory.getInstance()
            .createViewer(document, ProjectManager.getInstance().defaultProject) as EditorEx

        previewEditor.settings.apply {
            isLineMarkerAreaShown = false
            isLineNumbersShown = true
            isFoldingOutlineShown = false
            isRightMarginShown = false
            additionalColumnsCount = 0
            additionalLinesCount = 0
            isAdditionalPageAtBottom = false
        }
        previewEditor.highlighter = createHighlighter(previewEditor.colorsScheme)

        val splitter = JBSplitter(true, 0.3f)
        splitter.firstComponent = optionsPanel
        splitter.secondComponent = previewEditor.component

        formattingPanel = JPanel(BorderLayout())
        formattingPanel.add(splitter, BorderLayout.CENTER)

        spaceBeforeLbraceCheckBox.addActionListener { onOptionChanged() }
        alignModeComboBox.addActionListener { onOptionChanged() }
        specialAlignCheckBox.addActionListener { onOptionChanged() }
    }

    private fun onOptionChanged() {
        somethingChanged()
        updatePreviewEditor()
    }

    private fun updatePreviewEditor() {
        val text = getPreviewText()
        if (text.isEmpty()) return

        val project = ProjectManager.getInstance().defaultProject

        try {
            @Suppress("DEPRECATION")
            val tempSettings = settings.clone()
            apply(tempSettings)

            val psiFile = PsiFileFactory.getInstance(project)
                .createFileFromText("preview.conf", getFileType(), text)

            val manager = CodeStyleSettingsManager.getInstance(project)
            manager.setTemporarySettings(tempSettings)
            try {
                ApplicationManager.getApplication().runWriteAction {
                    CodeStyleManager.getInstance(project).reformat(psiFile)
                }
            } finally {
                manager.dropTemporarySettings()
            }

            ApplicationManager.getApplication().runWriteAction {
                previewEditor.document.setText(psiFile.text)
            }
        } catch (_: Exception) {
            // Silently handle preview update failures
        }
    }

    override fun getTabTitle(): String = message("codestyle.nginx.formatting.panel.title")

    override fun getRightMargin(): Int = 0

    override fun createHighlighter(scheme: EditorColorsScheme): EditorHighlighter =
        EditorHighlighterFactory.getInstance().createEditorHighlighter(NginxSyntaxHighlighter(), scheme)

    override fun getFileType(): FileType = NginxFileType()

    override fun getDefaultLanguage(): Language = NginxLanguage

    override fun getPreviewText(): String =
        LanguageCodeStyleSettingsProvider.forLanguage(NginxLanguage)
            ?.getCodeSample(LanguageCodeStyleSettingsProvider.SettingsType.SPACING_SETTINGS)
            ?: ""

    override fun getPanel(): JComponent = formattingPanel

    override fun apply(settings: CodeStyleSettings) {
        val custom = settings.getCustomSettings(NginxCodeStyleSettings::class.java)
        custom.spaceBeforeLbrace = spaceBeforeLbraceCheckBox.isSelected
        custom.alignPropertyValuesMode = alignModeComboBox.selectedIndex
        custom.specialAlignMultilineDirectives = specialAlignCheckBox.isSelected
    }

    override fun isModified(settings: CodeStyleSettings): Boolean {
        val custom = settings.getCustomSettings(NginxCodeStyleSettings::class.java)
        return spaceBeforeLbraceCheckBox.isSelected != custom.spaceBeforeLbrace ||
                alignModeComboBox.selectedIndex != custom.alignPropertyValuesMode ||
                specialAlignCheckBox.isSelected != custom.specialAlignMultilineDirectives
    }

    override fun resetImpl(settings: CodeStyleSettings) {
        val custom = settings.getCustomSettings(NginxCodeStyleSettings::class.java)
        spaceBeforeLbraceCheckBox.isSelected = custom.spaceBeforeLbrace
        alignModeComboBox.selectedIndex = custom.alignPropertyValuesMode.coerceIn(0, alignModeComboBox.itemCount - 1)
        specialAlignCheckBox.isSelected = custom.specialAlignMultilineDirectives
        updatePreviewEditor()
    }

    override fun dispose() {
        EditorFactory.getInstance().releaseEditor(previewEditor)
        super.dispose()
    }

}

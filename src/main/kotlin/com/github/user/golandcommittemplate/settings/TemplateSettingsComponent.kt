package com.github.user.golandcommittemplate.settings

import com.github.user.golandcommittemplate.model.CommitTemplate
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.ui.table.JBTable
import com.intellij.util.ui.FormBuilder
import java.awt.BorderLayout
import javax.swing.*
import javax.swing.table.AbstractTableModel

/**
 * Component for displaying and editing commit template settings.
 */
class TemplateSettingsComponent {
    val panel: JPanel
    val preferredFocusedComponent: JComponent
    val showEmojiCheckbox: JBCheckBox
    val maxSubjectLengthField: JBTextField
    val fillSubjectWithBranchCheckbox: JBCheckBox
    
    private val templatesModel: TemplatesTableModel
    private val templatesTable: JBTable

    init {
        showEmojiCheckbox = JBCheckBox("Show emoji in commit types")
        maxSubjectLengthField = JBTextField()
        fillSubjectWithBranchCheckbox = JBCheckBox("Fill subject with current branch name")
        
        preferredFocusedComponent = showEmojiCheckbox
        
        // Templates table
        templatesModel = TemplatesTableModel()
        templatesTable = JBTable(templatesModel)
        templatesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
        
        val templatesPanel = JPanel(BorderLayout())
        val toolbarDecorator = ToolbarDecorator.createDecorator(templatesTable)
            .setAddAction { addTemplate() }
            .setRemoveAction { removeTemplate() }
            .setEditAction { editTemplate() }
        
        templatesPanel.add(toolbarDecorator.createPanel(), BorderLayout.CENTER)
        
        panel = FormBuilder.createFormBuilder()
            .addLabeledComponent(JBLabel("Max subject length:"), maxSubjectLengthField, 1, false)
            .addComponent(showEmojiCheckbox, 1)
            .addComponent(fillSubjectWithBranchCheckbox, 1)
            .addSeparator()
            .addLabeledComponentFillVertically("Templates:", templatesPanel)
            .getPanel()
    }

    fun getTemplates(): List<CommitTemplate> {
        return templatesModel.templates
    }

    fun setTemplates(templates: List<CommitTemplate>) {
        templatesModel.templates.clear()
        templatesModel.templates.addAll(templates)
        templatesModel.fireTableDataChanged()
    }

    private fun addTemplate() {
        val dialog = TemplateDialog(null)
        if (dialog.showAndGet()) {
            val template = dialog.getTemplate()
            templatesModel.templates.add(template)
            templatesModel.fireTableDataChanged()
        }
    }

    private fun removeTemplate() {
        val selectedRow = templatesTable.selectedRow
        if (selectedRow >= 0 && selectedRow < templatesModel.templates.size) {
            templatesModel.templates.removeAt(selectedRow)
            templatesModel.fireTableDataChanged()
        }
    }

    private fun editTemplate() {
        val selectedRow = templatesTable.selectedRow
        if (selectedRow >= 0 && selectedRow < templatesModel.templates.size) {
            val template = templatesModel.templates[selectedRow]
            val dialog = TemplateDialog(template)
            if (dialog.showAndGet()) {
                templatesModel.templates[selectedRow] = dialog.getTemplate()
                templatesModel.fireTableDataChanged()
            }
        }
    }

    private inner class TemplatesTableModel : AbstractTableModel() {
        val templates: MutableList<CommitTemplate> = mutableListOf()
        
        override fun getRowCount(): Int = templates.size
        
        override fun getColumnCount(): Int = 2
        
        override fun getValueAt(rowIndex: Int, columnIndex: Int): Any {
            val template = templates[rowIndex]
            return when (columnIndex) {
                0 -> template.name
                1 -> template.content
                else -> ""
            }
        }
        
        override fun getColumnName(column: Int): String {
            return when (column) {
                0 -> "Name"
                1 -> "Template"
                else -> ""
            }
        }
    }
}

class TemplateDialog(template: CommitTemplate?) : DialogWrapper(true) {
    private val nameField = JBTextField()
    private val contentArea = JTextArea(10, 40)
    private val panel: JPanel
    
    init {
        title = if (template == null) "Add Template" else "Edit Template"
        
        if (template != null) {
            nameField.text = template.name
            contentArea.text = template.content
        }
        
        contentArea.lineWrap = true
        contentArea.wrapStyleWord = true
        
        panel = FormBuilder.createFormBuilder()
            .addLabeledComponent("Name:", nameField, 1, false)
            .addLabeledComponent("Template:", JScrollPane(contentArea), 1, true)
            .addComponentFillVertically(JPanel(), 0)
            .panel
        
        init()
    }
    
    override fun createCenterPanel(): JComponent = panel
    
    fun getTemplate(): CommitTemplate {
        return CommitTemplate(nameField.text, contentArea.text)
    }
}
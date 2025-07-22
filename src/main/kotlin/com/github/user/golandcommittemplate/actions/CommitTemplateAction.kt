package com.github.user.golandcommittemplate.actions

import com.github.user.golandcommittemplate.model.CommitType
import com.github.user.golandcommittemplate.services.TemplateSettingsService
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.vcs.CommitMessageI
import com.intellij.openapi.vcs.VcsDataKeys
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import com.intellij.util.ui.JBUI
import git4idea.branch.GitBranchUtil
import git4idea.repo.GitRepositoryManager
import java.awt.BorderLayout
import java.awt.Component
import java.awt.Dimension
import javax.swing.*

/**
 * Action for applying a commit template to the current commit message.
 */
class CommitTemplateAction : AnAction(), DumbAware {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val commitMessage = e.getData(VcsDataKeys.COMMIT_MESSAGE_CONTROL) as? CommitMessageI ?: return
        
        // Show commit type selection dialog
        val dialog = CommitFormDialog(project)
        if (dialog.showAndGet()) {
            val selectedType = dialog.selectedType ?: return
            val scope = dialog.scope
            val subject = dialog.subject
            val body = dialog.body
            val footer = dialog.footer
            
            // Build commit message
            val typeText = if (selectedType.emoji != null) "${selectedType.emoji} ${selectedType.key}" else selectedType.key
            
            var message = typeText
            if (scope.isNotEmpty()) {
                message += "($scope)"
            }
            message += ": $subject"
            
            if (body.isNotEmpty()) {
                message += "\n\n$body"
            }
            
            if (footer.isNotEmpty()) {
                message += "\n\n$footer"
            }
            
            // Set the message
            commitMessage.setCommitMessage(message)
        }
    }

    private fun getCurrentBranchName(project: Project): String? {
        val repositoryManager = GitRepositoryManager.getInstance(project)
        val repositories = repositoryManager.repositories
        if (repositories.isEmpty()) return null
        
        val currentRepository = repositories[0]
        return GitBranchUtil.getDisplayableBranchText(currentRepository)
    }

    private fun extractSubjectFromBranch(branchName: String): String {
        // Remove common prefixes like feature/, bugfix/, etc.
        val withoutPrefix = branchName.replace(Regex("^(feature|bugfix|hotfix|release)/"), "")
        
        // Replace hyphens and underscores with spaces
        val withSpaces = withoutPrefix.replace(Regex("[-_]"), " ")
        
        // Capitalize first letter
        return withSpaces.capitalize()
    }
}

class CommitFormDialog(project: Project) : DialogWrapper(project) {

    var selectedType: CommitType? = null
    var scope: String = ""
    var subject: String = ""
    var body: String = ""
    var footer: String = ""
    
    private val typesList = JBList(CommitType.getDefaultTypes(TemplateSettingsService.getInstance().showEmoji))
    private val scopeField = JBTextField()
    private val subjectField = JBTextField()
    private val bodyArea = JTextArea(5, 40)
    private val footerArea = JTextArea(3, 40)
    
    init {
        title = "Create Commit Message"
        init()
    }

    override fun createCenterPanel(): JComponent {
        // Types list
        typesList.selectionMode = ListSelectionModel.SINGLE_SELECTION
        typesList.cellRenderer = CommitTypeCellRenderer()
        if (typesList.model.size > 0) {
            typesList.selectedIndex = 0
        }
        
        val typesScrollPane = JBScrollPane(typesList)
        typesScrollPane.preferredSize = Dimension(400, 200)
        
        // Configure text areas
        bodyArea.lineWrap = true
        bodyArea.wrapStyleWord = true
        footerArea.lineWrap = true
        footerArea.wrapStyleWord = true
        
        // Build form
        val panel = FormBuilder.createFormBuilder()
            .addLabeledComponent("Commit Type:", typesScrollPane)
            .addLabeledComponent("Scope (optional):", scopeField)
            .addLabeledComponent("Subject:", subjectField)
            .addLabeledComponent("Body (optional):", JBScrollPane(bodyArea))
            .addLabeledComponent("Footer (optional):", JBScrollPane(footerArea))
            .addComponentFillVertically(JPanel(), 0)
            .panel
            
        panel.border = JBUI.Borders.empty(10)
        panel.preferredSize = Dimension(500, 400)
        
        return panel
    }

    override fun doOKAction() {
        val typeIndex = typesList.selectedIndex
        if (typeIndex >= 0 && typeIndex < typesList.model.size) {
            selectedType = typesList.model.getElementAt(typeIndex)
        }
        
        scope = scopeField.text.trim()
        subject = subjectField.text.trim()
        body = bodyArea.text.trim()
        footer = footerArea.text.trim()
        
        if (subject.isEmpty()) {
            // Focus on subject field if empty
            subjectField.requestFocus()
            return
        }
        
        super.doOKAction()
    }

    private inner class CommitTypeCellRenderer : DefaultListCellRenderer() {
        override fun getListCellRendererComponent(
            list: JList<*>?,
            value: Any?,
            index: Int,
            isSelected: Boolean,
            cellHasFocus: Boolean
        ): Component {
            val component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus)
            if (value is CommitType) {
                val displayText = if (value.emoji != null) {
                    "${value.emoji} ${value.key} - ${value.description}"
                } else {
                    "${value.key} - ${value.description}"
                }
                text = displayText
            }
            return component
        }
    }
}
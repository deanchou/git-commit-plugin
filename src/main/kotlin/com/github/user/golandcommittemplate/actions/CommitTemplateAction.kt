package com.github.user.golandcommittemplate.actions

import com.github.user.golandcommittemplate.model.CommitTemplate
import com.github.user.golandcommittemplate.model.CommitType
import com.github.user.golandcommittemplate.services.TemplateSettingsService
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vcs.CommitMessageI
import com.intellij.openapi.vcs.VcsDataKeys
import com.intellij.openapi.vcs.ui.CommitMessage
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBScrollPane
import com.intellij.util.ui.JBUI
import git4idea.branch.GitBranchUtil
import git4idea.repo.GitRepository
import git4idea.repo.GitRepositoryManager
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.*

/**
 * Action for applying a commit template to the current commit message.
 */
class CommitTemplateAction : AnAction(), DumbAware {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val commitMessage = e.getData(VcsDataKeys.COMMIT_MESSAGE_CONTROL) as? CommitMessageI ?: return
        
        val settings = TemplateSettingsService.getInstance()
        if (settings.templates.isEmpty()) {
            Messages.showInfoMessage(
                project,
                "No templates configured. Please add templates in Settings -> Tools -> Commit Template Settings.",
                "No Templates"
            )
            return
        }
        
        // Show template selection dialog
        val dialog = TemplateSelectionDialog(project, settings.templates)
        if (dialog.showAndGet()) {
            val selectedTemplate = dialog.selectedTemplate ?: return
            val selectedType = dialog.selectedType
            
            // Process template
            var message = selectedTemplate.content
            
            // Replace type placeholder
            if (selectedType != null) {
                val typeText = if (selectedType.emoji != null) "${selectedType.emoji} ${selectedType.key}" else selectedType.key
                message = message.replace("<type>", typeText)
            }
            
            // Replace branch name if configured
            if (settings.fillSubjectWithBranch) {
                val branchName = getCurrentBranchName(project)
                if (branchName != null) {
                    // Extract ticket ID or feature name from branch
                    val subject = extractSubjectFromBranch(branchName)
                    message = message.replace("<subject>", subject)
                }
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

class TemplateSelectionDialog(
    project: Project,
    private val templates: List<CommitTemplate>
) : DialogWrapper(project) {

    var selectedTemplate: CommitTemplate? = null
    var selectedType: CommitType? = null
    
    private val templatesList = JBList(templates.map { it.name }.toTypedArray())
    private val typesList = JBList(CommitType.getDefaultTypes(TemplateSettingsService.getInstance().showEmoji))
    
    init {
        title = "Select Commit Template"
        init()
    }

    override fun createCenterPanel(): JComponent {
        val panel = JPanel(BorderLayout())
        panel.preferredSize = Dimension(400, 300)
        
        // Templates list
        templatesList.selectionMode = ListSelectionModel.SINGLE_SELECTION
        if (templates.isNotEmpty()) {
            templatesList.selectedIndex = 0
        }
        
        val templatesPanel = JPanel(BorderLayout())
        templatesPanel.add(JLabel("Select Template:"), BorderLayout.NORTH)
        templatesPanel.add(JBScrollPane(templatesList), BorderLayout.CENTER)
        
        // Types list
        typesList.selectionMode = ListSelectionModel.SINGLE_SELECTION
        typesList.cellRenderer = CommitTypeCellRenderer()
        if (typesList.model.size > 0) {
            typesList.selectedIndex = 0
        }
        
        val typesPanel = JPanel(BorderLayout())
        typesPanel.add(JLabel("Select Commit Type:"), BorderLayout.NORTH)
        typesPanel.add(JBScrollPane(typesList), BorderLayout.CENTER)
        
        // Split panel
        val splitPane = JSplitPane(JSplitPane.VERTICAL_SPLIT, templatesPanel, typesPanel)
        splitPane.dividerLocation = 150
        splitPane.border = JBUI.Borders.empty(10)
        
        panel.add(splitPane, BorderLayout.CENTER)
        return panel
    }

    override fun doOKAction() {
        val templateIndex = templatesList.selectedIndex
        if (templateIndex >= 0 && templateIndex < templates.size) {
            selectedTemplate = templates[templateIndex]
        }
        
        val typeIndex = typesList.selectedIndex
        if (typeIndex >= 0 && typeIndex < typesList.model.size) {
            selectedType = typesList.model.getElementAt(typeIndex)
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
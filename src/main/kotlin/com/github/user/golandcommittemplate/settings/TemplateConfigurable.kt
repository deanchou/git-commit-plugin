package com.github.deanchou.gitcommithelper.settings

import com.github.deanchou.gitcommithelper.services.TemplateSettingsService
import com.intellij.openapi.options.Configurable
import javax.swing.JComponent

/**
 * Provides controller functionality for application settings.
 */
class TemplateConfigurable : Configurable {
    private var settingsComponent: TemplateSettingsComponent? = null

    override fun getDisplayName(): String {
        return "Git Commit Helper Settings"
    }

    override fun getPreferredFocusedComponent(): JComponent? {
        return settingsComponent?.preferredFocusedComponent
    }

    override fun createComponent(): JComponent {
        settingsComponent = TemplateSettingsComponent()
        return settingsComponent!!.panel
    }

    override fun isModified(): Boolean {
        val settings = TemplateSettingsService.getInstance()
        var modified = settingsComponent?.showEmojiCheckbox?.isSelected != settings.showEmoji
        modified = modified || settingsComponent?.maxSubjectLengthField?.text?.toIntOrNull() != settings.maxSubjectLength
        modified = modified || settingsComponent?.fillSubjectWithBranchCheckbox?.isSelected != settings.fillSubjectWithBranch
        
        // Check if templates are modified
        val uiTemplates = settingsComponent?.getTemplates() ?: return modified
        if (uiTemplates.size != settings.templates.size) {
            return true
        }
        
        for (i in uiTemplates.indices) {
            if (i >= settings.templates.size || 
                uiTemplates[i].name != settings.templates[i].name || 
                uiTemplates[i].content != settings.templates[i].content) {
                return true
            }
        }
        
        return modified
    }

    override fun apply() {
        val settings = TemplateSettingsService.getInstance()
        settings.showEmoji = settingsComponent?.showEmojiCheckbox?.isSelected ?: true
        settings.maxSubjectLength = settingsComponent?.maxSubjectLengthField?.text?.toIntOrNull() ?: 72
        settings.fillSubjectWithBranch = settingsComponent?.fillSubjectWithBranchCheckbox?.isSelected ?: false
        
        // Apply templates
        val uiTemplates = settingsComponent?.getTemplates()
        if (uiTemplates != null) {
            settings.templates.clear()
            settings.templates.addAll(uiTemplates)
        }
    }

    override fun reset() {
        val settings = TemplateSettingsService.getInstance()
        settingsComponent?.showEmojiCheckbox?.isSelected = settings.showEmoji
        settingsComponent?.maxSubjectLengthField?.text = settings.maxSubjectLength.toString()
        settingsComponent?.fillSubjectWithBranchCheckbox?.isSelected = settings.fillSubjectWithBranch
        settingsComponent?.setTemplates(settings.templates)
    }

    override fun disposeUIResources() {
        settingsComponent = null
    }
}
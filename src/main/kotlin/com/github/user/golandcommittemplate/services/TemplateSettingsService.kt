package com.github.deanchou.gitcommithelper.services

import com.github.deanchou.gitcommithelper.model.CommitTemplate
import com.intellij.openapi.components.*
import com.intellij.util.xmlb.XmlSerializerUtil

/**
 * Service for storing and managing commit template settings.
 */
@Service
@State(
    name = "com.github.deanchou.gitcommithelper.services.TemplateSettingsService",
    storages = [Storage("commitTemplateSettings.xml")]
)
class TemplateSettingsService : PersistentStateComponent<TemplateSettingsService> {
    var templates: MutableList<CommitTemplate> = mutableListOf(
        CommitTemplate(
            "Angular",
            "<type>(<scope>): <subject>\n\n<body>\n\n<footer>"
        ),
        CommitTemplate(
            "Conventional",
            "<type>(<scope>): <subject>\n\n<body>\n\nBREAKING CHANGE: <breaking changes>\n\n<footer>"
        )
    )
    
    var showEmoji: Boolean = true
    var maxSubjectLength: Int = 72
    var fillSubjectWithBranch: Boolean = false

    override fun getState(): TemplateSettingsService = this

    override fun loadState(state: TemplateSettingsService) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        fun getInstance(): TemplateSettingsService = service()
    }
}
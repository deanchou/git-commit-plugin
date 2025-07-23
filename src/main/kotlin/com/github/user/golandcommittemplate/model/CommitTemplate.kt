package com.github.deanchou.gitcommithelper.model

/**
 * Represents a commit message template.
 *
 * @property name The name of the template.
 * @property content The content of the template with placeholders.
 */
data class CommitTemplate(
    var name: String = "",
    var content: String = ""
)
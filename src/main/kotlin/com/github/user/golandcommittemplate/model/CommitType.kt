package com.github.user.golandcommittemplate.model

/**
 * Represents a commit type with its description and emoji.
 */
data class CommitType(
    val key: String,
    val label: String,
    val description: String,
    val emoji: String? = null
) {
    companion object {
        fun getDefaultTypes(showEmoji: Boolean): List<CommitType> {
            return listOf(
                CommitType("init", "init", "Project initialization", if (showEmoji) "🎉" else null),
                CommitType("feat", "feat", "New feature", if (showEmoji) "✨" else null),
                CommitType("fix", "fix", "Bug fix", if (showEmoji) "🐛" else null),
                CommitType("docs", "docs", "Documentation only changes", if (showEmoji) "📝" else null),
                CommitType("style", "style", "Changes that do not affect the meaning of the code", if (showEmoji) "💄" else null),
                CommitType("refactor", "refactor", "A code change that neither fixes a bug nor adds a feature", if (showEmoji) "♻️" else null),
                CommitType("perf", "perf", "A code change that improves performance", if (showEmoji) "⚡️" else null),
                CommitType("test", "test", "Adding missing tests or correcting existing tests", if (showEmoji) "✅" else null),
                CommitType("build", "build", "Changes that affect the build system or external dependencies", if (showEmoji) "📦" else null),
                CommitType("ci", "ci", "Changes to our CI configuration files and scripts", if (showEmoji) "👷" else null),
                CommitType("chore", "chore", "Other changes that don't modify src or test files", if (showEmoji) "🔧" else null),
                CommitType("revert", "revert", "Reverts a previous commit", if (showEmoji) "⏪" else null)
            )
        }
    }
}
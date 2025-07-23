package com.github.deanchou.gitcommithelper.model

import java.util.*

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
        private fun isChinese(): Boolean {
            val locale = Locale.getDefault()
            return locale.language == "zh" || locale.country == "CN"
        }
        
        fun getDefaultTypes(showEmoji: Boolean): List<CommitType> {
            val isChineseLocale = isChinese()
            return listOf(
                CommitType("init", "init", if (isChineseLocale) "项目初始化" else "Project initialization", if (showEmoji) "🎉" else null),
                CommitType("feat", "feat", if (isChineseLocale) "新加特性" else "New feature", if (showEmoji) "✨" else null),
                CommitType("fix", "fix", if (isChineseLocale) "修复bug" else "Bug fix", if (showEmoji) "🐞" else null),
                CommitType("docs", "docs", if (isChineseLocale) "仅仅修改文档" else "Documentation only changes", if (showEmoji) "📃" else null),
                CommitType("style", "style", if (isChineseLocale) "仅仅修改了空格、格式缩进、逗号等等，不改变代码逻辑" else "Changes that do not affect the meaning of the code", if (showEmoji) "🌈" else null),
                CommitType("refactor", "refactor", if (isChineseLocale) "代码重构，没有加新功能或者修复bug" else "A code change that neither fixes a bug nor adds a feature", if (showEmoji) "🦄" else null),
                CommitType("perf", "perf", if (isChineseLocale) "优化相关，比如提升性能、体验" else "A code change that improves performance", if (showEmoji) "🎈" else null),
                CommitType("test", "test", if (isChineseLocale) "增加测试用例" else "Adding missing tests or correcting existing tests", if (showEmoji) "🧪" else null),
                CommitType("build", "build", if (isChineseLocale) "依赖相关的内容" else "Changes that affect the build system or external dependencies", if (showEmoji) "🔧" else null),
                CommitType("ci", "ci", if (isChineseLocale) "ci配置相关 例如对 k8s，docker的配置文件的修改" else "Changes to our CI configuration files and scripts", if (showEmoji) "🐎" else null),
                CommitType("chore", "chore", if (isChineseLocale) "改变构建流程、或者增加依赖库、工具等" else "Other changes that don't modify src or test files", if (showEmoji) "🐳" else null),
                CommitType("revert", "revert", if (isChineseLocale) "回滚到上一个版本" else "Reverts a previous commit", if (showEmoji) "↩" else null)
            )
        }
    }
}
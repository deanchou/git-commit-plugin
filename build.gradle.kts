plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.20"
    id("org.jetbrains.intellij") version "1.17.0"
}

group = "com.github.user"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

// Configure Gradle IntelliJ Plugin
intellij {
    // 配置为GoLand
    type.set("GO")
    version.set("2023.3.8") // 使用2023.3版本，与代码更兼容
    
    // 添加Go插件依赖和Git4Idea插件依赖
    plugins.set(listOf("org.jetbrains.plugins.go", "Git4Idea"))
    
    // 构建版本设置
    updateSinceUntilBuild.set(true)
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }
    
    patchPluginXml {
        sinceBuild.set("242")
        untilBuild.set("251.*")
        changeNotes.set("""
          Initial version of Git Commit Plugin
        """.trimIndent())
    }
}
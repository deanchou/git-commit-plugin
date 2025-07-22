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
    // Configure for GoLand
    type.set("GO")
    version.set("2023.3.8") // Use 2023.3 version for better code compatibility
    
    // Add Go plugin and Git4Idea plugin dependencies
    plugins.set(listOf("org.jetbrains.plugins.go", "Git4Idea"))
    
    // Build version settings
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
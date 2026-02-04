import java.util.*

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.1.20"
    id("org.jetbrains.intellij.platform") version "2.10.2"
}

group = "cc.ayakurayuki"
version = "1.0.1"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

// Read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin.html
dependencies {
    intellijPlatform {
        intellijIdea("2025.2.4")
        testFramework(org.jetbrains.intellij.platform.gradle.TestFrameworkType.Platform)

        bundledPlugin("com.intellij.java")
        zipSigner()
    }
}

val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) {
        file.inputStream().use { load(it) }
    }
}

intellijPlatform {
    pluginConfiguration {
        id = "${group}.${rootProject.name}"
        name = "Nginx Configuration Formatter"
        version = "1.0.0"

        ideaVersion {
            sinceBuild = "252.25557"
        }

        changeNotes = """
            This is the first release.
        """.trimIndent()
    }

    signing {
        val certFilepath = System.getenv("JETBRAINS_SIGNING_CERT_FILE")
            ?: System.getProperty("signing.certFilePath")
            ?: project.findProperty("signing.certFilePath")?.toString()
            ?: localProperties.getProperty("signing.certFilePath")
            ?: ""

        val privateKeyFilepath = System.getenv("JETBRAINS_SIGNING_PRIVATE_KEY_FILE")
            ?: System.getProperty("signing.privateKeyFilePath")
            ?: project.findProperty("signing.privateKeyFilePath")?.toString()
            ?: localProperties.getProperty("signing.privateKeyFilePath")
            ?: ""

        val passphrase = System.getenv("JETBRAINS_SIGNING_PASSPHRASE")
            ?: System.getProperty("signing.passphrase")
            ?: project.findProperty("signing.passphrase")?.toString()
            ?: localProperties.getProperty("signing.passphrase")
            ?: ""

        if (certFilepath.isNotEmpty() && privateKeyFilepath.isNotEmpty() && passphrase.isNotEmpty()) {
            certificateChainFile.set(file(certFilepath))
            privateKeyFile.set(file(privateKeyFilepath))
            password.set(passphrase)
        }
    }

    publishing {
        val publishingToken = System.getenv("JETBRAINS_SIGNING_TOKEN")
            ?: System.getProperty("publishing.token")
            ?: project.findProperty("publishing.token")?.toString()
            ?: localProperties.getProperty("publishing.token")
            ?: ""

        if (publishingToken.isNotEmpty()) {
            token.set(publishingToken)
        }
    }
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "21"
        targetCompatibility = "21"
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
    }
}

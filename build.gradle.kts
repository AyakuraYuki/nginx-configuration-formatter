plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.1.20"
    id("org.jetbrains.intellij.platform") version "2.10.2"
}

group = "cc.ayakurayuki"
version = "1.0.0"

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
        instrumentationTools()
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
        certificateChainFile.set(file(providers.environmentVariable("JETBRAINS_PLUGIN_PUBLISHER_CERTIFICATE_CHAIN_FILE").get()))
        privateKeyFile.set(file(providers.environmentVariable("JETBRAINS_PLUGIN_PUBLISHER_PRIVATE_KEY_FILE").get()))
        password.set(providers.environmentVariable("JETBRAINS_PLUGIN_PUBLISHER_PRIVATE_KEY_PASSWORD"))
    }

    publishing {
        token.set(providers.environmentVariable("JETBRAINS_PLUGIN_PUBLISHER_TOKEN"))
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

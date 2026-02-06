import org.jetbrains.intellij.platform.gradle.TestFrameworkType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.*

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.1.20"
    id("org.jetbrains.intellij.platform") version "2.10.2"
}

fun prop(name: String) = project.findProperty(name).toString()

val localProperties = Properties().apply {
    project.file("local.properties")?.inputStream()?.use { load(it) }
}


group = prop("group")
version = prop("version")

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

        bundledPlugin("com.intellij.java")
        pluginVerifier()
        zipSigner()

        testFramework(TestFrameworkType.Platform)
    }
}

intellijPlatform {
    pluginConfiguration {
        id = "${group}.${project.name}"
        name = prop("plugin.name")
        version = prop("version")
        changeNotes.set(readChanges("CHANGES.md"))

        ideaVersion {
            sinceBuild.set(prop("plugin.platform.since"))
            untilBuild.set(provider { null })
        }
    }

    pluginVerification {
        ides {
            recommended()
        }
    }

    signing {
        val certFilepath = localProperties.getProperty("signing.certFilePath") ?: ""
        val privateKeyFilepath = localProperties.getProperty("signing.privateKeyFilePath") ?: ""
        val passphrase = localProperties.getProperty("signing.passphrase") ?: ""
        if (certFilepath.isNotEmpty() && privateKeyFilepath.isNotEmpty() && passphrase.isNotEmpty()) {
            certificateChainFile.set(file(certFilepath))
            privateKeyFile.set(file(privateKeyFilepath))
            password.set(passphrase)
        }
    }

    publishing {
        val publishingToken = localProperties.getProperty("publishing.token") ?: ""
        if (publishingToken.isNotEmpty()) {
            token.set(publishingToken)
        }
    }
}

fun readChanges(pathname: String): String {
    val lines = file(pathname).readLines()
    val notes: MutableList<MutableList<String>> = mutableListOf()
    var note: MutableList<String>? = null

    for (line in lines) {
        if (line.startsWith('#')) {
            if (notes.size == 3) {
                break
            }
            note = mutableListOf()
            notes.add(note)
            val header = line.trimStart('#')
            note.add("<b>$header</b>")
        } else if (line.isNotBlank()) {
            note?.add(line)
        }
    }

    return notes.joinToString(
        "</p><br><p>",
        prefix = "<p>",
        postfix = "</p><br>"
    ) { it.joinToString("<br>") } +
            "See the full change notes on the <a href='" +
            prop("repository") +
            "/blob/master/CHANGES.md'>github</a>"
}

tasks {
    prop("jvmVersion").let {
        withType<JavaCompile> {
            sourceCompatibility = it
            targetCompatibility = it
        }
        withType<KotlinCompile> {
            compilerOptions.jvmTarget.set(JvmTarget.fromTarget(it))
        }
    }

    wrapper {
        distributionType = Wrapper.DistributionType.ALL
        gradleVersion = prop("gradleVersion")
    }

    test {
        useJUnit()
        maxHeapSize = "1G"
    }
}

plugins {
    `maven-publish`
    id("hytale-mod") version "0.+"
    id("com.gradleup.shadow") version "9.0.0-rc3"
}

group = "net.cfh.vault"
val vuApiVersion: String = "${findProperty("plugin_version")}"
val vuRelVersion: String = ".0"
version = vuApiVersion.plus(vuRelVersion)
description = "VaultUnlocked is a Chat, Permissions & Economy API to allow plugins to more easily" +
        " hook into these systems without needing to hook each individual system themselves."
val vuWebsite: String = "https://cfh.dev"
val javaVersion = 25

val appData = System.getenv("APPDATA") ?: (System.getenv("HOME") + "/.var/app/com.hypixel.HytaleLauncher/data")
val hytaleAssets = file("$appData/Hytale/install/release/package/game/latest/Assets.zip")


repositories {
    mavenCentral()
    maven("https://maven.hytale-modding.info/releases") {
        name = "HytaleModdingReleases"
    }
    maven("https://repo.codemc.io/repository/creatorfromhell/")
}

dependencies {
    compileOnly(libs.jetbrains.annotations)
    compileOnly(libs.jspecify)

    if (hytaleAssets.exists()) {
        compileOnly(files(hytaleAssets))
    } else {
        // Optional: Print a warning so you know why it's missing
        logger.warn("Hytale Assets.zip not found at: ${hytaleAssets.absolutePath}")
    }
    shadow(libs.vault.unlocked.api)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(javaVersion)
    }

    withSourcesJar()
}

tasks.named<ProcessResources>("processResources") {
    var replaceProperties = mapOf(
        "plugin_group" to findProperty("plugin_group"),
        "plugin_maven_group" to project.group,
        "plugin_name" to project.name,
        "plugin_version" to findProperty("plugin_version"),
        "server_version" to findProperty("server_version"),

        "plugin_description" to findProperty("plugin_description"),
        "plugin_website" to findProperty("plugin_website"),

        "plugin_main_entrypoint" to findProperty("plugin_main_entrypoint"),
        "plugin_author" to findProperty("plugin_author")
    )

    filesMatching("manifest.json") {
        expand(replaceProperties)
    }

    inputs.properties(replaceProperties)
}

hytale {

}

tasks.withType<Jar> {
    manifest {
        attributes["Specification-Title"] = rootProject.name
        attributes["Specification-Version"] = version
        attributes["Implementation-Title"] = project.name
        attributes["Implementation-Version"] =
            providers.environmentVariable("COMMIT_SHA_SHORT")
                .map { "${version}-${it}" }
                .getOrElse(version.toString())
    }
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["shadow"])

        pom {
            name = project.name
            description = project.description
            url = vuWebsite
            organization {
                name = "The New Economy"
                url = "https://tnemc.net"
            }
            licenses {
                license {
                    name = "GNU Lesser General Public License, Version 3 (LGPL-3.0)"
                    url = "https://github.com/TheNewEconomy/VaultUnlocked-Hytale/blob/master/license.txt"
                }
            }
            developers {
                developer {
                    id = "creatorfromhell"
                    name = "Daniel \"creatorfromhell\" Vidmar"
                    email = "daniel.viddy@gmail.com"
                    url = "https://cfh.dev"
                    organization = "The New Economy"
                    organizationUrl = "https://tnemc.net"
                    timezone = "America/New_York"
                    roles.add("Developer")
                }
            }
            scm {
                connection = "scm:git:git://github.com/TheNewEconomy/VaultUnlocked-Hytale.git"
                developerConnection = connection
                url = "https://github.com/TheNewEconomy/VaultUnlocked-Hytale/"
            }
            issueManagement {
                system = "GitHub"
                url = "https://github.com/TheNewEconomy/VaultUnlocked-Hytale/issues"
            }
        }
    }

    repositories {
        maven {
            name = "CodeMC"
            url = uri("https://repo.codemc.io/repository/creatorfromhell/")

            val mavenUsername = System.getenv("GRADLE_PROJECT_MAVEN_USERNAME")
            val mavenPassword = System.getenv("GRADLE_PROJECT_MAVEN_PASSWORD")
            if (mavenUsername != null && mavenPassword != null) {
                credentials {
                    username = mavenUsername
                    password = mavenPassword
                }
            }
        }
    }
}

// IDEA no longer automatically downloads sources/javadoc jars for dependencies, so we need to explicitly enable the behavior.
idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}

val syncAssets = tasks.register<Copy>("syncAssets") {
    group = "hytale"
    description = "Automatically syncs assets from Build back to Source after server stops."

    // Take from the temporary build folder (Where the game saved changes)
    from(layout.buildDirectory.dir("resources/main"))

    // Copy into your actual project source (Where your code lives)
    into("src/main/resources")

    // IMPORTANT: Protect the manifest template from being overwritten
    exclude("manifest.json")

    // If a file exists, overwrite it with the new version from the game
    duplicatesStrategy = DuplicatesStrategy.INCLUDE

    doLast {
        println("✅ Assets successfully synced from Game to Source Code!")
    }
}

tasks {
    compileJava {
        sourceCompatibility = "25"
        targetCompatibility = "25"
    }

    jar {
        dependsOn(shadowJar)
        archiveFileName = "original-VaultUnlocked-Hytale-${version}.jar"
    }

    shadowJar {
        archiveFileName = "VaultUnlocked-Hytale-${version}.jar"
        archiveClassifier = ""

        configurations = listOf(project.configurations.shadow.get())
    }
    processResources {
        filesMatching("**/resources/*") {
            expand(rootProject.project.properties)
        }

        outputs.upToDateWhen { false }
    }
}

afterEvaluate {
    // Now Gradle will find it, because the plugin has finished working
    val targetTask = tasks.findByName("runServer") ?: tasks.findByName("server")

    if (targetTask != null) {
        targetTask.finalizedBy(syncAssets)
        logger.lifecycle("✅ specific task '${targetTask.name}' hooked for auto-sync.")
    } else {
        logger.warn("⚠️ Could not find 'runServer' or 'server' task to hook auto-sync into.")
    }
}

plugins {
    `maven-publish`
    id("hytale-mod") version "0.+"
    id("com.gradleup.shadow") version "9.0.0-rc3"
}

group = "net.cfh.vault"
val vuApiVersion: String = "${findProperty("plugin_version")}"
val vuRelVersion: String = ".3"
version = vuApiVersion.plus(vuRelVersion)
description = "VaultUnlocked is a Chat, Permissions & Economy API to allow plugins to more easily" +
        " hook into these systems without needing to hook each individual system themselves."
val vuWebsite: String = "https://cfh.dev"
val javaVersion = 25


repositories {
    mavenCentral()
    maven("https://maven.hytale-modding.info/releases") {
        name = "HytaleModdingReleases"
    }
    maven("https://repo.codemc.io/repository/creatorfromhell/") {
        name = "VaultUnlocked"
    }
    maven("https://nexus.lucko.me/repository/maven-hytale/") {
        name = "lucko"
    }
}

dependencies {
    compileOnly(libs.jetbrains.annotations)
    compileOnly(libs.jspecify)
    compileOnly("com.hypixel.hytale:HytaleServer:2026.01.17-4b0f30090-20260119.081336-1")
    shadow(libs.vault.unlocked.api)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(javaVersion)
    }

    withSourcesJar()
}

tasks.named<ProcessResources>("processResources") {
    val replaceProperties = mapOf(
        "plugin_group" to findProperty("plugin_group"),
        "plugin_maven_group" to project.group,
        "plugin_name" to project.name,
        "plugin_version" to version,
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
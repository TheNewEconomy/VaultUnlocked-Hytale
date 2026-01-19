plugins {
    `maven-publish`
    id("hytale-mod") version "0.+"
    id("com.gradleup.shadow") version "9.0.0-rc3"
}

group = "net.cfh.vault"
val vuApiVersion: String = "${findProperty("plugin_version")}"
val vuRelVersion: String = ".2"
version = vuApiVersion.plus(vuRelVersion)
description = "VaultUnlocked is a Chat, Permissions & Economy API to allow plugins to more easily" +
        " hook into these systems without needing to hook each individual system themselves."
val vuWebsite: String = "https://cfh.dev"
val javaVersion = 25
val moduleName by extra("dev.faststats.hytale")

val libsDir: Directory = layout.projectDirectory.dir("libs")
val hytaleServerJar: RegularFile = libsDir.file("HytaleServer.jar")
val credentialsFile: RegularFile = layout.projectDirectory.file(".hytale-downloader-credentials.json")
val downloadDir: Provider<Directory> = layout.buildDirectory.dir("download")
val hytaleZip: Provider<RegularFile> = downloadDir.map { it.file("hytale.zip") }


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
    compileOnly(files(hytaleServerJar))
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

tasks.register("download-server") {
    group = "hytale"

    doLast {
        if (hytaleServerJar.asFile.exists()) {
            println("HytaleServer.jar already exists, skipping download")
            return@doLast
        }

        val downloaderZip: Provider<RegularFile> = downloadDir.map { it.file("hytale-downloader.zip") }

        libsDir.asFile.mkdirs()
        downloadDir.get().asFile.mkdirs()

        val os = org.gradle.internal.os.OperatingSystem.current()
        val downloaderExecutable = when {
            os.isLinux -> downloadDir.map { it.file("hytale-downloader-linux-amd64") }
            os.isWindows -> downloadDir.map { it.file("hytale-downloader-windows-amd64.exe") }
            else -> throw GradleException("Unsupported operating system: ${os.name}")
        }

        if (!downloaderExecutable.get().asFile.exists()) {
            if (!downloaderZip.get().asFile.exists()) ant.invokeMethod(
                "get", mapOf(
                    "src" to "https://downloader.hytale.com/hytale-downloader.zip",
                    "dest" to downloaderZip.get().asFile.absolutePath
                )
            ) else {
                println("hytale-downloader.zip already exists, skipping download")
            }

            copy {
                from(zipTree(downloaderZip))
                include(downloaderExecutable.get().asFile.name)
                into(downloadDir)
            }
        } else {
            println("Hytale downloader binary already exists, skipping download and extraction")
        }

        if (downloaderZip.get().asFile.delete()) {
            println("Deleted hytale-downloader.zip after extracting binaries")
        }

        downloaderExecutable.get().asFile.setExecutable(true)

        if (!hytaleZip.get().asFile.exists()) {
            val credentials = System.getenv("HYTALE_DOWNLOADER_CREDENTIALS")
            if (!credentials.isNullOrBlank()) {
                if (!credentialsFile.asFile.exists()) {
                    credentialsFile.asFile.writeText(credentials)
                    println("Hytale downloader credentials written from environment variable to ${credentialsFile.asFile.absolutePath}")
                } else {
                    println("Using existing credentials file at ${credentialsFile.asFile.absolutePath}")
                }
            }

            val processBuilder = ProcessBuilder(
                downloaderExecutable.get().asFile.absolutePath,
                "-download-path",
                "hytale",
                "-credentials-path",
                credentialsFile.asFile.absolutePath
            )
            processBuilder.directory(downloadDir.get().asFile)
            processBuilder.redirectErrorStream(true)
            val process = processBuilder.start()

            process.inputStream.bufferedReader().use { reader ->
                reader.lines().forEach { line ->
                    println(line)
                }
            }

            val exitCode = process.waitFor()
            if (exitCode != 0) {
                throw GradleException("Hytale downloader failed with exit code: $exitCode")
            }
        } else {
            println("hytale.zip already exists, skipping download")
        }

        if (hytaleZip.get().asFile.exists()) {
            val serverDir = downloadDir.map { it.dir("Server") }
            copy {
                from(zipTree(hytaleZip))
                include("Server/HytaleServer.jar")
                into(downloadDir)
            }

            val extractedJar = serverDir.map { it.file("HytaleServer.jar") }
            if (extractedJar.get().asFile.exists()) {
                extractedJar.get().asFile.copyTo(hytaleServerJar.asFile, overwrite = true)
                serverDir.get().asFile.deleteRecursively()
            } else {
                throw GradleException("HytaleServer.jar was not found in Server/ subdirectory")
            }

            if (!hytaleServerJar.asFile.exists()) {
                throw GradleException("HytaleServer.jar was not found in hytale.zip")
            }

            hytaleZip.get().asFile.delete()
            println("Deleted hytale.zip after extracting HytaleServer.jar")
        } else {
            throw GradleException(
                "hytale.zip not found at ${hytaleZip.get().asFile.absolutePath}. " +
                        "The downloader may not have completed successfully."
            )
        }
    }
}

tasks.register("update-server") {
    group = "hytale"
    hytaleServerJar.asFile.delete()
    hytaleZip.get().asFile.delete()
    dependsOn(tasks.named("download-server"))
}

tasks {
    compileJava {
        sourceCompatibility = "25"
        targetCompatibility = "25"
        dependsOn("download-server")
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

import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.net.URL

plugins {
    `java-library`
    `maven-publish`
    id("xyz.jpenilla.run-paper") version "2.3.0"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
}

dependencies {
    compileOnly("com.alttd:Galaxy-API:1.21-R0.1-SNAPSHOT") {
        isChanging = true
    }
    compileOnly("com.alttd:AltitudeAPI:0.0.2")
    compileOnly("com.github.decentsoftware-eu:decentholograms:2.8.9")
    compileOnly("org.jetbrains:annotations:16.0.2")
    testImplementation("org.powermock:powermock-module-junit4:1.7.4")
    testImplementation("org.powermock:powermock-api-mockito2:1.7.4")
}

group = "com.alttd.altitudetag"
version = System.getenv("BUILD_NUMBER") ?: gitCommit()
description = "AltitudeTag"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}


publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }

    configure<PublishingExtension> {
        repositories {
            maven {
                name = "maven"
                url = uri("https://repo.destro.xyz/snapshots/")
                credentials(PasswordCredentials::class)
            }
        }
    }
}

tasks {
    withType<JavaCompile> {
        options.encoding = Charsets.UTF_8.name()
    }

    withType<Javadoc> {
        options.encoding = Charsets.UTF_8.name()
    }

    jar {
        archiveFileName.set("${rootProject.name}.jar")
    }

    runServer {
        val dir = File(System.getProperty("user.home") + "/share/devserver/")
        if (!dir.parentFile.exists()) {
            dir.parentFile.mkdirs()
        }
        runDirectory.set(dir)

        val fileName = "/galaxy.jar"
        val file = File(dir.path + fileName)

        if (!file.parentFile.exists()) {
            file.parentFile.mkdirs()
        }
        if (!file.exists()) {
            download("https://repo.destro.xyz/private/com/alttd/Galaxy-Server/Galaxy-paperclip-1.20.4-R0.1-SNAPSHOT-reobf.jar", file)
        }
        serverJar(file)
        minecraftVersion("1.20.4")
    }
}

fun download(link: String, path: File) {
    URL(link).openStream().use { input ->
        FileOutputStream(path).use { output ->
            input.copyTo(output)
        }
    }
}

fun gitCommit(): String {
    val os = ByteArrayOutputStream()
    project.exec {
        commandLine = "git rev-parse --short HEAD".split(" ")
        standardOutput = os
    }
    return String(os.toByteArray()).trim()
}

bukkit {
    name = rootProject.name
    main = "$group.${rootProject.name}"
    version = "${rootProject.version}"
    apiVersion = "1.21"
    authors = listOf("Michael Ziluck", "destro174")
    depend = listOf("AltitudeAPI", "DecentHolograms")
}
plugins {
    `java-library`
    `maven-publish`
}

dependencies {
    compileOnly("com.alttd:Galaxy-API:1.20.1-R0.1-SNAPSHOT") {
        isChanging = true
    }
    compileOnly("com.alttd:AltitudeAPI:LATEST")
    compileOnly("me.filoghost.holographicdisplays:holographicdisplays-api:3.0.0")
    compileOnly("org.jetbrains:annotations:16.0.2")
    testImplementation("org.powermock:powermock-module-junit4:1.7.4")
    testImplementation("org.powermock:powermock-api-mockito2:1.7.4")
}

group = "com.alttd"
version = "1.0.6"
description = "AltitudeTag"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
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
}
rootProject.name = "AltitudeTag"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven("https://repo.destro.xyz/snapshots") // Altitude - Galaxy
        maven("https://repo.codemc.io/repository/maven-public/")
        maven("https://repo.maven.apache.org/maven2/")
    }
}

pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

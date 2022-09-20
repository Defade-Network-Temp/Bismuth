plugins {
    id("java")
    id("maven-publish")
}

group = "net.defade.bismuth"
version = "1.1.0"

subprojects {
    apply(plugin = "java")
    apply(plugin = "maven-publish")

    group = rootProject.group
    version = rootProject.version

    java {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        withSourcesJar()
        withJavadocJar()
    }

    configure<PublishingExtension> {
        publications.create<MavenPublication>("maven") {
            from(components["java"])
        }
    }

    publishing {
        repositories {
            maven("https://repo.defade.net/defade") {
                name = "defade"
                credentials(PasswordCredentials::class)
            }
        }
    }

    repositories {
        mavenCentral()
    }
}
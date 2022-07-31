plugins {
    id("java")
}

group = "fr.defade"
version = "1.0-SNAPSHOT"

subprojects {
    apply(plugin = "java")
    apply(plugin = "maven-publish")

    configure<PublishingExtension> {
        publications.create<MavenPublication>("maven") {
            from(components["java"])
        }
    }

    repositories {
        mavenCentral()
    }
}
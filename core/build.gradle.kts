plugins {
    id("java-library")
}

group = "fr.defade.bismuth"
version = "1.0-SNAPSHOT"

dependencies {
    api("org.apache.logging.log4j:log4j-api:2.17.2")
    api("io.netty:netty-all:4.1.79.Final")
}
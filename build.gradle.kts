plugins {
    kotlin("jvm") version "1.9.23"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven {
        name = "GithubZenohKotlin"
        url = uri("https://maven.pkg.github.com/eclipse-zenoh/zenoh-kotlin")
        credentials {
            username = providers.gradleProperty("user").get()
            password = providers.gradleProperty("token").get()
        }
    }
}

dependencies {
    implementation("io.zenoh:zenoh-kotlin-jvm:0.10.1-rc")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1-Beta")
    implementation("org.apache.httpcomponents.client5:httpclient5:5.3.1")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
plugins {
    kotlin("jvm") version "1.9.21"
}

group = "com.github.Shahondin1624"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

sourceSets {
    main {
        kotlin.srcDir("kotlin")
    }
}
dependencies {
    implementation("ch.qos.logback:logback-classic:1.2.9")
    implementation("io.github.microutils:kotlin-logging-jvm:2.0.11")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
tasks {
    wrapper {
        gradleVersion = "8.5"
    }
}
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath(libs.binary.compatibility.validator)
    }
}

apply(plugin = "binary-compatibility-validator")

plugins {
    `kotlin-dsl`
    // `java-gradle-plugin`
    // `maven-publish`
    id("com.gradle.plugin-publish") version "1.2.1"
    // signing
}

version = "2.3.0"
group = "io.github.klememi.multiplatform-swiftpackage"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(11)
}

dependencies {
    compileOnly(kotlin("gradle-plugin", libs.versions.kotlin.get()))
    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.kotest.property)
    testImplementation(libs.mockk)
    testImplementation(kotlin("gradle-plugin", libs.versions.kotlin.get()))
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11

    withJavadocJar()
    withSourcesJar()
}

tasks.withType<Test> {
    useJUnitPlatform()
}

project.tasks.named("processResources", Copy::class.java) {
    // https://github.com/gradle/gradle/issues/17236
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

extensions.findByName("buildScan")?.withGroovyBuilder {
    setProperty("termsOfServiceUrl", "https://gradle.com/terms-of-service")
    setProperty("termsOfServiceAgree", "yes")
}

gradlePlugin {
    website = "https://github.com/klememi/multiplatform-swiftpackage"
    vcsUrl = "https://github.com/klememi/multiplatform-swiftpackage"
    plugins {
        create("pluginMaven") {
            id = "io.github.klememi.multiplatform-swiftpackage"
            displayName = "KMP Swift Package" 
            description = "Swift Package creating for KMP" 
            tags = listOf("kmp", "swift", "spm") 
            implementationClass = "com.chromaticnoise.multiplatformswiftpackage.MultiplatformSwiftPackagePlugin"
        }
    }
}

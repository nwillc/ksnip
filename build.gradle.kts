import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val assertjVersion = "3.15.0"
val jacksonVersion = "2.11.0"
val jacocoToolVersion: String by project
val slf4jkextVersion = "1.1.1"
val mainClassName = "com.github.nwillc.ksnip.SnippetsApp"
val slf4jApiVersion = "1.7.25"
val spek2Version = "2.0.10"
val tinyLogVersion = "1.3.6"
val tornadofxVersion = "1.7.20"

plugins {
    jacoco
    kotlin("jvm") version "1.3.72"
    kotlin("plugin.serialization") version "1.3.72"
    id("com.github.nwillc.vplugin") version "3.0.5"
    id("org.jetbrains.dokka") version "0.10.1"
    id("io.gitlab.arturbosch.detekt") version "1.8.0"
    id("org.jlleitschuh.gradle.ktlint") version "9.2.1"
}

group = "com.github.nwillc"
version = "1.3.2"

logger.lifecycle("${project.group}.${project.name}@${project.version}")

repositories {
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.20.0")
    implementation("org.slf4j:slf4j-api:$slf4jApiVersion")
    implementation("no.tornado:tornadofx:$tornadofxVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("org.slf4j:jul-to-slf4j:$slf4jApiVersion")
    implementation("$group:slf4jkext:$slf4jkextVersion")

    runtimeOnly("org.tinylog:slf4j-binding:$tinyLogVersion")

    testImplementation("org.assertj:assertj-core:$assertjVersion")
    testImplementation("org.spekframework.spek2:spek-dsl-jvm:$spek2Version")
    testImplementation("org.junit.jupiter:junit-jupiter:5.6.1")

    testRuntimeOnly("org.spekframework.spek2:spek-runner-junit5:$spek2Version")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.1")
}

detekt {
}

jacoco {
    toolVersion = jacocoToolVersion
}

tasks {
    named<Jar>("jar") {
        with(manifest) {
            attributes["Main-Class"] = mainClassName
            attributes["Automatic-Module-Name"] = "${project.group}.${project.name}"
            attributes["Implementation-Version"] = project.version
        }
        from(Callable { configurations["runtimeClasspath"].map { if (it.isDirectory) it else zipTree(it) } })
    }
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
    withType<Test> {
        useJUnitPlatform {
            includeEngines = mutableSetOf("spek2","junit-jupiter")
        }
        testLogging {
            showStandardStreams = true
            events("passed", "failed", "skipped")
        }
    }
    withType<DokkaTask> {
        outputFormat = "html"
        outputDirectory = "$buildDir/dokka"
    }
    register<Copy>("prepApp") {
        dependsOn("assemble")
        from("src/main/app")
        from("build/libs/${project.name}-${project.version}.jar")
        into("build/release")
    }
    register<Exec>("osxApp") {
        dependsOn("prepApp")
        commandLine = listOf(
            "javapackager",
            "-deploy",
            "-native", "image",
            "-srcdir", "$buildDir/release",
            "-srcfiles", "${project.name}-${project.version}.jar",
            "-outdir", "$buildDir/release",
            "-outfile", "${project.name}.app",
            "-appclass", mainClassName,
            "-name", project.name, "-BappVersion=${project.version}", "-Bicon=$buildDir/release/${project.name}.icns",
            "-title", project.name,
            "-nosign"
        )
    }
}

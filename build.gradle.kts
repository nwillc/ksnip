import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val assertjVersion = "3.12.1"
val jacksonVersion = "2.9.8"
val mainClassName = "com.github.nwillc.ksnip.SnippetsApp"
val slf4jApiVersion = "1.7.25"
val spek2Version = "2.0.1"
val tinyLogVersion = "1.3.6"
val tornadofxVersion = "1.7.18"

plugins {
    kotlin("jvm") version "1.3.21"
    id("com.github.nwillc.vplugin") version "2.3.0"
    id("org.jetbrains.dokka") version "0.9.17"
    id("io.gitlab.arturbosch.detekt") version "1.0.0.RC9.2"
    id("org.jlleitschuh.gradle.ktlint") version "7.1.0"
}

group = "com.github.nwillc"
version = "1.3.1-SNAPSHOT"

logger.lifecycle("${project.group}.${project.name}@${project.version}")

repositories {
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation("org.slf4j:slf4j-api:$slf4jApiVersion")
    implementation("no.tornado:tornadofx:$tornadofxVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("org.slf4j:jul-to-slf4j:$slf4jApiVersion")

    runtime("org.tinylog:slf4j-binding:$tinyLogVersion")

    testImplementation("org.assertj:assertj-core:$assertjVersion")
    testImplementation("org.spekframework.spek2:spek-dsl-jvm:$spek2Version")

    testRuntime("org.spekframework.spek2:spek-runner-junit5:$spek2Version")
}

detekt {
    input = files("src/main/kotlin")
    filters = ".*/build/.*"
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
            includeEngines = mutableSetOf("spek2")
        }
        testLogging.showStandardStreams = true
        beforeTest(KotlinClosure1<TestDescriptor, Unit>({ logger.lifecycle("\t${this.name}") }))
        afterSuite(KotlinClosure2<TestDescriptor, TestResult, Unit>({ descriptor, result ->
            if (descriptor.parent == null) {
                logger.lifecycle("Tests run: ${result.testCount}, Failures: ${result.failedTestCount}, Skipped: ${result.skippedTestCount}")
            }
            Unit
        }))
    }
    withType<DokkaTask> {
        outputFormat = "html"
        includeNonPublic = false
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

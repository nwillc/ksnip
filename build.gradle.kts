/*
 * Copyright 2020 nwillc@gmail.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any purpose with or
 * without fee is hereby granted, provided that the above copyright notice and this
 * permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT,
 * OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE,
 * DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS
 * ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    jacoco
    Dependencies.plugins.forEach { (n, v) -> id(n) version v }
}

group = Constants.group
version = Constants.version

logger.lifecycle("${project.group}.${project.name}@${project.version}")

repositories {
    jcenter()
}

dependencies {
    Dependencies.artifacts(
        "org.jetbrains.kotlin:kotlin-stdlib-jdk8",
        "org.jetbrains.kotlinx:kotlinx-serialization-runtime",
        "org.jetbrains.kotlin:kotlin-reflect",
        "no.tornado:tornadofx",
        "org.slf4j:slf4j-api",
        "org.slf4j:jul-to-slf4j",
        "org.springframework.boot:spring-boot-starter",
        "${Constants.group}:slf4jkext"
    ) { implementation(it) }

    Dependencies.artifacts(
        "org.assertj:assertj-core",
        "org.junit.jupiter:junit-jupiter",
        "org.spekframework.spek2:spek-dsl-jvm",
        "org.spekframework.spek2:spek-runner-junit5"
    ) { testImplementation(it) }

    Dependencies.artifacts(
        "org.junit.jupiter:junit-jupiter-engine"
    ) { testRuntimeOnly(it) }
}

detekt {
}

ktlint {
    version.set(ToolVersions.ktlint)
    disabledRules.set(setOf("import-ordering", "no-wildcard-imports"))
}

jacoco {
    toolVersion = ToolVersions.jacoco
}

javafx {
    version = "11.0.2"
    modules = arrayListOf("javafx.controls", "javafx.graphics", "javafx.fxml")
}

tasks {
    named<Jar>("jar") {
        with(manifest) {
            attributes["Main-Class"] = Constants.mainClassName
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
            includeEngines = mutableSetOf("spek2", "junit-jupiter")
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
            "-appclass", Constants.mainClassName,
            "-name", project.name, "-BappVersion=${project.version}", "-Bicon=$buildDir/release/${project.name}.icns",
            "-title", project.name,
            "-nosign"
        )
    }
}

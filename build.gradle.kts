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
import org.springframework.boot.gradle.tasks.bundling.BootJar

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
    version = ToolVersions.jfx
    modules = arrayListOf("javafx.controls", "javafx.graphics", "javafx.fxml")
}

tasks {
    named<BootJar>("bootJar") {
        with(manifest) {
            attributes["Automatic-Module-Name"] = "${project.group}.${project.name}"
            attributes["Implementation-Version"] = project.version
        }
    }
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
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
}

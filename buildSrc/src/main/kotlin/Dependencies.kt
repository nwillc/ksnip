/*
 * Copyright (c) 2020, nwillc@gmail.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 *
 */

object Constants {
    const val group = "com.github.nwillc"
    const val version = "1.4.0"
}

object PluginVersions {
    const val detekt = "1.9.1"
    const val dokka = "0.10.1"
    const val kotlin = "1.3.72"
    const val ktlint = "9.2.1"
    const val openFX = "0.0.8"
    const val springBoot = "2.2.5.RELEASE"
    const val springDeps = "1.0.9.RELEASE"
    const val vplugin = "3.0.5"
}

object ToolVersions {
    const val jacoco = "0.8.3"
    const val jfx = "14.0.1"
    const val ktlint = "0.37.0"
}

object Versions {
    const val assertJ = "3.16.1"
    const val slf4j = "1.7.25"
    const val slf4jKExt = "1.1.2"
    const val tornadoFX = "1.7.20"
    const val jupiter = "5.7.0-M1"
    const val serializationRuntime = "0.20.0"
    const val tinyLog = "1.3.6"
    const val spek2 = "2.0.11"
}

object Dependencies {
    val plugins = mapOf(
        "org.jetbrains.kotlin.jvm" to PluginVersions.kotlin,
        "org.jetbrains.kotlin.plugin.serialization" to PluginVersions.kotlin,
        "org.jetbrains.kotlin.plugin.spring" to PluginVersions.kotlin,
        "org.jetbrains.dokka" to PluginVersions.dokka,
        "org.jlleitschuh.gradle.ktlint" to PluginVersions.ktlint,
        "com.github.nwillc.vplugin" to PluginVersions.vplugin,
        "io.gitlab.arturbosch.detekt" to PluginVersions.detekt,
        "org.openjfx.javafxplugin" to PluginVersions.openFX,
        "io.spring.dependency-management" to PluginVersions.springDeps,
        "org.springframework.boot" to PluginVersions.springBoot
    )
    val artifacts = mapOf(
        "org.jetbrains.kotlin:kotlin-stdlib-jdk8" to PluginVersions.kotlin,
        "org.jetbrains.kotlin:kotlin-reflect" to PluginVersions.kotlin,
        "org.jetbrains.kotlin:kotlin-plugin.spring" to PluginVersions.kotlin,
        "org.jetbrains.kotlinx:kotlinx-serialization-runtime" to Versions.serializationRuntime,
        "org.junit.jupiter:junit-jupiter" to Versions.jupiter,
        "org.junit.jupiter:junit-jupiter-engine" to Versions.jupiter,
        "no.tornado:tornadofx" to Versions.tornadoFX,
        "org.slf4j:slf4j-api" to Versions.slf4j,
        "org.slf4j:jul-to-slf4j" to Versions.slf4j,
        "org.assertj:assertj-core" to Versions.assertJ,
        "${Constants.group}:slf4jkext" to Versions.slf4jKExt,
        "org.tinylog:slf4j-binding" to Versions.tinyLog,
        "org.spekframework.spek2:spek-dsl-jvm" to Versions.spek2,
        "org.spekframework.spek2:spek-runner-junit5" to Versions.spek2,
        "org.springframework.boot:spring-boot-starter" to ""
    )

    fun plugins(vararg keys: String, block: (Pair<String, String>) -> Unit) =
        keys
            .map { it to (plugins[it] ?: error("No plugin $it registered in Dependencies.")) }
            .forEach {
                block(it)
            }

    fun artifacts(vararg keys: String, block: (String) -> Unit) =
        keys
            .map { it to (artifacts[it] ?: error("No artifact $it registered in Dependencies.")) }
            .forEach { (n, v) ->
                if (v.isNotEmpty())  block("$n:$v") else block(n)
            }
}

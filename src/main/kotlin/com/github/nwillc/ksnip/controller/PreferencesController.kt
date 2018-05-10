/*
 * Copyright (c) 2018, ADP Inc. All Rights Reserved. This property belongs to ADP Inc. Copying in any form is prohibited.
 */

package com.github.nwillc.ksnip.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.github.nwillc.ksnip.model.Preferences
import tornadofx.*
import java.io.File


class PreferencesController : Controller() {
    companion object {
        val prefFile = File(System.getProperty("user.home"), ".snippets.json")
    }

    val mapper = ObjectMapper().registerModule(KotlinModule())
    var preferences = Preferences()

    init {
        if (prefFile.canRead()) {
            preferences = mapper.readValue<Preferences>(prefFile, Preferences::class.java)
        }
    }

    fun savePreferences() {
        prefFile.delete()
        prefFile.createNewFile()
        val outputStream = prefFile.outputStream();
        mapper.writeValue(outputStream, preferences)
    }
}
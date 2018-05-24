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
        @JvmField
        val PREF_FILE = File(System.getProperty("user.home"), ".snippets.json")
    }

    val mapper = ObjectMapper().registerModule(KotlinModule())
    var preferences = Preferences()

    init {
        if (PREF_FILE.canRead()) {
            preferences = mapper.readValue<Preferences>(PREF_FILE, Preferences::class.java)
        }
    }

    fun savePreferences() {
        PREF_FILE.delete()
        PREF_FILE.createNewFile()
        val outputStream = PREF_FILE.outputStream();
        mapper.writeValue(outputStream, preferences)
    }
}
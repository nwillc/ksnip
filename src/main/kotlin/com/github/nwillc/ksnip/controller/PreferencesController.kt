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

package com.github.nwillc.ksnip.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.github.nwillc.ksnip.model.Preferences
import tornadofx.Controller

import java.io.File

/**
 * Controller regarding [Preferences].
 * @property preferences the application preferences.
 */
class PreferencesController : Controller() {
    companion object {
        private val prefFile = File(System.getProperty("user.home"), ".snippets.json")
    }

    private val mapper = ObjectMapper().registerModule(KotlinModule())
    var preferences = Preferences()

    init {
        if (prefFile.canRead()) {
            preferences = mapper.readValue<Preferences>(prefFile, Preferences::class.java)
        }
    }

    /**
     * Save the application preferences.
     */
    fun savePreferences() {
        prefFile.delete()
        prefFile.createNewFile()
        val outputStream = prefFile.outputStream()
        mapper.writeValue(outputStream, preferences)
    }
}

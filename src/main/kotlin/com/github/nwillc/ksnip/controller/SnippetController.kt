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
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.nwillc.ksnip.model.LegacyFile
import com.github.nwillc.ksnip.model.Snippet
import com.github.nwillc.ksnip.view.SnippetsView
import com.github.nwillc.slf4jkext.getLogger
import tornadofx.Controller
import java.io.File

private val LOGGER = getLogger<SnippetController>()

/**
 * Application controller for operations regarding [Snippet]s.
 * @property snippets the working list of snippets.
 */
class SnippetController : Controller() {
    private val snippetView: SnippetsView by inject()
    private val mapper = ObjectMapper().registerModule(KotlinModule())
    private val preferencesController: PreferencesController by inject()
    var snippets: MutableList<Snippet> = mutableListOf()

    init {
        if (preferencesController.preferences.defaultFile.isNotEmpty()) {
            val arrayOfSnippets =
                mapper.readValue<MutableList<Snippet>>(File(preferencesController.preferences.defaultFile))
            snippets = arrayOfSnippets
        }
    }

    /**
     * Add a new [Snippet] to the [snippets].
     * @param category the category of the new snippet.
     * @param title the title of the new snippet.
     * @param body the body of the new snippet.
     */
    fun addSnippet(category: String, title: String, body: String) {
        val snippet = Snippet()
        snippet.category = category
        snippet.title = title
        snippet.body = body

        snippets.add(snippet)
        snippetView.workingSet = snippets
        snippetView.refreshCategories()
    }

    /**
     * Import a JSON file containing an array of snippets.
     * @param file the file containing snippets.
     */
    fun import(file: File) {
        val arrayOfSnippets = mapper.readValue<MutableList<Snippet>>(file)
        snippets = arrayOfSnippets
        snippetView.workingSet = snippets
        snippetView.refreshCategories()
    }

    /**
     * Import a [LegacyFile] of snippets.
     * @param file the legacy format file.
     */
    fun importLagacy(file: File) {

        val importFile = mapper.readValue<LegacyFile>(file)

        LOGGER.info("Importing file: $importFile")

        importFile.snippets.forEach { s ->
            val snippet = Snippet()
            snippet.title = s.title
            snippet.body = s.body

            val category = importFile.categories.filter { c -> c.key.equals(s.category) }.first()
            snippet.category = category.name
            snippets.add(snippet)
        }
        snippetView.workingSet = snippets
        snippetView.refreshCategories()
    }

    /**
     * Save an array of [Snippet]s to a file.
     * @param file the file to save to.
     */
    fun saveAs(file: File) {
        mapper.writeValue(file.outputStream(), snippets)
    }
}

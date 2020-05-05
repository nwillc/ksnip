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

import com.github.nwillc.ksnip.model.LegacyFile
import com.github.nwillc.ksnip.model.Snippet
import com.github.nwillc.ksnip.readSnippets
import com.github.nwillc.ksnip.view.SnippetsView
import com.github.nwillc.ksnip.writeSnippets
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
    private val preferencesController: PreferencesController by inject()
    var snippets: MutableList<Snippet> = mutableListOf()

    init {
        if (preferencesController.preferences.defaultFile.isNotEmpty()) {
            snippets = File(preferencesController.preferences.defaultFile).readSnippets().toMutableList()
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
        snippets = file.readSnippets().toMutableList()
        snippetView.workingSet = snippets
        snippetView.refreshCategories()
    }

    /**
     * Save an array of [Snippet]s to a file.
     * @param file the file to save to.
     */
    fun saveAs(file: File) {
        file.writeSnippets(snippets)
    }
}

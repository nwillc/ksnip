/*
 * Copyright 2018 nwillc@gmail.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted, provided that the above copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.github.nwillc.ksnip.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.nwillc.ksnip.model.ImportFile
import com.github.nwillc.ksnip.model.Snippet
import com.github.nwillc.ksnip.view.SnippetsView
import tornadofx.*
import java.io.File

class SnippetController : Controller() {
    val snippets = ArrayList<Snippet>()
    val snippetView: SnippetsView by inject()

    fun addSnippet(categoryName: String, title: String, body: String) {
        val snippet = Snippet()
        snippet.category = categoryName
        snippet.title = title
        snippet.body = body

        snippets.add(snippet)
        snippetView.refreshCategories(snippets)
    }

    fun importFile(file: File) {
        val mapper = ObjectMapper().registerModule(KotlinModule())
        val importFile = mapper.readValue<ImportFile>(file)

        println(importFile)

        importFile.snippets.forEach {
            val snippet = Snippet()
            snippet.title = it.title
            snippet.body = it.body

            val category = importFile.categories.filter { c -> c.key.equals(it.category) }.first()
            snippet.category = category.name
            snippets.add(snippet)
        }

        snippetView.refreshCategories(snippets)
    }
}
/*
 * Copyright 2018 nwillc@gmail.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted, provided that the above copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.github.nwillc.ksnip.view

import com.github.nwillc.ksnip.controller.SnippetController
import com.github.nwillc.ksnip.model.Snippet
import javafx.scene.control.ChoiceBox
import javafx.scene.control.ListView
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.layout.VBox
import javafx.stage.FileChooser
import tornadofx.*

class SnippetsView : View() {
    companion object {
        @JvmField
        val JSON_FILTER = arrayOf(FileChooser.ExtensionFilter("JSON file", "*.json"))
    }

    // UI Elements
    override val root: VBox by fxml("/views/Snippets.fxml")
    val categories: ListView<String> by fxid()
    val titles: ListView<String> by fxid()
    val text: TextArea by fxid()
    val searchText: TextField by fxid()
    val categoryList: ChoiceBox<String> by fxid()
    val snippetCategory: TextField by fxid()
    val snippetTitle: TextField by fxid()
    val snippetBody: TextArea by fxid()

    // Controllers
    val snippetController: SnippetController by inject()

    init {
        refreshCategories(snippetController.snippets)
    }

    fun refreshCategories(snippets: List<Snippet>) {
        categories.items.clear()
        titles.items.clear()
        text.text = ""

        snippets.map { it.category }
                .toSet()
                .sorted()
                .forEach { categories.items.add(it) }
    }

    fun exit() {
        System.exit(0)
    }

    fun tabChanged() {
        categoryList.items.clear()

        snippetController.snippets.map { it.category }
                .toSet()
                .sorted()
                .forEach { }
        println("plink")
    }

    fun refreshTitles() {
        val selectedCategory: String? = categories.selectedItem
        titles.items.clear()
        text.text = ""
        snippetController.snippets
                .asSequence()
                .filter { it.category.equals(selectedCategory) }
                .forEach { titles.items.add(it.title) }
    }

    fun refreshText() {
        val selectedCategory: String? = categories.selectedItem
        val selectedTitle: String? = titles.selectedItem
        snippetController.snippets
                .asSequence()
                .filter { it.category.equals(selectedCategory) }
                .filter { it.title.equals(selectedTitle) }
                .forEach { text.text = it.body }
    }

    fun saveSnippet() {
        snippetController.addSnippet(snippetCategory.text, snippetTitle.text, snippetBody.text)
        snippetCategory.text = ""
        snippetTitle.text = ""
        snippetBody.text = ""
    }

    fun search() {
        println("search ${searchText.text}")
    }

    fun saveAs() {
        val list = chooseFile("Save As", JSON_FILTER, FileChooserMode.Save)
        snippetController.saveAs(list[0])
    }

    fun openNew() {
        val list = chooseFile("Import New", JSON_FILTER, FileChooserMode.Single)
        snippetController.importNew(list[0])
    }

    fun openOld() {
        val list = chooseFile("Import Old", JSON_FILTER, FileChooserMode.Single)
        snippetController.importOld(list[0])
    }
}


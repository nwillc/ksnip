/*
 * Copyright 2018 nwillc@gmail.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted, provided that the above copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.github.nwillc.ksnip.view

import com.github.nwillc.ksnip.controller.BrowseController
import com.github.nwillc.ksnip.controller.CategoryController
import com.github.nwillc.ksnip.controller.SnippetController
import com.github.nwillc.ksnip.dao.CategoryDao
import com.github.nwillc.ksnip.dao.SnippetDao
import javafx.scene.control.*
import javafx.scene.layout.VBox
import javafx.stage.FileChooser
import tornadofx.*

class SnippetsView : View() {
    // UI Elements
    override val root: VBox by fxml("/views/Snippets.fxml")
    val categories: ListView<String> by fxid()
    val titles: ListView<String> by fxid()
    val text: TextArea by fxid()
    val categoryName: TextField by fxid()

    val snippetCategory: ChoiceBox<String> by fxid()
    val snippetTitle: TextField by fxid()
    val snippetBody: TextArea by fxid()

    // Controllers
    val categoryController: CategoryController by inject()
    val snippetController: SnippetController by inject()
    val browseController: BrowseController by inject()

    init {
        refreshCategories()
    }

    fun refreshCategories() {
        categories.items.clear()
        snippetCategory.items.clear()
        CategoryDao.findAll()
                .sorted()
                .forEach({ c ->
                    categories.items.add(c.name)
                    snippetCategory.items.add(c.name)
                })
        titles.items.clear()
        text.text = ""
    }

    fun exit() {
        System.exit(0)
    }

    fun refreshTitles() {
        val selectedItem: String? = categories.selectedItem
        titles.items.clear()
        SnippetDao.findAll()
                .sorted()
                .filter({ s -> s.category.equals(selectedItem) })
                .forEach({ s -> titles.items.add(s.title) })
        text.text = ""
    }

    fun refreshText() {
        val selectedCategory: String? = categories.selectedItem
        val selectedTitle: String? = titles.selectedItem
        SnippetDao.findAll()
                .filter({ s -> s.category.equals(selectedCategory) })
                .filter({ s -> s.title.equals(selectedTitle) })
                .forEach({ s -> text.text = s.body })
    }

    fun saveCategory() {
        categoryController.addCategory(categoryName.text)
        categoryName.text = ""
    }

    fun saveSnippet() {
        snippetController.addSnippet(snippetCategory.value, snippetTitle.text, snippetBody.text)
        snippetTitle.text = ""
        snippetBody.text = ""
    }

    fun openImport() {
        val file = chooseFile("Import File", arrayOf(FileChooser.ExtensionFilter("JSON File", "*.json")), FileChooserMode.Single)
        println("File $file")
    }
}


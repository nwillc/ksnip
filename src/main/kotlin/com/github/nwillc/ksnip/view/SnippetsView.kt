/*
 * Copyright 2018 nwillc@gmail.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted, provided that the above copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.github.nwillc.ksnip.view

import com.github.nwillc.ksnip.controller.PreferencesController
import com.github.nwillc.ksnip.controller.SnippetController
import com.github.nwillc.ksnip.model.Snippet
import javafx.embed.swing.SwingFXUtils
import javafx.event.ActionEvent
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.layout.VBox
import javafx.stage.FileChooser
import tornadofx.*
import java.io.File

class SnippetsView : View() {
    companion object {
        @JvmField
        val JSON_FILTER = arrayOf(FileChooser.ExtensionFilter("JSON file", "*.json"))
    }

    // UI Elements
    override val root: VBox by fxml("/views/snippets.fxml")
    val categories: ListView<String> by fxid()
    val titles: ListView<String> by fxid()
    val text: TextArea by fxid()
    val searchText: TextField by fxid()
    val categoryList: ChoiceBox<String> by fxid()
    val snippetCategory: TextField by fxid()
    val snippetTitle: TextField by fxid()
    val snippetBody: TextArea by fxid()
    val menuBar: MenuBar by fxid()

    // Controllers
    val snippetController: SnippetController by inject()
    val preferencesController: PreferencesController by inject()

    // Other Views
    val preferencesView: PreferencesView by inject()

    var workingSet = emptyList<Snippet>()

    init {
        osxConfig()
        workingSet = snippetController.snippets
        refreshCategories()
        // Scene Builder lacks this event type asignment ?!
        categoryList.addEventHandler(ActionEvent.ACTION, { categorySelect() })
    }

    fun osxConfig() {
        val os = System.getProperty("os.name", "UNKNOWN")
        if (!os.equals("Mac OS X")) {
            println("Skipping OS X tailoring for: $os")
            return
        }

        menuBar.isUseSystemMenuBar = true
        // Hacky OS X dock icon assignment
        val asStream = javaClass.classLoader.getResourceAsStream("icon.png")
        val image = Image(asStream)
        val bufferedImage = SwingFXUtils.fromFXImage(image, null)
        com.apple.eawt.Application.getApplication().dockIconImage = bufferedImage

    }

    fun refreshCategories() {
        categories.items.clear()
        titles.items.clear()
        text.text = ""

        workingSet.map { it.category }
                .toSet()
                .sorted()
                .forEach { categories.items.add(it) }
    }

    fun minimize() {
        primaryStage.isIconified = true
    }

    fun exit() {
        System.exit(0)
    }

    fun find() {
        searchText.requestFocus()
        searchText.selectAll()
    }

    fun tabChanged() {
        categoryList.items.clear()

        snippetController.snippets.map { it.category }
                .toSet()
                .sorted()
                .forEach { categoryList.items.add(it) }
    }

    fun refreshTitles() {
        val selectedCategory: String? = categories.selectedItem
        titles.items.clear()
        text.text = ""
        workingSet
                .asSequence()
                .filter { it.category.equals(selectedCategory) }
                .sorted()
                .forEach { titles.items.add(it.title) }
    }

    fun refreshText() {
        val selectedCategory: String? = categories.selectedItem
        val selectedTitle: String? = titles.selectedItem
        text.text = workingSet
                .asSequence()
                .filter { it.category.equals(selectedCategory) }
                .filter { it.title.equals(selectedTitle) }
                .first()
                .body
    }

    fun updateSnippet() {
        val selectedCategory: String? = categories.selectedItem
        val selectedTitle: String? = titles.selectedItem
        workingSet
                .asSequence()
                .filter { it.category.equals(selectedCategory) }
                .filter { it.title.equals(selectedTitle) }
                .forEach { it.body = text.text }
    }

    fun deleteSnippet() {
        val selectedCategory: String? = categories.selectedItem
        val selectedTitle: String? = titles.selectedItem

        println("delete")

        val snippet = snippetController.snippets.find {
            it.category.equals(selectedCategory) &&
                    it.title.equals(selectedTitle)
        }
        snippetController.snippets.remove(snippet)
        search()
    }

    fun saveSnippet() {
        snippetController.addSnippet(snippetCategory.text, snippetTitle.text, snippetBody.text)
        snippetCategory.text = ""
        snippetTitle.text = ""
        snippetBody.text = ""
    }

    fun search() {
        val search = searchText.text
        workingSet = if (search.isEmpty()) {
            snippetController.snippets
        } else {
            snippetController.snippets
                    .filter { it.title.contains(searchText.text) || it.body.contains(searchText.text) }
        }
        refreshCategories()
    }

    fun openPreferences() {
        preferencesView.openModal()
    }

    fun save() {
        snippetController.saveAs(File(preferencesController.preferences.defaultFile))
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

    fun categorySelect() {
        snippetCategory.text = categoryList.value
    }
}


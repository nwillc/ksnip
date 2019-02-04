/*
 * Copyright 2019 nwillc@gmail.com
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

package com.github.nwillc.ksnip.view

import com.github.nwillc.ksnip.controller.PreferencesController
import com.github.nwillc.ksnip.controller.SnippetController
import com.github.nwillc.ksnip.model.Snippet
import javafx.embed.swing.SwingFXUtils
import javafx.event.ActionEvent
import javafx.scene.control.ChoiceBox
import javafx.scene.control.ListView
import javafx.scene.control.MenuBar
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.image.Image
import javafx.scene.layout.VBox
import javafx.stage.FileChooser
import tornadofx.FileChooserMode
import tornadofx.View
import tornadofx.chooseFile
import tornadofx.selectedItem
import java.io.File

/**
 * The view managing the snippets UI.
 * @property workingSet the set of snippets being displayed.
 * @property root the root UI view.
 */
class SnippetsView : View() {
    companion object {
        /**
         * File type filter for open panels.
         */
        val JSON_FILTER = arrayOf(FileChooser.ExtensionFilter("JSON file", "*.json"))
    }

    // UI Elements
    private val categories: ListView<String> by fxid()
    private val titles: ListView<String> by fxid()
    private val text: TextArea by fxid()
    private val searchText: TextField by fxid()
    private val categoryList: ChoiceBox<String> by fxid()
    private val snippetCategory: TextField by fxid()
    private val snippetTitle: TextField by fxid()
    private val snippetBody: TextArea by fxid()
    private val menuBar: MenuBar by fxid()

    // Controllers
    private val snippetController: SnippetController by inject()
    private val preferencesController: PreferencesController by inject()

    // Other Views
    private val preferencesView: PreferencesView by inject()

    var workingSet = emptyList<Snippet>()

    override val root: VBox by fxml("/views/snippets.fxml")

    init {
        osxConfig()
        workingSet = snippetController.snippets
        refreshCategories()
        // Scene Builder lacks this event type asignment ?!
        categoryList.addEventHandler(ActionEvent.ACTION, { categorySelect() })
    }

    private fun osxConfig() {
        val os = System.getProperty("os.name", "UNKNOWN")
        if (!os.equals("Mac OS X")) {
            return
        }

        menuBar.isUseSystemMenuBar = true
        // Hacky OS X dock icon assignment, done with reflection so it can build with any JDK but run on OS X.
        val asStream = javaClass.classLoader.getResourceAsStream("icon.png")
        val image = Image(asStream)
        val bufferedImage = SwingFXUtils.fromFXImage(image, null)
        val applicationClass = Class.forName("com.apple.eawt.Application")
        val method = applicationClass.getMethod("getApplication")
        val application = method.invoke(applicationClass)
        val setDockImage = application.javaClass.getMethod("setDockIconImage", java.awt.Image::class.java)
        setDockImage.invoke(application, bufferedImage)
    }

    /**
     * Refresh the categories displayed based on the [workingSet].
     */
    fun refreshCategories() {
        categories.items.clear()
        titles.items.clear()
        text.text = ""

        workingSet.map { it.category }
            .toSet()
            .sorted()
            .forEach { categories.items.add(it) }
    }

    /**
     * Minimize the UI.
     */
    fun minimize() {
        primaryStage.isIconified = true
    }

    /**
     * Exit the application.
     */
    fun exit() {
        System.exit(0)
    }

    /**
     * Focus on the find field.
     */
    fun find() {
        searchText.requestFocus()
        searchText.selectAll()
    }

    /**
     * Note when a tab is changed.
     */
    fun tabChanged() {
        categoryList.items.clear()

        snippetController.snippets.map { it.category }
            .toSet()
            .sorted()
            .forEach { categoryList.items.add(it) }
    }

    /**
     * Refresh the titles list.
     */
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

    /**
     * Refresh the snippet text.
     */
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

    /**
     * Update the snippet with the current values.
     */
    fun updateSnippet() {
        val selectedCategory: String? = categories.selectedItem
        val selectedTitle: String? = titles.selectedItem
        workingSet
            .asSequence()
            .filter { it.category.equals(selectedCategory) }
            .filter { it.title.equals(selectedTitle) }
            .forEach { it.body = text.text }
    }

    /**
     * Delete the current snippet.
     */
    fun deleteSnippet() {
        val selectedCategory: String? = categories.selectedItem
        val selectedTitle: String? = titles.selectedItem

        val snippet = snippetController.snippets.find {
            it.category.equals(selectedCategory) &&
                    it.title.equals(selectedTitle)
        }
        snippetController.snippets.remove(snippet)
        search()
    }

    /**
     * Save the current snippet.
     */
    fun saveSnippet() {
        snippetController.addSnippet(snippetCategory.text, snippetTitle.text, snippetBody.text)
        snippetCategory.text = ""
        snippetTitle.text = ""
        snippetBody.text = ""
    }

    /**
     * Search snippets titles and text.
     */
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

    /**
     * Open the preferences panel.
     */
    fun openPreferences() {
        preferencesView.openModal()
    }

    /**
     * Save the snippets to the default file.
     */
    fun save() {
        snippetController.saveAs(File(preferencesController.preferences.defaultFile))
    }

    /**
     * Save the snippets to another file.
     */
    fun saveAs() {
        val list = chooseFile("Save As", JSON_FILTER, FileChooserMode.Save)
        snippetController.saveAs(list[0])
    }

    /**
     * Open a new style file.
     */
    fun openNew() {
        val list = chooseFile("Import New", JSON_FILTER, FileChooserMode.Single)
        snippetController.import(list[0])
    }

    /**
     * Open a legacy file.
     */
    fun openOld() {
        val list = chooseFile("Import Old", JSON_FILTER, FileChooserMode.Single)
        snippetController.importLagacy(list[0])
    }

    /**
     * Note selected category.
     */
    fun categorySelect() {
        snippetCategory.text = categoryList.value
    }
}

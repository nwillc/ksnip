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

package com.github.nwillc.ksnip.view

import com.github.nwillc.ksnip.TornadoFxApplication
import com.github.nwillc.ksnip.controller.PreferencesController
import com.github.nwillc.ksnip.controller.SnippetController
import com.github.nwillc.ksnip.model.Snippet
import javafx.event.ActionEvent
import javafx.scene.control.*
import javafx.scene.layout.VBox
import javafx.stage.FileChooser
import tornadofx.FileChooserMode
import tornadofx.View
import tornadofx.chooseFile
import tornadofx.selectedItem
import java.io.File
import kotlin.system.exitProcess

/**
 * The view managing the snippets UI.
 * @property workingSet the set of snippets being displayed.
 * @property root the root UI view.
 */
@SuppressWarnings("TooManyFunctions")
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

    // Controllers
    private val snippetController: SnippetController by inject()
    private val preferencesController: PreferencesController by inject()

    // Other Views
    private val preferencesView: PreferencesView by inject()

    var workingSet = emptyList<Snippet>()

    override val root: VBox by fxml("/views/snippets.fxml")

    init {
        workingSet = snippetController.snippets
        refreshCategories()
        // Scene Builder lacks this event type assignment ?!
        categoryList.addEventHandler(ActionEvent.ACTION, { categorySelect() })
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
    fun exit(): Nothing =
        exitProcess(0)

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
        save()
    }

    /**
     * Delete the current snippet.
     */
    fun deleteSnippet() {
        val selectedCategory: String? = categories.selectedItem
        val selectedTitle: String? = titles.selectedItem

        val snippet = snippetController.snippets.find {
            it.category.equals(selectedCategory) && it.title.equals(selectedTitle)
        }
        snippetController.snippets.remove(snippet)
        search()
        save()
    }

    /**
     * Save the current snippet.
     */
    fun saveSnippet() {
        snippetController.addSnippet(snippetCategory.text, snippetTitle.text, snippetBody.text)
        snippetCategory.text = ""
        snippetTitle.text = ""
        snippetBody.text = ""
        save()
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
    fun openPreferences() =
        preferencesView.openModal()

    fun openAbout() {
        println("about")
        val alert = Alert(Alert.AlertType.INFORMATION)
        alert.title = "About KSnips"
        val version = TornadoFxApplication::class.java.`package`!!.implementationVersion ?: ""
        alert.headerText = "About KSnips"
        alert.contentText = "Version: $version"
        alert.show()
    }

    /**
     * Save the snippets to the default file.
     */
    fun save() =
        snippetController.saveAs(File(preferencesController.preferences.defaultFile))

    /**
     * Save the snippets to another file.
     */
    fun saveAs() = chooseFile(title = "Save As", filters = JSON_FILTER, mode = FileChooserMode.Save).let {
        if (it.isNotEmpty()) snippetController.saveAs(it[0])
    }

    /**
     * Open a new style file.
     */
    fun openNew() = chooseFile(title = "Import New", filters = JSON_FILTER, mode = FileChooserMode.Single).let {
        if (it.isNotEmpty()) snippetController.import(it[0])
    }

    /**
     * Note selected category.
     */
    fun categorySelect() {
        snippetCategory.text = categoryList.value
    }
}

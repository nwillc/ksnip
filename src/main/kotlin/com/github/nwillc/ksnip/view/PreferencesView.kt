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

import com.github.nwillc.ksnip.controller.PreferencesController
import javafx.scene.control.TextField
import javafx.scene.layout.VBox
import tornadofx.FileChooserMode
import tornadofx.View
import tornadofx.chooseFile
import java.io.File

/**
 * A view managing the application preferences panel.
 * @property root the UIs view.
 */
class PreferencesView : View() {
    private val preferencesController: PreferencesController by inject()
    private val defaultFile: TextField by fxid()

    override val root: VBox by fxml("/views/preferences.fxml")

    init {
        defaultFile.text = preferencesController.preferences.defaultFile
    }

    /**
     * Set the defualt file name.
     */
    fun setDefaultFile() {
        val list: List<File> = chooseFile(title = "Default File", filters = SnippetsView.JSON_FILTER, mode = FileChooserMode.Single)
        defaultFile.text = list[0].path
    }

    /**
     * Persist the appication preferences.
     */
    fun savePreferences() {
        preferencesController.preferences.defaultFile = defaultFile.text
        preferencesController.savePreferences()
        close()
    }
}

/*
 * Copyright (c) 2018, ADP Inc. All Rights Reserved. This property belongs to ADP Inc. Copying in any form is prohibited.
 */

package com.github.nwillc.ksnip.view

import com.github.nwillc.ksnip.controller.PreferencesController
import javafx.scene.control.TextField
import javafx.scene.layout.VBox
import tornadofx.FileChooserMode
import tornadofx.View
import tornadofx.chooseFile

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
        val list = chooseFile("Default File", SnippetsView.JSON_FILTER, FileChooserMode.Single)
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
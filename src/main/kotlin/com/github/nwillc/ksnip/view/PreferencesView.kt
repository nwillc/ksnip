/*
 * Copyright (c) 2018, ADP Inc. All Rights Reserved. This property belongs to ADP Inc. Copying in any form is prohibited.
 */

package com.github.nwillc.ksnip.view

import com.github.nwillc.ksnip.controller.PreferencesController
import com.github.nwillc.ksnip.model.Preferences
import javafx.scene.control.TextField
import javafx.scene.layout.VBox
import tornadofx.FileChooserMode
import tornadofx.VBoxConstraint
import tornadofx.View
import tornadofx.chooseFile




class PreferencesView : View() {
    override val root: VBox by fxml("/views/preferences.fxml")
    val defaultFile: TextField by fxid()
    val preferencesController: PreferencesController by inject()

    init {
        defaultFile.text = preferencesController.preferences.defaultFile
    }

    fun setDefaultFile() {
        val list = chooseFile("Default File", SnippetsView.JSON_FILTER, FileChooserMode.Single)
        defaultFile.text = list[0].path
    }

    fun savePreferences() {
        preferencesController.preferences.defaultFile = defaultFile.text
        preferencesController.savePreferences()
        close()
    }
}
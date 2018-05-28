/*
 * Copyright (c) 2018, ADP Inc. All Rights Reserved. This property belongs to ADP Inc. Copying in any form is prohibited.
 */

package com.github.nwillc.ksnip.model

/**
 * The preferences of the application. The [defaultFile] is the file the application will open on start.
 */
data class Preferences(var defaultFile: String = "")
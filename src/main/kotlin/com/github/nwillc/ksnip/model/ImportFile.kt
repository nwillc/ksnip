/*
 * Copyright 2018 nwillc@gmail.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted, provided that the above copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.github.nwillc.ksnip.model

import java.util.*

class ImportFile {
    var categories: Array<ImportCategory> = emptyArray()
    var snippets: Array<ImportSnippet> = emptyArray()
    override fun toString(): String {
        return "ImportFile(categories=${Arrays.toString(categories)}, snippets=${Arrays.toString(snippets)})"
    }
}

data class ImportCategory(
    var key: String = "",
    var name: String = ""
)

data class ImportSnippet(
    var key: String = "",
    var category: String = "",
    var title: String = "",
    var body: String = ""
)
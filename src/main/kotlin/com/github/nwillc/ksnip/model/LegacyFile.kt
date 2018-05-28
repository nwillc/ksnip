/*
 * Copyright 2018 nwillc@gmail.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted, provided that the above copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.github.nwillc.ksnip.model

/**
 * Data class supporting legacy snippet JSON persistence file format.
 * @property categories an Array of legacy Categories.
 * @property snippets an Array of legacy snippets.
 */
data class LegacyFile(
    var categories: Array<LegacyCategory> = emptyArray(),
    var snippets: Array<LagacySnippet> = emptyArray()
)

/**
 * Data class supporting legacy Category format.
 * @property key the unique identifier of the category.
 * @property name the category name.
 */
data class LegacyCategory(
    var key: String = "",
    var name: String = ""
)

/**
 * Data class supporting the legacy snippet format.
 * @property key the unique identifier of the snippet.
 * @property category the unique identifier of the category of the snippet.
 * @property title the snippet's title.
 * @property body the snippet's body.
 */
data class LagacySnippet(
    var key: String = "",
    var category: String = "",
    var title: String = "",
    var body: String = ""
)
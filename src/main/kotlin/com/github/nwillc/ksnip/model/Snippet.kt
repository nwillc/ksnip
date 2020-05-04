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

package com.github.nwillc.ksnip.model

import kotlinx.serialization.Serializable

/**
 * Data class representing a snippet. A snippet is comprised of a category, a title and its body.
 *
 * @property category the category the snippet falls in
 * @property title the title of the snippet
 * @property body the body of the snippet
 */
@Serializable
data class Snippet(
    var category: String = "",
    var title: String = "",
    var body: String = ""
) : Comparable<Snippet> {
    /**
     * Compares one snippet to [other], employing the category and title to compare the two snippets.
     * @return 0 if equal, -1 if less, 1 if greater
     */
    override fun compareTo(other: Snippet): Int {
        val catComp = category.compareTo(other.category, true)
        return if (catComp != 0) catComp else title.compareTo(other.title, true)
    }
}

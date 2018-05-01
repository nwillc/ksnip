/*
 * Copyright 2018 nwillc@gmail.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted, provided that the above copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.github.nwillc.ksnip.model

import com.github.nwillc.opa.HasKey
import java.util.*

class Snippet(key: String) : HasKey<String>(key), Comparable<Snippet> {
    var category = ""
    var title = ""
    var body = ""

    constructor() : this("") {
        key = UUID.randomUUID().toString()
    }

    override fun compareTo(other: Snippet): Int {
        val catComp = category.compareTo(other.category)
        if (catComp != 0) {
            return catComp
        }

        return title.compareTo(other.title)
    }

    override fun toString(): String {
        return "Snippet(category='$category',title='$title',body='$body')"
    }
}

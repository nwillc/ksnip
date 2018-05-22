/*
 * Copyright 2018 nwillc@gmail.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted, provided that the above copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.github.nwillc.ksnip.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class SnippetTest {
    companion object {
        val CATEGORY = "FOO"
        val TITLE = "BAR"
        val BODY = "BAZ"
    }

    private var snippet = Snippet()

    @BeforeEach
    internal fun setUp() {
        snippet = Snippet()
    }

    @Test
    internal fun testDefaultConstructor() {
        assertThat(snippet.category).isBlank()
        assertThat(snippet.title).isBlank()
        assertThat(snippet.body).isBlank()
    }

    @Test
    internal fun testGetSet() {
        snippet.category = CATEGORY
        assertThat(snippet.category).isEqualTo(CATEGORY)
        snippet.title = TITLE
        assertThat(snippet.title).isEqualTo(TITLE)
        snippet.body = BODY
        assertThat(snippet.body).isEqualTo(BODY)
    }
}

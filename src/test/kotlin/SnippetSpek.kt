import com.github.nwillc.ksnip.model.Snippet
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

/*
 * Copyright 2018 nwillc@gmail.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted, provided that the above copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

internal val CATEGORY = "FOO"
internal val TITLE = "BAR"
internal val BODY = "BAZ"

internal class SnippetSpek : Spek({
    given("the model object Snippet") {
        on("instantiation with default constructor") {
            val snippet = Snippet()
            it("should have sane default values") {
                assertThat(snippet.category).isBlank()
                assertThat(snippet.title).isBlank()
                assertThat(snippet.body).isBlank()
            }
            it("getters and setters should work") {
                snippet.category = CATEGORY
                assertThat(snippet.category).isEqualTo(CATEGORY)
                snippet.title = TITLE
                assertThat(snippet.title).isEqualTo(TITLE)
                snippet.body = BODY
                assertThat(snippet.body).isEqualTo(BODY)
            }
        }
    }
    given("two snippets") {
        val snippet1 = Snippet()
        val snippet2 = Snippet()
        on("comparison") {
            it("should ignore the body") {
                snippet1.category = CATEGORY
                snippet2.category = CATEGORY
                snippet1.title = TITLE
                snippet2.title = TITLE
                snippet1.body = BODY
                snippet2.body = BODY + BODY

                assertThat(snippet1.compareTo(snippet2)).isEqualTo(0)
            }
            it("should ignore the case of the category") {
                snippet1.category = CATEGORY
                snippet2.category = CATEGORY.toLowerCase()
                snippet1.title = TITLE
                snippet2.title = TITLE
                snippet1.body = BODY
                snippet2.body = BODY + BODY

                assertThat(snippet1.compareTo(snippet2)).isEqualTo(0)
            }
            it("should ignore the case of the title") {
                snippet1.category = CATEGORY
                snippet2.category = CATEGORY
                snippet1.title = TITLE
                snippet2.title = TITLE.capitalize()
                snippet1.body = BODY
                snippet2.body = BODY + BODY

                assertThat(snippet1.compareTo(snippet2)).isEqualTo(0)
            }
            it("should compare the category") {
                snippet1.category = ""
                snippet2.category = CATEGORY
                snippet1.title = TITLE
                snippet2.title = TITLE
                snippet1.body = BODY
                snippet2.body = BODY + BODY

                assertThat(snippet1.compareTo(snippet2)).isLessThan(0)
                snippet1.category = CATEGORY
                snippet2.category = ""
                assertThat(snippet1.compareTo(snippet2)).isGreaterThan(0)
            }
            it("should ignore the case of the title") {
                snippet1.category = CATEGORY
                snippet2.category = CATEGORY
                snippet1.title = TITLE
                snippet2.title = TITLE.capitalize()
                snippet1.body = BODY
                snippet2.body = BODY + BODY

                assertThat(snippet1.compareTo(snippet2)).isEqualTo(0)
            }
            it("should compare the category") {
                snippet1.category = ""
                snippet2.category = CATEGORY
                snippet1.title = TITLE
                snippet2.title = TITLE
                snippet1.body = BODY
                snippet2.body = BODY + BODY

                assertThat(snippet1.compareTo(snippet2)).isLessThan(0)
                snippet1.category = CATEGORY
                snippet2.category = ""
                assertThat(snippet1.compareTo(snippet2)).isGreaterThan(0)
            }
            it("should compare the title") {
                snippet1.category = CATEGORY
                snippet2.category = CATEGORY
                snippet1.title = ""
                snippet2.title = TITLE
                snippet1.body = BODY
                snippet2.body = BODY + BODY

                assertThat(snippet1.compareTo(snippet2)).isLessThan(0)
                snippet1.title = TITLE
                snippet2.title = ""
                assertThat(snippet1.compareTo(snippet2)).isGreaterThan(0)
            }
        }
    }
})

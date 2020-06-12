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

package com.github.nwillc

import com.github.nwillc.ksnip.model.Snippet
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

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

object SnippetFeature : Spek({
    Feature("Snippet") {
        Scenario("Getter/Setters") {
            lateinit var snippet: Snippet
            Given("a snipppet instantiated with default constructor") {
                snippet = Snippet()
            }
            When("it has default blank values") {
                assertThat(snippet.category).isBlank()
                assertThat(snippet.title).isBlank()
                assertThat(snippet.body).isBlank()
            }
            Then("getter and setters should work") {
                snippet.category = CATEGORY
                assertThat(snippet.category).isEqualTo(CATEGORY)
                snippet.title = TITLE
                assertThat(snippet.title).isEqualTo(TITLE)
                snippet.body = BODY
                assertThat(snippet.body).isEqualTo(BODY)
            }
        }
        Scenario("Comparing Equal Snippets") {
            lateinit var snippet1: Snippet
            lateinit var snippet2: Snippet
            Given("given two snippets") {
                snippet1 = Snippet()
                snippet2 = Snippet()
            }
            When("only their bodies differ") {
                snippet1.category = CATEGORY
                snippet2.category = CATEGORY
                snippet1.title = TITLE
                snippet2.title = TITLE
                snippet1.body = BODY
                snippet2.body = BODY + BODY
            }
            Then("they should be equal") {
                assertThat(snippet1.compareTo(snippet2)).isEqualTo(0)
            }

            Given("given two snippets") {
                snippet1 = Snippet()
                snippet2 = Snippet()
            }
            When("their categories have different case") {
                snippet1.category = CATEGORY
                snippet2.category = CATEGORY.toLowerCase()
                snippet1.title = TITLE
                snippet2.title = TITLE
                snippet1.body = BODY
                snippet2.body = BODY
            }
            Then("they should be equal") {
                assertThat(snippet1.compareTo(snippet2)).isEqualTo(0)
            }

            Given("given two snippets") {
                snippet1 = Snippet()
                snippet2 = Snippet()
            }
            When("their titles have different case") {
                snippet1.category = CATEGORY
                snippet2.category = CATEGORY
                snippet1.title = TITLE
                snippet2.title = TITLE.toLowerCase()
                snippet1.body = BODY
                snippet2.body = BODY
            }
            Then("they should be equal") {
                assertThat(snippet1.compareTo(snippet2)).isEqualTo(0)
            }
        }
        Scenario("Comparing Unequal Snippets") {
            lateinit var snippet1: Snippet
            lateinit var snippet2: Snippet
            Given("given two snippets") {
                snippet1 = Snippet()
                snippet2 = Snippet()
            }
            When("the first categories is lexicographically less") {
                snippet1.category = "a$CATEGORY"
                snippet2.category = CATEGORY
                snippet1.title = TITLE
                snippet2.title = TITLE
                snippet1.body = BODY
                snippet2.body = BODY
            }
            Then("they should compare less than 0") {
                assertThat(snippet1.compareTo(snippet2)).isLessThan(0)
            }

            Given("given two snippets") {
                snippet1 = Snippet()
                snippet2 = Snippet()
            }
            When("the second categories is lexicographically less") {
                snippet1.category = CATEGORY
                snippet2.category = "a$CATEGORY"
                snippet1.title = TITLE
                snippet2.title = TITLE
                snippet1.body = BODY
                snippet2.body = BODY
            }
            Then("they should compare greater than 0") {
                assertThat(snippet1.compareTo(snippet2)).isGreaterThan(0)
            }

            Given("given two snippets") {
                snippet1 = Snippet()
                snippet2 = Snippet()
            }
            When("the first title is lexicographically less") {
                snippet1.category = CATEGORY
                snippet2.category = CATEGORY
                snippet1.title = "a$TITLE"
                snippet2.title = TITLE
                snippet1.body = BODY
                snippet2.body = BODY
            }
            Then("they should compare less than 0") {
                assertThat(snippet1.compareTo(snippet2)).isLessThan(0)
            }

            Given("given two snippets") {
                snippet1 = Snippet()
                snippet2 = Snippet()
            }
            When("the second title is lexicographically less") {
                snippet1.category = CATEGORY
                snippet2.category = CATEGORY
                snippet1.title = TITLE
                snippet2.title = "a$TITLE"
                snippet1.body = BODY
                snippet2.body = BODY
            }
            Then("they should compare greater than 0") {
                assertThat(snippet1.compareTo(snippet2)).isGreaterThan(0)
            }
        }
    }
})

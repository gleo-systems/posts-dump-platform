package pl.gleosys.postsdump.core

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.datatest.withData
import io.kotest.matchers.equals.shouldBeEqual

class StringTest : BehaviorSpec({
    given("single text value and whitespace characters to be removed") {

        `when`("passing null value") {
            then("throw null pointer exception") {
                val text: String? = null
                shouldThrowExactly<java.lang.NullPointerException> {
                    text!!.removeWhitespaceChars()
                }
            }
        }

        `when`("passing value from data set") {
            withData(
                nameFn = { "Then: return '${it.second}' value for text '${it.first}'" },
                listOf(
                    "" to "",
                    "  " to "",
                    "a b" to "ab",
                    " a \n  \n\n\r\r - \t\t \u000c b " to "a-b"
                )
            ) {
                val (text, expected) = it
                text.removeWhitespaceChars() shouldBeEqual expected
            }
        }
    }
})

package pl.gleosys.postsdump.infrastructure

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.datatest.withData
import io.kotest.matchers.equals.shouldBeEqual
import pl.gleosys.postsdump.util.removeWhitespaceChars

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
                    Pair("", ""),
                    Pair("  ", ""),
                    Pair("a b", "ab"),
                    Pair(" a \n  \n\n\r\r - \t\t \u000c b ", "a-b")
                )
            ) {
                val (text, expected) = it
                text.removeWhitespaceChars() shouldBeEqual expected
            }
        }
    }
})

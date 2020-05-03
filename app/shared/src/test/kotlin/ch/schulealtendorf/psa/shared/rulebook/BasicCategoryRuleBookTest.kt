package ch.schulealtendorf.psa.shared.rulebook

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("unit-test")
internal class BasicCategoryRuleBookTest {
    private val ruleBook = BasicCategoryRuleBook()

    @Nested
    internal inner class TargetThrowing {

        @Test
        internal fun ageUnder12() {
            val categoryModel = CategoryModel(11, "Ballzielwurf")

            val distance = ruleBook.run(categoryModel)

            assertThat(distance).isEqualTo("4m")
        }

        @Test
        internal fun ageAbove12() {
            val categoryModel = CategoryModel(12, "Ballzielwurf")

            val distance = ruleBook.run(categoryModel)

            assertThat(distance).isEqualTo("5m")
        }
    }

    @Nested
    internal inner class BasketThrowing {

        @Test
        internal fun ageUnder12() {
            val categoryModel = CategoryModel(11, "Korbeinwurf")

            val distance = ruleBook.run(categoryModel)

            assertThat(distance).isEqualTo("2m")
        }

        @Test
        internal fun ageAbove12() {
            val categoryModel = CategoryModel(12, "Korbeinwurf")

            val distance = ruleBook.run(categoryModel)

            assertThat(distance).isEqualTo("2.5m")
        }
    }

    @Test
    internal fun ageUnder12() {
        val ballThrowing = CategoryModel(12, "Korbeinwurf")
        val skipping = CategoryModel(12, "Seilspringen")

        ruleBook.run(ballThrowing)

        val distance = ruleBook.run(skipping)

        assertThat(distance).isNull()
    }
}

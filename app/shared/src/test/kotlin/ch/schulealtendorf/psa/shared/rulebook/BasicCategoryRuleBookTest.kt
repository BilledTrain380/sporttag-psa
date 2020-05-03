package ch.schulealtendorf.psa.shared.rulebook

import ch.schulealtendorf.psa.dto.participation.athletics.BALLZIELWURF
import ch.schulealtendorf.psa.dto.participation.athletics.KORBEINWURF
import ch.schulealtendorf.psa.dto.participation.athletics.SEILSPRINGEN
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
            val categoryModel = CategoryModel(11, BALLZIELWURF)

            val distance = ruleBook.run(categoryModel)

            assertThat(distance).isEqualTo("4m")
        }

        @Test
        internal fun ageAbove12() {
            val categoryModel = CategoryModel(12, BALLZIELWURF)

            val distance = ruleBook.run(categoryModel)

            assertThat(distance).isEqualTo("5m")
        }
    }

    @Nested
    internal inner class BasketThrowing {

        @Test
        internal fun ageUnder12() {
            val categoryModel = CategoryModel(11, KORBEINWURF)

            val distance = ruleBook.run(categoryModel)

            assertThat(distance).isEqualTo("2m")
        }

        @Test
        internal fun ageAbove12() {
            val categoryModel = CategoryModel(12, KORBEINWURF)

            val distance = ruleBook.run(categoryModel)

            assertThat(distance).isEqualTo("2.5m")
        }
    }

    @Test
    internal fun ageUnder12() {
        val ballThrowing = CategoryModel(12, KORBEINWURF)
        val skipping = CategoryModel(12, SEILSPRINGEN)

        ruleBook.run(ballThrowing)

        val distance = ruleBook.run(skipping)

        assertThat(distance).isNull()
    }
}

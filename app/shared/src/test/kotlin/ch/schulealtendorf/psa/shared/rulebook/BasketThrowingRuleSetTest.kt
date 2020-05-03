package ch.schulealtendorf.psa.shared.rulebook

import ch.schulealtendorf.psa.dto.participation.GenderDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("unit-test")
internal class BasketThrowingRuleSetTest {
    private val ruleSet = BasketThrowingRuleSet()

    @Test
    internal fun onFemaleCompetitorsWith2m() {
        val model = FormulaModel("Korbeinwurf", "2m", 2.0, GenderDto.FEMALE)

        val points: Int = ruleSet.getRules().first { it.whenever(model) }.then(model)

        assertThat(points).isEqualTo(62)
    }

    @Test
    internal fun onFemaleCompetitorsWith2p5m() {
        val model = FormulaModel("Korbeinwurf", "2.5m", 10.0, GenderDto.FEMALE)

        val points: Int = ruleSet.getRules().first { it.whenever(model) }.then(model)

        assertThat(points).isEqualTo(402)
    }

    @Test
    internal fun onMaleCompetitorsWith2m() {
        val model = FormulaModel("Korbeinwurf", "2m", 8.0, GenderDto.MALE)

        val points: Int = ruleSet.getRules().first { it.whenever(model) }.then(model)

        assertThat(points).isEqualTo(264)
    }

    @Test
    internal fun onMaleCompetitorsOn2p5m() {
        val model = FormulaModel("Korbeinwurf", "2.5m", 8.0, GenderDto.MALE)

        val points: Int = ruleSet.getRules().first { it.whenever(model) }.then(model)

        assertThat(points).isEqualTo(292)
    }
}

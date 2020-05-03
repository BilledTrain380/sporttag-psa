package ch.schulealtendorf.psa.shared.rulebook

import ch.schulealtendorf.psa.dto.participation.GenderDto
import ch.schulealtendorf.psa.dto.participation.athletics.SEILSPRINGEN
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("unit-test")
internal class SkippingRuleSetTest {
    private val ruleSet = SkippingRuleSet()

    @Test
    internal fun onFemaleCompetitors() {
        val model = FormulaModel(SEILSPRINGEN, null, 170.0, GenderDto.FEMALE)

        val points: Int = ruleSet.getRules().first { it.whenever(model) }.then(model)

        assertThat(points).isEqualTo(598)
    }

    @Test
    internal fun onMaleCompetitors() {
        val model = FormulaModel(SEILSPRINGEN, null, 88.0, GenderDto.MALE)

        val points: Int = ruleSet.getRules().first { it.whenever(model) }.then(model)

        assertThat(points).isEqualTo(275)
    }
}

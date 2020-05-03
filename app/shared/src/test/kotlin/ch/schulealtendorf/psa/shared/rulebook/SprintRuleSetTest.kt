package ch.schulealtendorf.psa.shared.rulebook

import ch.schulealtendorf.psa.dto.participation.GenderDto
import ch.schulealtendorf.psa.dto.participation.athletics.SCHNELLLAUF
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("unit-test")
internal class SprintRuleSetTest {
    private val ruleSet = SprintRuleSet()

    @Test
    internal fun onFemaleCompetitors() {
        val model = FormulaModel(SCHNELLLAUF, "60m", 10.99, GenderDto.FEMALE)

        val points: Int = ruleSet.getRules().first { it.whenever(model) }.then(model)

        assertThat(points).isEqualTo(224)
    }

    @Test
    internal fun onMaleCompetitors() {
        val model = FormulaModel(SCHNELLLAUF, "60m", 11.4, GenderDto.MALE)

        val points: Int = ruleSet.getRules().first { it.whenever(model) }.then(model)

        assertThat(points).isEqualTo(128)
    }
}

package ch.schulealtendorf.psa.shared.rulebook

import ch.schulealtendorf.psa.dto.participation.GenderDto
import ch.schulealtendorf.psa.dto.participation.athletics.BALLZIELWURF
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("unit-test")
internal class TargetThrowingRuleSetTest {
    private val ruleSet = TargetThrowingRuleSet()

    @Test
    internal fun onFemaleCompetitors4m() {
        val model = FormulaModel(BALLZIELWURF, "4m", 17.0, GenderDto.FEMALE)

        val points: Int = ruleSet.getRules().first { it.whenever(model) }.then(model)

        assertThat(points).isEqualTo(160)
    }

    @Test
    internal fun onFemaleCompetitors5m() {
        val model = FormulaModel(BALLZIELWURF, "5m", 34.0, GenderDto.FEMALE)

        val points: Int = ruleSet.getRules().first { it.whenever(model) }.then(model)

        assertThat(points).isEqualTo(466)
    }

    @Test
    internal fun onMaleCompetitors4m() {
        val model = FormulaModel(BALLZIELWURF, "4m", 20.0, GenderDto.MALE)

        val points: Int = ruleSet.getRules().first { it.whenever(model) }.then(model)

        assertThat(points).isEqualTo(181)
    }

    @Test
    internal fun onMaleCompetitors5m() {
        val model = FormulaModel(BALLZIELWURF, "5m", 30.0, GenderDto.MALE)

        val points: Int = ruleSet.getRules().first { it.whenever(model) }.then(model)

        assertThat(points).isEqualTo(372)
    }
}

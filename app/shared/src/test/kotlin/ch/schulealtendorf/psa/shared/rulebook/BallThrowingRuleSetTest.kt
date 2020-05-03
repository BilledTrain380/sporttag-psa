package ch.schulealtendorf.psa.shared.rulebook

import ch.schulealtendorf.psa.dto.participation.GenderDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("unit-test")
internal class BallThrowingRuleSetTest {
    private val ruleSet = BallThrowingRuleSet()

    @Test
    internal fun onFemaleCompetitors() {
        val model = FormulaModel("Ballwurf", "60m", 32.96, GenderDto.FEMALE)

        val points: Int = ruleSet.getRules().first { it.whenever(model) }.then(model)

        assertThat(points).isEqualTo(440)
    }

    @Test
    internal fun onMaleCompetitors() {
        val model = FormulaModel("Ballwurf", "60m", 16.32, GenderDto.MALE)

        val points: Int = ruleSet.getRules().first { it.whenever(model) }.then(model)

        assertThat(points).isEqualTo(121)
    }
}

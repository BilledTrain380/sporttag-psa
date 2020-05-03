package ch.schulealtendorf.psa.shared.rulebook

import ch.schulealtendorf.psa.dto.participation.GenderDto
import ch.schulealtendorf.psa.dto.participation.athletics.WEITSPRUNG
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("unit-test")
internal class BroadJumpRuleSetTest {
    private val ruleSet = BroadJumpRuleSet()

    @Test
    internal fun onFemaleCompetitors() {
        val model = FormulaModel(WEITSPRUNG, null, 4.31, GenderDto.FEMALE)

        val points: Int = ruleSet.getRules().first { it.whenever(model) }.then(model)

        assertThat(points).isEqualTo(553)
    }

    @Test
    internal fun onMaleCompetitors() {
        val model = FormulaModel(WEITSPRUNG, null, 3.32, GenderDto.MALE)

        val points: Int = ruleSet.getRules().first { it.whenever(model) }.then(model)

        assertThat(points).isEqualTo(256)
    }
}

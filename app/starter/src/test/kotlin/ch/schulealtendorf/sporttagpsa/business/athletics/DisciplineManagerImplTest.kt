package ch.schulealtendorf.sporttagpsa.business.athletics

import org.assertj.core.api.Assertions.assertThat
import org.flywaydb.test.annotation.FlywayTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@ActiveProfiles("test")
@Tag("db-test")
@ExtendWith(SpringExtension::class)
@Import(DisciplineManagerImpl::class)
@DataJpaTest
@FlywayTest
internal class DisciplineManagerImplTest {

    @Autowired
    private lateinit var disciplineManager: DisciplineManagerImpl

    @Test
    internal fun getDisciplines() {
        val disciplines = disciplineManager.getDisciplines()

        assertThat(disciplines).hasSize(6)

        val schnelllauf = disciplines.find { it.name == "Schnelllauf" }
        assertThat(schnelllauf).isNotNull
        assertThat(schnelllauf?.hasDistance).isFalse()
        assertThat(schnelllauf?.hasTrials).isFalse()
        assertThat(schnelllauf?.unit?.name).isEqualTo("Sekunden")
        assertThat(schnelllauf?.unit?.factor).isEqualTo(100)

        val weitsprung = disciplines.find { it.name == "Weitsprung" }
        assertThat(weitsprung).isNotNull
        assertThat(weitsprung?.hasDistance).isFalse()
        assertThat(weitsprung?.hasTrials).isTrue()
        assertThat(weitsprung?.unit?.name).isEqualTo("Meter")
        assertThat(weitsprung?.unit?.factor).isEqualTo(100)

        val ballwurf = disciplines.find { it.name == "Ballwurf" }
        assertThat(ballwurf).isNotNull
        assertThat(ballwurf?.hasDistance).isFalse()
        assertThat(ballwurf?.hasTrials).isTrue()
        assertThat(ballwurf?.unit?.name).isEqualTo("Meter")
        assertThat(ballwurf?.unit?.factor).isEqualTo(100)

        val ballzielwurf = disciplines.find { it.name == "Ballzielwurf" }
        assertThat(ballzielwurf).isNotNull
        assertThat(ballzielwurf?.hasDistance).isTrue()
        assertThat(ballzielwurf?.hasTrials).isFalse()
        assertThat(ballzielwurf?.unit?.name).isEqualTo("Punkte")
        assertThat(ballzielwurf?.unit?.factor).isEqualTo(1)

        val seilspringen = disciplines.find { it.name == "Seilspringen" }
        assertThat(seilspringen).isNotNull
        assertThat(seilspringen?.hasDistance).isFalse()
        assertThat(seilspringen?.hasTrials).isTrue()
        assertThat(seilspringen?.unit?.name).isEqualTo("Punkte")
        assertThat(seilspringen?.unit?.factor).isEqualTo(1)

        val korbeinwurf = disciplines.find { it.name == "Korbeinwurf" }
        assertThat(korbeinwurf).isNotNull
        assertThat(korbeinwurf?.hasDistance).isTrue()
        assertThat(korbeinwurf?.hasTrials).isFalse()
        assertThat(korbeinwurf?.unit?.name).isEqualTo("Punkte")
        assertThat(korbeinwurf?.unit?.factor).isEqualTo(1)
    }

    @Test
    internal fun getDiscipline() {
        val disciplineOptional = disciplineManager.getDiscipline("Seilspringen")
        assertThat(disciplineOptional).isNotEmpty

        val seilspringen = disciplineOptional.get()
        assertThat(seilspringen.hasDistance).isFalse()
        assertThat(seilspringen.hasTrials).isTrue()
        assertThat(seilspringen.unit.name).isEqualTo("Punkte")
        assertThat(seilspringen.unit.factor).isEqualTo(1)
    }

    @Test
    internal fun getDisciplineWhenNameNotExisting() {
        val disciplineOptional = disciplineManager.getDiscipline("Not-existing")
        assertThat(disciplineOptional).isEmpty
    }
}

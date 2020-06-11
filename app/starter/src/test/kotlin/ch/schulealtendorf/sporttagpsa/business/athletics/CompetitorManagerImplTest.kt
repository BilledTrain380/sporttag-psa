package ch.schulealtendorf.sporttagpsa.business.athletics

import ch.schulealtendorf.psa.dto.participation.BirthdayDto
import ch.schulealtendorf.psa.dto.participation.GenderDto
import ch.schulealtendorf.psa.dto.participation.athletics.BALLWURF
import ch.schulealtendorf.psa.dto.participation.athletics.BALLZIELWURF
import ch.schulealtendorf.psa.dto.participation.athletics.KORBEINWURF
import ch.schulealtendorf.psa.dto.participation.athletics.ResultElement
import ch.schulealtendorf.psa.dto.participation.athletics.SCHNELLLAUF
import ch.schulealtendorf.psa.dto.participation.athletics.SEILSPRINGEN
import ch.schulealtendorf.psa.dto.participation.athletics.WEITSPRUNG
import ch.schulealtendorf.psa.shared.rulebook.PSARuleBook
import ch.schulealtendorf.sporttagpsa.repository.CompetitorRepository
import org.assertj.core.api.Assertions.assertThat
import org.flywaydb.test.annotation.FlywayTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.junit.jupiter.SpringExtension

@ActiveProfiles("test")
@Tag("db-test")
@ExtendWith(SpringExtension::class)
@Import(CompetitorManagerImpl::class, PSARuleBook::class)
@DataJpaTest
@FlywayTest
@Sql("/db/athletics/finish_participation.sql")
internal class CompetitorManagerImplTest {

    @Autowired
    private lateinit var competitorManager: CompetitorManagerImpl

    @Autowired
    private lateinit var competitorRepository: CompetitorRepository

    @Test
    internal fun getCompetitors() {
        val competitors = competitorManager.getCompetitors()

        assertThat(competitors).hasSize(3)
    }

    @Test
    internal fun getCompetitorsByFilter() {
        val filter = CompetitorFilter(gender = GenderDto.FEMALE)
        val competitors = competitorManager.getCompetitors(filter)

        assertThat(competitors).hasSize(1)
    }

    @Test
    internal fun getCompetitor() {
        val competitorOptional = competitorManager.getCompetitor(7)

        assertThat(competitorOptional).isNotEmpty

        val competitor = competitorOptional.get()
        assertThat(competitor).isNotNull
        assertThat(competitor.startnumber).isEqualTo(1)
        assertThat(competitor.prename).isEqualTo("Eric A")
        assertThat(competitor.surname).isEqualTo("Mason")
        assertThat(competitor.gender).isEqualTo(GenderDto.MALE)
        assertThat(competitor.birthday).isEqualTo(BirthdayDto.parse("2012-02-14"))
        assertThat(competitor.isAbsent).isFalse()
        assertThat(competitor.address).isEqualTo("Peck Court 51")
        assertThat(competitor.town.zip).isEqualTo("3000")
        assertThat(competitor.town.name).isEqualTo("Bern")

        assertThat(competitor.results.keys).contains(
            SCHNELLLAUF,
            WEITSPRUNG,
            BALLWURF,
            BALLZIELWURF,
            SEILSPRINGEN,
            KORBEINWURF
        )

        val schnelllauf = competitor.results[SCHNELLLAUF]
        assertThat(schnelllauf).isNotNull
        assertThat(schnelllauf?.value).isEqualTo(1)
        assertThat(schnelllauf?.points).isEqualTo(1)

        val weitsprung = competitor.results[WEITSPRUNG]
        assertThat(weitsprung).isNotNull
        assertThat(weitsprung?.value).isEqualTo(1)
        assertThat(weitsprung?.points).isEqualTo(1)

        val ballwurf = competitor.results[BALLWURF]
        assertThat(ballwurf).isNotNull
        assertThat(ballwurf?.value).isEqualTo(1)
        assertThat(ballwurf?.points).isEqualTo(1)

        val ballzielwurf = competitor.results[BALLZIELWURF]
        assertThat(ballzielwurf).isNotNull
        assertThat(ballzielwurf?.value).isEqualTo(1)
        assertThat(ballzielwurf?.points).isEqualTo(1)

        val seilspringen = competitor.results[SEILSPRINGEN]
        assertThat(seilspringen).isNotNull
        assertThat(seilspringen?.value).isEqualTo(1)
        assertThat(seilspringen?.points).isEqualTo(1)

        val korbeinwurf = competitor.results[KORBEINWURF]
        assertThat(korbeinwurf).isNotNull
        assertThat(korbeinwurf?.value).isEqualTo(1)
        assertThat(korbeinwurf?.points).isEqualTo(1)
    }

    @Test
    internal fun updateResult() {

        // Schnelllauf to 11.25 seconds
        val resultElement = ResultElement(id = 1, value = 1125)
        val resultAmend = CompetitorResultAmend(competitorId = 7, result = resultElement)

        val result = competitorManager.updateResult(resultAmend)

        assertThat(result.value).isEqualTo(1125)
        assertThat(result.points).isEqualTo(144)

        val savedResult = competitorRepository.findByParticipantId(resultAmend.competitorId)
            .map { it.results.find { resultEntity -> resultEntity.id == resultElement.id } }
        assertThat(savedResult).isNotEmpty
        assertThat(savedResult.get().value).isEqualTo(1125)
        assertThat(savedResult.get().points).isEqualTo(144)
    }

    @Test
    internal fun updateResultWhenIdNotFound() {
        val resultElement = ResultElement(id = 2000, value = 1125)
        val resultAmend = CompetitorResultAmend(competitorId = 7, result = resultElement)

        val exception = assertThrows<NoSuchElementException> {
            competitorManager.updateResult(resultAmend)
        }
        assertThat(exception).hasMessage("Could not find result: id=${resultElement.id}")
    }

    @Test
    internal fun updateResultWhenCompetitorIdNotFound() {
        val resultElement = ResultElement(id = 1, value = 1125)
        val resultAmend = CompetitorResultAmend(competitorId = 999, result = resultElement)

        val exception = assertThrows<NoSuchElementException> {
            competitorManager.updateResult(resultAmend)
        }
        assertThat(exception).hasMessage("Could not find competitor: id=${resultAmend.competitorId}")
    }

    @Test
    @FlywayTest
    internal fun deleteCompetitor() {
        assertThat(competitorRepository.existsById(1)).isTrue()

        competitorManager.deleteCompetitor(1)

        val competitor = competitorRepository.findByParticipantId(7)
        assertThat(competitor).isEmpty
    }
}

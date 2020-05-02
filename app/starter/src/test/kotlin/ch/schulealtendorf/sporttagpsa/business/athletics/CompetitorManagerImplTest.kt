package ch.schulealtendorf.sporttagpsa.business.athletics

import ch.schulealtendorf.psa.dto.BirthdayDto
import ch.schulealtendorf.psa.dto.participation.GenderDto
import ch.schulealtendorf.psa.dto.participation.SportTypeConstant
import ch.schulealtendorf.psa.dto.participation.athletics.ResultElement
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

        val sportTypes = competitors.map { it.participant.sportType }
        assertThat(sportTypes).containsOnly(SportTypeConstant.ATHLETICS)
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
        assertThat(competitor.participant.prename).isEqualTo("Eric A")
        assertThat(competitor.participant.surname).isEqualTo("Mason")
        assertThat(competitor.participant.gender).isEqualTo(GenderDto.MALE)
        assertThat(competitor.participant.birthday).isEqualTo(BirthdayDto.parse("2012-02-14T00:00:00Z"))
        assertThat(competitor.participant.isAbsent).isFalse()
        assertThat(competitor.participant.address).isEqualTo("Peck Court 51")
        assertThat(competitor.participant.town.zip).isEqualTo("3000")
        assertThat(competitor.participant.town.name).isEqualTo("Bern")
        assertThat(competitor.participant.sportType).isEqualTo(SportTypeConstant.ATHLETICS)

        assertThat(competitor.results.keys).contains(
            "Schnelllauf",
            "Weitsprung",
            "Ballwurf",
            "Ballzielwurf",
            "Seilspringen",
            "Korbeinwurf"
        )

        val schnelllauf = competitor.results["Schnelllauf"]
        assertThat(schnelllauf).isNotNull
        assertThat(schnelllauf?.value).isEqualTo(1)
        assertThat(schnelllauf?.points).isEqualTo(1)

        val weitsprung = competitor.results["Weitsprung"]
        assertThat(weitsprung).isNotNull
        assertThat(weitsprung?.value).isEqualTo(1)
        assertThat(weitsprung?.points).isEqualTo(1)

        val ballwurf = competitor.results["Ballwurf"]
        assertThat(ballwurf).isNotNull
        assertThat(ballwurf?.value).isEqualTo(1)
        assertThat(ballwurf?.points).isEqualTo(1)

        val ballzielwurf = competitor.results["Ballzielwurf"]
        assertThat(ballzielwurf).isNotNull
        assertThat(ballzielwurf?.value).isEqualTo(1)
        assertThat(ballzielwurf?.points).isEqualTo(1)

        val seilspringen = competitor.results["Seilspringen"]
        assertThat(seilspringen).isNotNull
        assertThat(seilspringen?.value).isEqualTo(1)
        assertThat(seilspringen?.points).isEqualTo(1)

        val korbeinwurf = competitor.results["Korbeinwurf"]
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
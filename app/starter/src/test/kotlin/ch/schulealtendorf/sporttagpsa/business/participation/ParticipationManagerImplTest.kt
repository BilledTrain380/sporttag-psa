package ch.schulealtendorf.sporttagpsa.business.participation

import ch.schulealtendorf.psa.dto.BirthdayDto
import ch.schulealtendorf.psa.dto.GenderDto
import ch.schulealtendorf.psa.dto.SportConstant.ATHLETICS
import ch.schulealtendorf.psa.dto.TownDto
import ch.schulealtendorf.psa.dto.participation.ParticipantDto
import ch.schulealtendorf.psa.dto.participation.ParticipationStatusType
import ch.schulealtendorf.psa.shared.rulebook.BasicCategoryRuleBook
import ch.schulealtendorf.sporttagpsa.repository.CompetitorRepository
import ch.schulealtendorf.sporttagpsa.repository.GroupRepository
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
import org.springframework.test.context.junit.jupiter.SpringExtension

@ActiveProfiles("test")
@Tag("db-test")
@ExtendWith(SpringExtension::class)
@Import(
    ParticipationManagerImpl::class,
    ParticipantManagerImpl::class,
    BasicCategoryRuleBook::class
)
@DataJpaTest
@FlywayTest
internal class ParticipationManagerImplTest {

    @Autowired
    private lateinit var participationManager: ParticipationManagerImpl

    @Autowired
    private lateinit var participantManager: ParticipantManagerImpl

    @Autowired
    private lateinit var competitorRepository: CompetitorRepository

    @Autowired
    private lateinit var groupRepository: GroupRepository

    @Test
    @FlywayTest
    internal fun participate() {
        val participant = participantManager.getParticipant(1).get()

        participationManager.participate(participant, ATHLETICS)

        val savedParticipant = participantManager.getParticipant(1).get()
        assertThat(savedParticipant.sport).isEqualTo(ATHLETICS)
    }

    @Test
    @FlywayTest
    internal fun participateWhenParticipationIsClosed() {
        participationManager.closeParticipation()

        val participant = participantManager.getParticipant(1).get()

        assertThrows<IllegalStateException>("Participation already closed. Use ParticipationManager#reParticipate instead") {
            participationManager.participate(participant, ATHLETICS)
        }
    }

    @Test
    internal fun participateWhenParticipantNotFound() {
        val participant = ParticipantDto(
            surname = "Miller",
            prename = "Ben R",
            gender = GenderDto.MALE,
            birthday = BirthdayDto.parse("2012-05-15T00:00:00Z"),
            address = "Deans Lane 124",
            town = TownDto(
                zip = "3027",
                name = "Bern"
            ),
            group = "2a"
        )

        assertThrows<NoSuchElementException>("Could not find participant: id=0") {
            participationManager.participate(participant, "2a")
        }
    }

    @Test
    internal fun participateWhenSportNotFound() {
        val participant = participantManager.getParticipant(1).get()

        val sport = "non-existing-sport"

        assertThrows<NoSuchElementException>("Could not find sport: name=$sport") {
            participationManager.participate(participant, sport)
        }
    }

    @Test
    @FlywayTest
    internal fun reParticipateWhenWasCompetitor() {
        participationManager.closeParticipation()

        val participant = participantManager.getParticipant(7).get()

        participationManager.reParticipate(participant, "Schatzsuche")

        val competitor = competitorRepository.findByParticipantId(7)
        assertThat(competitor).isEmpty

        val updatedParticipant = participantManager.getParticipant(7)
        assertThat(updatedParticipant.get().sport).isEqualTo("Schatzsuche")
    }

    @Test
    @FlywayTest
    internal fun reParticipateWhenWasGroupTypeFun() {
        participationManager.closeParticipation()

        val participant = participantManager.getParticipant(1).get()

        participationManager.reParticipate(participant, ATHLETICS)

        val competitorOptional = competitorRepository.findByParticipantId(1)
        assertThat(competitorOptional).isNotEmpty

        val competitor = competitorOptional.get()
        assertThat(competitor.results).hasSize(6)

        val resultDisciplines = competitor.results.map { it.discipline.name }
        assertThat(resultDisciplines).contains(
            "Schnelllauf",
            "Weitsprung",
            "Ballwurf",
            "Ballzielwurf",
            "Seilspringen",
            "Korbeinwurf"
        )
    }

    @Test
    internal fun reParticipateWhenParticipationIsOpen() {
        val participant = participantManager.getParticipant(1).get()

        assertThrows<IllegalStateException>("Participation is not closed. Use ParticipationManager#participate instead") {
            participationManager.reParticipate(participant, ATHLETICS)
        }
    }

    @Test
    @FlywayTest
    internal fun closeParticipation() {
        participationManager.closeParticipation()

        val participationStatus = participationManager.getParticipationStatus()
        assertThat(participationStatus).isEqualTo(ParticipationStatusType.CLOSED)

        val competitorOptional = competitorRepository.findByParticipantId(7)
        assertThat(competitorOptional).isNotEmpty

        val competitor = competitorOptional.get()
        assertThat(competitor.results).hasSize(6)

        val resultDisciplines = competitor.results.map { it.discipline.name }
        assertThat(resultDisciplines).contains(
            "Schnelllauf",
            "Weitsprung",
            "Ballwurf",
            "Ballzielwurf",
            "Seilspringen",
            "Korbeinwurf"
        )
    }

    @Test
    @FlywayTest
    internal fun resetParticipation() {
        participationManager.resetParticipation()

        val participants = participantManager.getParticipants()
        assertThat(participants).isEmpty()

        val competitors = competitorRepository.findAll()
        assertThat(competitors).isEmpty()

        val participationStatus = participationManager.getParticipationStatus()
        assertThat(participationStatus).isEqualTo(ParticipationStatusType.OPEN)

        val groups = groupRepository.findAll()
        assertThat(groups).isEmpty()
    }
}
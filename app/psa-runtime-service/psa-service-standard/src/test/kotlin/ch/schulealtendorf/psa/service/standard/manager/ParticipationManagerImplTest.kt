package ch.schulealtendorf.psa.service.standard.manager

import ch.schulealtendorf.psa.dto.group.SimpleGroupDto
import ch.schulealtendorf.psa.dto.participation.ATHLETICS
import ch.schulealtendorf.psa.dto.participation.BRENNBALL
import ch.schulealtendorf.psa.dto.participation.BirthdayDto
import ch.schulealtendorf.psa.dto.participation.GenderDto
import ch.schulealtendorf.psa.dto.participation.ParticipantDto
import ch.schulealtendorf.psa.dto.participation.ParticipationStatusType
import ch.schulealtendorf.psa.dto.participation.SCHATZSUCHE
import ch.schulealtendorf.psa.dto.participation.TownDto
import ch.schulealtendorf.psa.dto.participation.VELO_ROLLERBLADES
import ch.schulealtendorf.psa.dto.participation.athletics.BALLWURF
import ch.schulealtendorf.psa.dto.participation.athletics.BALLZIELWURF
import ch.schulealtendorf.psa.dto.participation.athletics.KORBEINWURF
import ch.schulealtendorf.psa.dto.participation.athletics.SCHNELLLAUF
import ch.schulealtendorf.psa.dto.participation.athletics.SEILSPRINGEN
import ch.schulealtendorf.psa.dto.participation.athletics.WEITSPRUNG
import ch.schulealtendorf.psa.service.standard.entity.ResultEntity
import ch.schulealtendorf.psa.service.standard.participantDtoOf
import ch.schulealtendorf.psa.service.standard.repository.CompetitorRepository
import ch.schulealtendorf.psa.service.standard.repository.GroupRepository
import ch.schulealtendorf.psa.service.standard.repository.ParticipantRepository
import ch.schulealtendorf.psa.shared.rulebook.BasicCategoryRuleBook
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
import java.util.Optional

@ActiveProfiles("test")
@Tag("db-test")
@ExtendWith(SpringExtension::class)
@Import(
    ParticipationManagerImpl::class,
    BasicCategoryRuleBook::class
)
@DataJpaTest
@FlywayTest
internal class ParticipationManagerImplTest {

    @Autowired
    private lateinit var participationManager: ParticipationManagerImpl

    @Autowired
    private lateinit var participantRepository: ParticipantRepository

    @Autowired
    private lateinit var competitorRepository: CompetitorRepository

    @Autowired
    private lateinit var groupRepository: GroupRepository

    @Test
    @FlywayTest
    internal fun participate() {
        val participant = getParticipant(1).get()

        participationManager.participate(participant, ATHLETICS)

        val savedParticipant = getParticipant(1).get()
        assertThat(savedParticipant.sportType).isEqualTo(ATHLETICS)
    }

    @Test
    @FlywayTest
    internal fun participateWhenParticipationIsClosed() {
        participationManager.closeParticipation()

        val participant = getParticipant(1).get()

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
            birthday = BirthdayDto.parse("2012-05-15"),
            address = "Deans Lane 124",
            town = TownDto(
                zip = "3027",
                name = "Bern"
            ),
            group = SimpleGroupDto.ofNameOnly("2a")
        )

        assertThrows<NoSuchElementException>("Could not find participant: id=0") {
            participationManager.participate(participant, "2a")
        }
    }

    @Test
    internal fun participateWhenSportNotFound() {
        val participant = getParticipant(1).get()

        val sport = "non-existing-sport"

        assertThrows<NoSuchElementException>("Could not find sport: name=$sport") {
            participationManager.participate(participant, sport)
        }
    }

    @Test
    @FlywayTest
    internal fun reParticipateWhenWasCompetitor() {
        participationManager.closeParticipation()

        val participant = getParticipant(7).get()

        participationManager.reParticipate(participant, SCHATZSUCHE)

        val competitor = competitorRepository.findByParticipantId(7)
        assertThat(competitor).isEmpty

        val updatedParticipant = getParticipant(7)
        assertThat(updatedParticipant.get().sportType).isEqualTo(SCHATZSUCHE)
    }

    @Test
    @FlywayTest
    internal fun reParticipateWhenWasGroupTypeFun() {
        participationManager.closeParticipation()

        val participant = getParticipant(1).get()

        participationManager.reParticipate(participant, ATHLETICS)

        val competitorOptional = competitorRepository.findByParticipantId(1)
        assertThat(competitorOptional).isNotEmpty
        assertDefaultResults(competitorOptional.get().results)
    }

    @Test
    internal fun reParticipateWhenParticipationIsOpen() {
        val participant = getParticipant(1).get()

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
        assertDefaultResults(competitorOptional.get().results)
    }

    @Test
    @FlywayTest
    internal fun resetParticipation() {
        participationManager.resetParticipation()

        val participants = getParticipants()
        assertThat(participants).isEmpty()

        val competitors = competitorRepository.findAll()
        assertThat(competitors).isEmpty()

        val participationStatus = participationManager.getParticipationStatus()
        assertThat(participationStatus).isEqualTo(ParticipationStatusType.OPEN)

        val groups = groupRepository.findAll()
        assertThat(groups).isEmpty()
    }

    @Test
    internal fun getSportTypes() {
        val sportTypes = participationManager.getSportTypes()

        assertThat(sportTypes).hasSize(4)
        assertThat(sportTypes)
            .contains(SCHATZSUCHE)
            .contains(ATHLETICS)
            .contains(BRENNBALL)
            .contains(VELO_ROLLERBLADES)
    }

    private fun assertDefaultResults(results: Collection<ResultEntity>) {
        assertThat(results).hasSize(6)

        val schnelllauf = results.find { it.discipline.name == SCHNELLLAUF }
        assertThat(schnelllauf).isNotNull
        assertThat(schnelllauf?.value).isEqualTo(100)
        assertThat(schnelllauf?.points).isEqualTo(1)

        val weitsprung = results.find { it.discipline.name == WEITSPRUNG }
        assertThat(weitsprung).isNotNull
        assertThat(weitsprung?.value).isEqualTo(100)
        assertThat(weitsprung?.points).isEqualTo(1)

        val ballwurf = results.find { it.discipline.name == BALLWURF }
        assertThat(ballwurf).isNotNull
        assertThat(ballwurf?.value).isEqualTo(100)
        assertThat(ballwurf?.points).isEqualTo(1)

        val ballzielwurf = results.find { it.discipline.name == BALLZIELWURF }
        assertThat(ballzielwurf).isNotNull
        assertThat(ballzielwurf?.value).isEqualTo(1)
        assertThat(ballzielwurf?.points).isEqualTo(1)

        val seilspringen = results.find { it.discipline.name == SEILSPRINGEN }
        assertThat(seilspringen).isNotNull
        assertThat(seilspringen?.value).isEqualTo(1)
        assertThat(seilspringen?.points).isEqualTo(1)

        val korbeinwurf = results.find { it.discipline.name == KORBEINWURF }
        assertThat(korbeinwurf).isNotNull
        assertThat(korbeinwurf?.value).isEqualTo(1)
        assertThat(korbeinwurf?.points).isEqualTo(1)
    }

    private fun getParticipant(id: Int): Optional<ParticipantDto> {
        return participantRepository.findById(id).map { participantDtoOf(it) }
    }

    private fun getParticipants(): List<ParticipantDto> {
        return participantRepository.findAll().map { participantDtoOf(it) }
    }
}

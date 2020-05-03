package ch.schulealtendorf.sporttagpsa.business.participation

import ch.schulealtendorf.psa.dto.group.SimpleGroupDto
import ch.schulealtendorf.psa.dto.participation.BirthdayDto
import ch.schulealtendorf.psa.dto.participation.GenderDto
import ch.schulealtendorf.psa.dto.participation.ParticipantDto
import ch.schulealtendorf.psa.dto.participation.SCHATZSUCHE
import ch.schulealtendorf.psa.dto.participation.TownDto
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
import java.time.ZonedDateTime

@ActiveProfiles("test")
@Tag("db-test")
@ExtendWith(SpringExtension::class)
@Import(ParticipantManagerImpl::class)
@DataJpaTest
@FlywayTest
internal class ParticipantManagerImplTest {

    @Autowired
    private lateinit var participantManager: ParticipantManagerImpl

    @Test
    internal fun getParticipants() {
        val participants = participantManager.getParticipants()

        assertThat(participants).hasSize(10)
    }

    @Test
    internal fun getParticipantsByGroup() {
        val participants = participantManager.getParticipantsByGroup("2a")

        assertThat(participants).hasSize(5)

        // Just test if non null properties are set correctly
        val prenames = participants.map { it.prename }
        assertThat(prenames).doesNotContainNull()

        val surnames = participants.map { it.surname }
        assertThat(surnames).doesNotContainNull()

        val birthdays = participants.map { it.birthday }
        assertThat(birthdays).doesNotContainNull()

        val genders = participants.map { it.gender }
        assertThat(genders).doesNotContainNull()

        val addresses = participants.map { it.address }
        assertThat(addresses).doesNotContainNull()
    }

    @Test
    internal fun getParticipantById() {
        val participantOptional = participantManager.getParticipant(1)

        assertThat(participantOptional).isPresent

        val participant = participantOptional.get()
        assertThat(participant.prename).isEqualTo("Emily")
        assertThat(participant.surname).isEqualTo("Hill")
        assertThat(participant.gender).isEqualTo(GenderDto.FEMALE)
        assertThat(participant.birthday.time).isEqualTo(ZonedDateTime.parse("2011-02-23T00:00:00Z").toInstant())
        assertThat(participant.address).isEqualTo("Hanover Street 45")
        assertThat(participant.town.zip).isEqualTo("3000")
        assertThat(participant.town.name).isEqualTo("Bern")
        assertThat(participant.group.name).isEqualTo("2a")
        assertThat(participant.sportType).isEqualTo(SCHATZSUCHE)
    }

    @Test
    internal fun saveNewParticipant() {
        val participant = ParticipantDto(
            surname = "Meyer",
            prename = "Max",
            gender = GenderDto.MALE,
            birthday = BirthdayDto.parse("2012-05-15T00:00:00Z"),
            address = "Hillburg 48",
            town = TownDto(
                zip = "3027",
                name = "Bern"
            ),
            group = SimpleGroupDto.ofNameOnly("2a")
        )

        val savedParticipant = participantManager.saveParticipant(participant)
        assertThat(savedParticipant.id).isNotZero()
        assertThat(savedParticipant.surname).isEqualTo(participant.surname)
        assertThat(savedParticipant.prename).isEqualTo(participant.prename)
        assertThat(savedParticipant.gender).isEqualTo(GenderDto.MALE)
        assertThat(savedParticipant.isAbsent).isFalse()
        assertThat(savedParticipant.birthday).isEqualTo(participant.birthday)
        assertThat(savedParticipant.address).isEqualTo(participant.address)
        assertThat(savedParticipant.town).isEqualTo(participant.town)
        assertThat(savedParticipant.group.name).isEqualTo(participant.group.name)
    }

    @Test
    @FlywayTest
    internal fun saveExistingParticipant() {
        val participantOptional = participantManager.getParticipant(1)

        assertThat(participantOptional).isPresent

        val participant = participantOptional.get().toBuilder()
            .setSurname("Meyer")
            .setAbsent(true)
            .build()

        val savedParticipant = participantManager.saveParticipant(participant)
        assertThat(savedParticipant.id).isEqualTo(participantOptional.get().id)
        assertThat(savedParticipant.surname).isEqualTo(participant.surname)
        assertThat(savedParticipant.isAbsent).isTrue()
    }

    @Test
    internal fun saveParticipantToNonExistingGroup() {
        val participant = ParticipantDto(
            surname = "Haller",
            prename = "Alisha",
            gender = GenderDto.FEMALE,
            birthday = BirthdayDto.parse("2011-07-25T00:00:00Z"),
            address = "Davis Court 535",
            town = TownDto(
                zip = "3000",
                name = "Bern"
            ),
            group = SimpleGroupDto.ofNameOnly("non-existing group")
        )

        assertThrows<NoSuchElementException>("Group does not exist: name=${participant.group}") {
            participantManager.saveParticipant(participant)
        }
    }

    @Test
    @FlywayTest
    internal fun deleteParticipant() {
        participantManager.deleteParticipantById(1)

        val participant = participantManager.getParticipant(1)
        assertThat(participant).isEmpty
    }
}

/*
 * Copyright (c) 2018 by Nicolas Märchy
 *
 * This file is part of Sporttag PSA.
 *
 * Sporttag PSA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Sporttag PSA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Sporttag PSA.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Diese Datei ist Teil von Sporttag PSA.
 *
 * Sporttag PSA ist Freie Software: Sie können es unter den Bedingungen
 * der GNU General Public License, wie von der Free Software Foundation,
 * Version 3 der Lizenz oder (nach Ihrer Wahl) jeder späteren
 * veröffentlichten Version, weiterverbreiten und/oder modifizieren.
 *
 * Sporttag PSA wird in der Hoffnung, dass es nützlich sein wird, aber
 * OHNE JEDE GEWÄHRLEISTUNG, bereitgestellt; sogar ohne die implizite
 * Gewährleistung der MARKTFÄHIGKEIT oder EIGNUNG FÜR EINEN BESTIMMTEN ZWECK.
 * Siehe die GNU General Public License für weitere Details.
 *
 * Sie sollten eine Kopie der GNU General Public License zusammen mit diesem
 * Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.
 *
 *
 */

package ch.schulealtendorf.sporttagpsa.business.participation

import ch.schulealtendorf.psa.dto.BirthdayDto
import ch.schulealtendorf.psa.dto.CoachDto
import ch.schulealtendorf.psa.dto.GroupDto
import ch.schulealtendorf.psa.dto.ParticipantDto
import ch.schulealtendorf.psa.dto.SportDto
import ch.schulealtendorf.psa.dto.TownDto
import ch.schulealtendorf.sporttagpsa.entity.GroupEntity
import ch.schulealtendorf.sporttagpsa.entity.ParticipantEntity
import ch.schulealtendorf.sporttagpsa.entity.SportEntity
import ch.schulealtendorf.sporttagpsa.entity.TownEntity
import ch.schulealtendorf.sporttagpsa.repository.AbsentParticipantRepository
import ch.schulealtendorf.sporttagpsa.repository.GroupRepository
import ch.schulealtendorf.sporttagpsa.repository.ParticipantRepository
import ch.schulealtendorf.sporttagpsa.repository.TownRepository
import org.springframework.stereotype.Component
import java.util.*
import kotlin.NoSuchElementException

/**
 * Default implementation of a {@link ParticipantManager} which uses the repository classes.
 *
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.0.0
 */
@Component
class ParticipantManagerImpl(
        private val participantRepository: ParticipantRepository,
        private val absentRepository: AbsentParticipantRepository,
        private val townRepository: TownRepository,
        private val groupRepository: GroupRepository
) : ParticipantManager {

    override fun getParticipants() = participantRepository.findAll().map { it.toParticipant() }

    override fun getParticipant(id: Int): Optional<ParticipantDto> = participantRepository.findById(id).map { it.toParticipant() }

    override fun saveParticipant(participant: ParticipantDto): ParticipantDto {

        val participantEntity: ParticipantEntity = participantRepository.findById(participant.id)
                .orElseGet { ParticipantEntity() }

        val townEntity = townRepository.findByZipAndName(participant.town.zip, participant.town.name)
                .orElseGet { TownEntity(zip = participant.town.zip, name = participant.town.name) }

        val groupEntity = groupRepository.findById(participant.group.name)
                .orElseThrow { NoSuchElementException("Participant group does not exist: name=${participant.group.name}") }

        participantEntity.apply {
            surname = participant.surname
            prename = participant.prename
            gender = participant.gender
            birthday = participant.birthday.milliseconds
            address = participant.address
            town = townEntity
            group = groupEntity
        }

        return participantRepository.save(participantEntity).toParticipant()
    }

    override fun deleteParticipant(participant: ParticipantDto) {

        val participantEntity = participantRepository.findById(participant.id)

        participantEntity.ifPresent {
            participantRepository.delete(it)
        }
    }

    private fun ParticipantEntity.toParticipant(): ParticipantDto {
        return ParticipantDto(
                id!!,
                surname,
                prename,
                gender,
                BirthdayDto(birthday),
                absentRepository.findByParticipantId(id!!).isPresent,
                address,
                town.toTown(),
                group.toGroup(),
                sport.toSport()
        )
    }

    private fun TownEntity.toTown() = TownDto(zip, name)

    private fun GroupEntity.toGroup() = GroupDto(name, CoachDto(coach.id!!, coach.name))

    private fun SportEntity?.toSport(): Optional<SportDto> {
        return Optional.ofNullable(
                if (this == null) this else SportDto(name)
        )
    }
}

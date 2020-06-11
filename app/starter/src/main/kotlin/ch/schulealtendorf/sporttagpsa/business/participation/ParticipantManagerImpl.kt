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

import ch.schulealtendorf.psa.dto.participation.ParticipantDto
import ch.schulealtendorf.sporttagpsa.entity.ParticipantEntity
import ch.schulealtendorf.sporttagpsa.entity.TownEntity
import ch.schulealtendorf.sporttagpsa.lib.participantDtoOf
import ch.schulealtendorf.sporttagpsa.repository.GroupRepository
import ch.schulealtendorf.sporttagpsa.repository.ParticipantRepository
import ch.schulealtendorf.sporttagpsa.repository.TownRepository
import org.springframework.stereotype.Component
import java.util.Optional

/**
 * Default implementation of a {@link ParticipantManager} which uses the repository classes.
 *
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.0.0
 */
@Component
class ParticipantManagerImpl(
    private val participantRepository: ParticipantRepository,
    private val townRepository: TownRepository,
    private val groupRepository: GroupRepository
) : ParticipantManager {

    override fun getParticipants(): List<ParticipantDto> = participantRepository.findAll().map { it.toDto() }

    override fun getParticipantsByGroup(groupName: String): List<ParticipantDto> =
        participantRepository.findByGroupName(groupName).map { it.toDto() }

    override fun getParticipant(id: Int): Optional<ParticipantDto> =
        participantRepository.findById(id).map { it.toDto() }

    override fun saveParticipant(participant: ParticipantDto): ParticipantDto {
        val participantEntity = participantRepository.findById(participant.id)
            .orElseGet { ParticipantEntity() }

        val townEntity = townRepository.findByZipAndName(participant.town.zip, participant.town.name)
            .orElseGet { TownEntity(zip = participant.town.zip, name = participant.town.name) }

        val groupEntity = groupRepository.findById(participant.group.name)
            .orElseThrow { NoSuchElementException("Group does not exist: name=${participant.group}") }

        participantEntity.apply {
            surname = participant.surname
            prename = participant.prename
            gender = participant.gender
            birthday = participant.birthday.value
            address = participant.address
            town = townEntity
            group = groupEntity
            absent = participant.isAbsent
        }

        return participantRepository.save(participantEntity).toDto()
    }

    override fun deleteParticipantById(id: Int) {
        val participantEntity = participantRepository.findById(id)

        participantEntity.ifPresent {
            participantRepository.delete(it)
        }
    }

    private fun ParticipantEntity.toDto() = participantDtoOf(this)
}

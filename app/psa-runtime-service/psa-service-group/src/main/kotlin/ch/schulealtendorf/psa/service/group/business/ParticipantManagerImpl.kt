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

package ch.schulealtendorf.psa.service.group.business

import ch.schulealtendorf.psa.dto.participation.ATHLETICS
import ch.schulealtendorf.psa.dto.participation.ParticipantDto
import ch.schulealtendorf.psa.dto.participation.ParticipationStatusType
import ch.schulealtendorf.psa.service.standard.entity.ParticipantEntity
import ch.schulealtendorf.psa.service.standard.entity.TownEntity
import ch.schulealtendorf.psa.service.standard.manager.DefaultResultManager
import ch.schulealtendorf.psa.service.standard.participantDtoOf
import ch.schulealtendorf.psa.service.standard.repository.GroupRepository
import ch.schulealtendorf.psa.service.standard.repository.ParticipantRepository
import ch.schulealtendorf.psa.service.standard.repository.ParticipationRepository
import ch.schulealtendorf.psa.service.standard.repository.SportRepository
import ch.schulealtendorf.psa.service.standard.repository.TownRepository
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
    private val participationRepository: ParticipationRepository,
    private val defaultResultManager: DefaultResultManager,
    private val townRepository: TownRepository,
    private val groupRepository: GroupRepository,
    private val sportRepository: SportRepository
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

    override fun participate(participant: ParticipantDto, sport: String) {
        val participationStatus = participationRepository.getParticipationOrFail().statusType

        if (participationStatus == ParticipationStatusType.CLOSED) {
            throw IllegalStateException("Participation already closed. Use ParticipantManager#reParticipate instead")
        }

        val participantEntity = participantRepository.getParticipantOrFail(participant.id)
        val sportEntity = sportRepository.getSportOrFail(sport)

        participantEntity.sport = sportEntity

        participantRepository.save(participantEntity)
    }

    override fun reParticipate(participant: ParticipantDto, sport: String) {
        val participationStatus = participationRepository.getParticipationOrFail().statusType

        if (participationStatus != ParticipationStatusType.CLOSED) {
            throw IllegalStateException("Participation is not closed. Use ParticipantManager#participate instead")
        }

        val participantEntity = participantRepository.getParticipantOrFail(participant.id)

        // Only proceed further if sport type differs from existing
        if (participantEntity.sport != null && participantEntity.sport?.name == sport) {
            return
        }

        participantEntity.sport = sportRepository.getSportOrFail(sport)
        participantRepository.save(participantEntity)

        if (sport == ATHLETICS) {
            defaultResultManager.saveAsCompetitor(participantEntity)
        } else {
            defaultResultManager.deleteAsCompetitor(participantEntity)
        }
    }

    override fun getParticipationStatus(): ParticipationStatusType =
        participationRepository.getParticipationOrFail().statusType

    private fun ParticipantEntity.toDto() = participantDtoOf(this)
}

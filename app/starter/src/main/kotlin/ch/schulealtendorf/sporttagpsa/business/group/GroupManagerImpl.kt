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

package ch.schulealtendorf.sporttagpsa.business.group

import ch.schulealtendorf.psa.dto.CoachDto
import ch.schulealtendorf.psa.dto.GroupDto
import ch.schulealtendorf.sporttagpsa.business.participation.ATHLETICS
import ch.schulealtendorf.sporttagpsa.entity.CoachEntity
import ch.schulealtendorf.sporttagpsa.entity.GroupEntity
import ch.schulealtendorf.sporttagpsa.entity.ParticipantEntity
import ch.schulealtendorf.sporttagpsa.entity.TownEntity
import ch.schulealtendorf.sporttagpsa.from
import ch.schulealtendorf.sporttagpsa.repository.CoachRepository
import ch.schulealtendorf.sporttagpsa.repository.GroupRepository
import ch.schulealtendorf.sporttagpsa.repository.ParticipantRepository
import ch.schulealtendorf.sporttagpsa.repository.TownRepository
import org.springframework.stereotype.Component
import java.util.*

/**
 * A {@link GroupManager} which uses repositories to process its data.
 *
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.0.0
 */
@Component
class GroupManagerImpl(
        private val groupRepository: GroupRepository,
        private val participantRepository: ParticipantRepository,
        private val coachRepository: CoachRepository,
        private val townRepository: TownRepository
) : GroupManager {

    /**
     * @return true if the given {@code group} has participant, which are not participate in any sport, otherwise false
     */
    override fun hasPendingParticipation(group: GroupDto): Boolean {

        val participants = participantRepository.findByGroupName(group.name)

        return participants.any { it.sport == null }
    }

    /**
     * @return all groups
     */
    override fun getGroups(): List<GroupDto> {
        return groupRepository.findAll()
                .map { GroupDto from it }
    }

    override fun isCompetitive(group: GroupDto): Boolean {

        val participants = participantRepository.findByGroupName(group.name)

        return participants.any { it.sport != null && it.sport?.name == ATHLETICS }
    }

    /**
     * Gets the group matching the given {@code name}.
     *
     * @param name tha name of the group
     *
     * @return an Optional containing the group, or empty if the group could not be found
     */
    override fun getGroup(name: String): Optional<GroupDto> = groupRepository.findById(name).map { GroupDto from it }

    /**
     * Gets the coach matching the given {@code name}.
     *
     * @param name the name of the coach
     *
     * @return an Optional containing the coach, or empty if the coach could not be found
     */
    override fun getCoach(name: String): Optional<CoachDto> = coachRepository.findByName(name).map { CoachDto from it }

    /**
     * Imports the given {@code participant} by considering all their relations.
     * If a relation does not exist yet, it will be created, otherwise the already
     * created relation will be used.
     *
     * The participant will always be created.
     *
     * @param participant the participant to import
     */
    override fun import(participant: FlatParticipant) {

        val town = townRepository.findByZipAndName(participant.zipCode, participant.town)
                .orElseGet { TownEntity(zip = participant.zipCode, name = participant.town) }

        val coach = coachRepository.findByName(participant.coach)
                .orElseGet { CoachEntity(name = participant.coach) }

        val group = groupRepository.findByName(participant.group)
                .orElseGet { GroupEntity(participant.group, coach) }

        val participantEntity = ParticipantEntity(
                surname = participant.surname,
                prename = participant.prename,
                gender = participant.gender,
                birthday = participant.birthday.milliseconds,
                address = participant.address,
                town = town,
                group = group
        )

        participantRepository.save(participantEntity)
    }
}
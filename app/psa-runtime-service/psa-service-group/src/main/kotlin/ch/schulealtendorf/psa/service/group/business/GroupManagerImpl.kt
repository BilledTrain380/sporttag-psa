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

import ch.schulealtendorf.psa.dto.group.GroupStatusType
import ch.schulealtendorf.psa.dto.group.OverviewGroupDto
import ch.schulealtendorf.psa.dto.group.SimpleGroupDto
import ch.schulealtendorf.psa.dto.participation.ATHLETICS
import ch.schulealtendorf.psa.dto.status.StatusDto
import ch.schulealtendorf.psa.dto.status.StatusEntry
import ch.schulealtendorf.psa.dto.status.StatusSeverity
import ch.schulealtendorf.psa.service.group.business.parsing.FlatParticipant
import ch.schulealtendorf.psa.service.standard.entity.CoachEntity
import ch.schulealtendorf.psa.service.standard.entity.GroupEntity
import ch.schulealtendorf.psa.service.standard.entity.ParticipantEntity
import ch.schulealtendorf.psa.service.standard.entity.TownEntity
import ch.schulealtendorf.psa.service.standard.repository.CoachRepository
import ch.schulealtendorf.psa.service.standard.repository.GroupRepository
import ch.schulealtendorf.psa.service.standard.repository.ParticipantRepository
import ch.schulealtendorf.psa.service.standard.repository.TownRepository
import ch.schulealtendorf.psa.service.standard.simpleGroupDtoOf
import org.springframework.stereotype.Component
import java.util.Optional

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
    override fun hasPendingParticipation(group: SimpleGroupDto) = hasPendingParticipation(group.name)

    override fun isCompetitive(group: SimpleGroupDto) = isCompetitive(group.name)

    override fun getGroup(name: String): Optional<SimpleGroupDto> {
        return groupRepository.findById(name)
            .map { it.toDto() }
    }

    override fun getGroups(): List<SimpleGroupDto> {
        return groupRepository.findAll()
            .map { it.toDto() }
    }

    override fun getGroupsBy(filter: GroupStatusType): List<SimpleGroupDto> {
        return getOverviewBy(filter)
            .map { it.group }
    }

    override fun getOverview(): List<OverviewGroupDto> {
        return groupRepository.findAll()
            .map { it.toOverview() }
    }

    override fun getOverviewBy(filter: GroupStatusType): List<OverviewGroupDto> {
        return getOverview()
            .filter { it.status.contains(filter) }
    }

    @Deprecated("Use participant manager instead")
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
            birthday = participant.birthday.value,
            address = participant.address,
            town = town,
            group = group
        )

        participantRepository.save(participantEntity)
    }

    private fun hasPendingParticipation(groupName: String): Boolean {
        return participantRepository.findByGroupName(groupName)
            .any { it.sport == null }
    }

    private fun isCompetitive(groupName: String): Boolean {
        return participantRepository.findByGroupName(groupName)
            .any { it.isCompetitive() }
    }

    private fun ParticipantEntity.isCompetitive() = sport != null && sport?.name == ATHLETICS

    private fun GroupEntity.toDto() = simpleGroupDtoOf(this)

    private fun GroupEntity.toOverview(): OverviewGroupDto {
        var severity = StatusSeverity.OK

        val statusList = ArrayList<StatusEntry>()

        if (hasPendingParticipation(name)) {
            severity = StatusSeverity.WARNING
            statusList.add(
                StatusEntry(
                    StatusSeverity.WARNING,
                    GroupStatusType.UNFINISHED_PARTICIPANTS
                )
            )
        }

        val statusType =
            if (isCompetitive(name)) GroupStatusType.GROUP_TYPE_COMPETITIVE
            else GroupStatusType.GROUP_TYPE_FUN

        statusList.add(
            StatusEntry(
                StatusSeverity.INFO,
                statusType
            )
        )

        return OverviewGroupDto(
            this.toDto(),
            StatusDto(
                severity,
                statusList
            )
        )
    }
}

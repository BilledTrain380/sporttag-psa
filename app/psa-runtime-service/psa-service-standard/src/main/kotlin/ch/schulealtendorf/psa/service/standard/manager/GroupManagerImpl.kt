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

package ch.schulealtendorf.psa.service.standard.manager

import ch.schulealtendorf.psa.dto.group.GroupStatusType
import ch.schulealtendorf.psa.dto.group.OverviewGroupDto
import ch.schulealtendorf.psa.dto.group.SimpleGroupDto
import ch.schulealtendorf.psa.dto.participation.ATHLETICS
import ch.schulealtendorf.psa.dto.status.StatusDto
import ch.schulealtendorf.psa.dto.status.StatusEntry
import ch.schulealtendorf.psa.dto.status.StatusSeverity
import ch.schulealtendorf.psa.service.standard.entity.GroupEntity
import ch.schulealtendorf.psa.service.standard.entity.ParticipantEntity
import ch.schulealtendorf.psa.service.standard.repository.GroupRepository
import ch.schulealtendorf.psa.service.standard.repository.ParticipantRepository
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
    private val participantRepository: ParticipantRepository
) : GroupManager {
    override fun hasPendingParticipation(group: SimpleGroupDto): Boolean {
        val participants = participantRepository.findByGroupName(group.name)

        return hasPendingParticipation(participants)
    }

    override fun isCompetitive(group: SimpleGroupDto): Boolean {
        val participants = participantRepository.findByGroupName(group.name)

        return hasCompetitors(participants)
    }

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

    private fun hasPendingParticipation(participants: List<ParticipantEntity>): Boolean {
        return participants.any { it.sport == null }
    }

    private fun hasCompetitors(participants: List<ParticipantEntity>): Boolean {
        return participants.any { it.isCompetitive() }
    }

    private fun hasNonCompetitors(participants: List<ParticipantEntity>): Boolean {
        return participants.any { it.isCompetitive().not() }
    }

    private fun ParticipantEntity.isCompetitive() = sport != null && sport?.name == ATHLETICS

    private fun GroupEntity.toDto() = simpleGroupDtoOf(this)

    private fun GroupEntity.toOverview(): OverviewGroupDto {
        var severity = StatusSeverity.OK

        val statusList = ArrayList<StatusEntry>()

        val participants = participantRepository.findByGroupName(name)

        if (hasPendingParticipation(participants)) {
            severity = StatusSeverity.INFO
            statusList.add(
                StatusEntry(
                    StatusSeverity.INFO,
                    GroupStatusType.UNFINISHED_PARTICIPANTS
                )
            )
        }

        if (hasCompetitors(participants)) {
            statusList.add(
                StatusEntry(
                    StatusSeverity.INFO,
                    GroupStatusType.GROUP_TYPE_COMPETITIVE
                )
            )
        }

        if (hasNonCompetitors(participants)) {
            statusList.add(
                StatusEntry(
                    StatusSeverity.INFO,
                    GroupStatusType.GROUP_TYPE_FUN
                )
            )
        }

        return OverviewGroupDto(
            this.toDto(),
            StatusDto(
                severity,
                statusList
            )
        )
    }
}

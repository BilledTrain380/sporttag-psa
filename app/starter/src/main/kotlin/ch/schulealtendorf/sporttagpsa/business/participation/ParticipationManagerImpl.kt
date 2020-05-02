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
import ch.schulealtendorf.psa.dto.participation.ParticipationStatusType
import ch.schulealtendorf.psa.dto.participation.SportTypeConstant.ATHLETICS
import ch.schulealtendorf.psa.shared.rulebook.CategoryModel
import ch.schulealtendorf.psa.shared.rulebook.CategoryRuleBook
import ch.schulealtendorf.sporttagpsa.entity.CompetitorEntity
import ch.schulealtendorf.sporttagpsa.entity.ParticipantEntity
import ch.schulealtendorf.sporttagpsa.entity.ParticipationEntity
import ch.schulealtendorf.sporttagpsa.entity.ParticipationEntity.Companion.MAIN_PARTICIPATION
import ch.schulealtendorf.sporttagpsa.entity.ResultEntity
import ch.schulealtendorf.sporttagpsa.entity.SportEntity
import ch.schulealtendorf.sporttagpsa.lib.ageOf
import ch.schulealtendorf.sporttagpsa.repository.CompetitorRepository
import ch.schulealtendorf.sporttagpsa.repository.DisciplineRepository
import ch.schulealtendorf.sporttagpsa.repository.GroupRepository
import ch.schulealtendorf.sporttagpsa.repository.ParticipantRepository
import ch.schulealtendorf.sporttagpsa.repository.ParticipationRepository
import ch.schulealtendorf.sporttagpsa.repository.SportRepository
import org.springframework.stereotype.Component

/**
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.0.0
 */
@Component
class ParticipationManagerImpl(
    private val participantRepository: ParticipantRepository,
    private val participationRepository: ParticipationRepository,
    private val competitorRepository: CompetitorRepository,
    private val categoryRuleBook: CategoryRuleBook,
    private val sportRepository: SportRepository,
    private val disciplineRepository: DisciplineRepository,
    private val groupRepository: GroupRepository
) : ParticipationManager {
    override fun participate(participant: ParticipantDto, sport: String) {
        if (getParticipationStatus() == ParticipationStatusType.CLOSED) {
            throw IllegalStateException("Participation already closed. Use ParticipationManager#reParticipate instead")
        }

        val participantEntity = participantRepository.getParticipantOrFail(participant.id)
        val sportEntity = sportRepository.getSportOrFail(sport)

        participantEntity.sport = sportEntity

        participantRepository.save(participantEntity)
    }

    override fun reParticipate(participant: ParticipantDto, sport: String) {
        if (getParticipationStatus() != ParticipationStatusType.CLOSED) {
            throw IllegalStateException("Participation is not closed. Use ParticipationManager#participate instead")
        }

        val participantEntity = participantRepository.getParticipantOrFail(participant.id)

        // only perform further if sport differs from existing
        if (participantEntity.sport != null && participantEntity.sport?.name == sport) {
            return
        }

        participantEntity.sport = sportRepository.getSportOrFail(sport)
        participantRepository.save(participantEntity)

        if (sport == ATHLETICS) {
            participantEntity.saveAsCompetitor()
        } else {
            val competitor = competitorRepository.findByParticipantId(participant.id)

            competitor.ifPresent {
                competitorRepository.delete(it)
            }
        }
    }

    override fun closeParticipation() {
        if (getParticipationStatus() == ParticipationStatusType.CLOSED) {
            return
        }

        val participants = participantRepository.findBySportName(ATHLETICS)
        participants.forEach { it.saveAsCompetitor() }

        val participation = participationRepository.getParticipationOrFail().apply {
            status = ParticipationStatusType.CLOSED.name
        }

        participationRepository.save(participation)
    }

    override fun resetParticipation() {
        participantRepository.deleteAll()
        groupRepository.deleteAll()

        // TODO: Restart auto increment
        val participation = participationRepository.getParticipationOrFail().apply {
            status = ParticipationStatusType.OPEN.name
        }

        participationRepository.save(participation)
    }

    override fun getParticipationStatus(): ParticipationStatusType =
        participationRepository.getParticipationOrFail().statusType

    override fun getSportTypes(): List<String> {
        return sportRepository.findAll().map { it.name }
    }

    private fun ParticipantRepository.getParticipantOrFail(id: Int): ParticipantEntity {
        return this.findById(id)
            .orElseThrow { NoSuchElementException("Could not find participant: id=$id") }
    }

    private fun SportRepository.getSportOrFail(sport: String): SportEntity {
        return this.findById(sport)
            .orElseThrow { NoSuchElementException("Could not find sport: name=$sport") }
    }

    private fun ParticipationRepository.getParticipationOrFail(): ParticipationEntity {
        return this.findById(MAIN_PARTICIPATION)
            .orElseThrow { IllegalStateException("No participation status could be found. There MUST be a status. Check your database.") }
    }

    private fun ParticipantEntity.saveAsCompetitor() {
        val competitor = competitorRepository.save(CompetitorEntity(participant = this))
        competitor.createDefaultResults()
        competitorRepository.save(competitor)
    }

    private fun CompetitorEntity.createDefaultResults() {
        val disciplines = disciplineRepository.findAll()

        this.results = disciplines.map { discipline ->
            ResultEntity(
                distance = categoryRuleBook.getDistance(CategoryModel(ageOf(this), discipline.name)),
                discipline = discipline,
                value = 1.toLong() * discipline.unit.factor
            ).also {
                it.competitor = this
            }
        }.toSet()
    }
}

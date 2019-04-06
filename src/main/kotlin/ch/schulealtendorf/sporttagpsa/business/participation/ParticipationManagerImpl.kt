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

import ch.schulealtendorf.psa.dto.ParticipantDto
import ch.schulealtendorf.psa.dto.ParticipationStatusDto
import ch.schulealtendorf.psa.dto.SportDto
import ch.schulealtendorf.psa.shared.reporting.rulebook.CategoryModel
import ch.schulealtendorf.psa.shared.reporting.rulebook.CategoryRuleBook
import ch.schulealtendorf.sporttagpsa.business.database.DatabaseReset
import ch.schulealtendorf.sporttagpsa.entity.CompetitorEntity
import ch.schulealtendorf.sporttagpsa.entity.MAIN_PARTICIPATION
import ch.schulealtendorf.sporttagpsa.entity.ParticipantEntity
import ch.schulealtendorf.sporttagpsa.entity.ParticipationEntity
import ch.schulealtendorf.sporttagpsa.entity.ResultEntity
import ch.schulealtendorf.sporttagpsa.entity.SportEntity
import ch.schulealtendorf.sporttagpsa.repository.CompetitorRepository
import ch.schulealtendorf.sporttagpsa.repository.DisciplineRepository
import ch.schulealtendorf.sporttagpsa.repository.ParticipantRepository
import ch.schulealtendorf.sporttagpsa.repository.ParticipationRepository
import org.joda.time.DateTime
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
        private val disciplineRepository: DisciplineRepository,
        private val databaseReset: DatabaseReset
) : ParticipationManager {

    /**
     * Marks the given {@code participant} as absent.
     *
     * @param participant the participant to mark as absent
     *
     * @throws NoSuchElementException if the given participant could not be found
     */
    override fun markAsAbsent(participant: ParticipantDto) {

        val participantEntity = participantRepository.findById(participant.id)
                .orElseThrow { NoSuchElementException("Could not found participant: id=${participant.id}") }

        participantEntity.absent = true

        participantRepository.save(participantEntity)
    }

    /**
     * Marks the given {@code participant} as present.
     *
     * @param participant the participant to mark as absent
     *
     * @throws NoSuchElementException if the given participant could not be found
     */
    override fun markAsPresent(participant: ParticipantDto) {

        val participantEntity = participantRepository.findById(participant.id)
                .orElseThrow { NoSuchElementException("Could not found participant: id=${participant.id}") }

        participantEntity.absent = false

        participantRepository.save(participantEntity)
    }

    /**
     * Sets the given {@code sport} to the given {@code participant}.
     *
     * This operation can not be performed, if the {@link ParticipationManager#getParticipationStatus}equals {@link ParticipationStatus#CLOSE}.
     * In order to change the sport of a participant, use the {@link ParticipationManager#reParticipate} method.
     *
     * @param participant the participant to set the sport on
     * @param sport the sport to set on the participant
     *
     * @throws NoSuchElementException if the given participant or sport could not be found
     * @throws IllegalStateException if the participation is already closed
     */
    override fun participate(participant: ParticipantDto, sport: SportDto) {

        if (getParticipationStatus() == ParticipationStatusDto.CLOSE) {
            throw IllegalStateException("Participation already closed. Use ParticipationManager#reParticipate instead")
        }

        val participantEntity = participantRepository.findById(participant.id)
                .orElseThrow { NoSuchElementException("Could not find participant: id=${participant.id}") }

        participantEntity.sport = SportEntity(sport.name)

        participantRepository.save(participantEntity)
    }

    /**
     * Sets the given {@code sport} to the given {@code participant}.
     *
     * In contrast to the {@link ParticipationManager#participate} method, this operation will
     * consider the participation status.
     *
     * If the {@link ParticipationManager#getParticipationStatus} equals {@link ParticipationStatus#CLOSE},
     * and the given {@code sport} equals athletics, the participant will be saved as a competitor.
     *
     * If the {@link ParticipationManager#getParticipationStatus} equals {@link ParticipationStatus#CLOSE},
     * and the given {@code participant} is already a competitor, but the given {@code sport} is not athletics,
     * the competitor will be removed.
     *
     * The {@link ParticipationManager#getParticipationStatus} must be equal to {@link ParticipationStatus#CLOSE}
     * in order to perform this operation. Otherwise use the {@link ParticipationManager#participate} method.
     *
     * @param participant the participant to set the sport on
     * @param sport the sport to set on the participant
     *
     * @throws NoSuchElementException if the given participant could not be found
     * @throws IllegalStateException if the participation status is not CLOSE.
     */
    override fun reParticipate(participant: ParticipantDto, sport: SportDto) {

        if (getParticipationStatus() != ParticipationStatusDto.CLOSE) {
            throw IllegalStateException("Participation is open. Use ParticipationManager#participate instead")
        }

        val participantEntity = participantRepository.findById(participant.id)
                .orElseThrow { NoSuchElementException("Could not find participant: id=${participant.id}") }

        // only perform further if any changes are made
        if (participantEntity.sport != null && participantEntity.sport?.name == sport.name) {
            return
        }

        participantEntity.sport = SportEntity(sport.name)
        participantRepository.save(participantEntity)

        if (sport.name == ATHLETICS) {
            participantEntity.saveAsCompetitor()
        } else {

            val competitor = competitorRepository.findByParticipantId(participant.id)

            competitor.ifPresent {
                competitorRepository.delete(it)
            }
        }
    }

    /**
     * Closes the participation. {@link ParticipationManager#getParticipationStatus} will always
     * return {@link ParticipationStatus#CLOSE} until {@link ParticipationManager#resetParticipation}
     * will be invoked.
     *
     * This operation looks up all participant who participates in the sport athletics
     * and saves them as {@link Competitor} with default results of all available disciplines.
     *
     * If the participation is already closed, this method performs not any further.
     */
    override fun closeParticipation() {

        if (getParticipationStatus() == ParticipationStatusDto.CLOSE) {
            return
        }

        val participants = participantRepository.findBySportName(ATHLETICS)

        participants.forEach { it.saveAsCompetitor() }

        val participation = ParticipationEntity(MAIN_PARTICIPATION, ParticipationStatusDto.CLOSE.name)
        participationRepository.save(participation)
    }

    /**
     * Resets the participation. All recorded data will be DELETED.
     *
     * {@link ParticipationManager#getParticipationStatus} will always return {@link ParticipationStatus#OPEN}
     * until {@link ParticipationManager#closeParticipation} will be invoked.
     */
    override fun resetParticipation() {

        databaseReset.run()
        val participation = ParticipationEntity(MAIN_PARTICIPATION, ParticipationStatusDto.OPEN.name)
        participationRepository.save(participation)
    }

    /**
     * @return the participation status
     * @throws IllegalStateException if no participation status could be found
     */
    override fun getParticipationStatus(): ParticipationStatusDto {

        val participation = participationRepository.findById(MAIN_PARTICIPATION)
                .orElseThrow { IllegalStateException("No participation status could be found. There MUST be a status. Check your database.") }

        return ParticipationStatusDto.valueOf(participation.status)
    }

    private fun ParticipantEntity.saveAsCompetitor() {
        val competitor = competitorRepository.save(CompetitorEntity(participant = this))
        competitor.createDefaultResults()
        competitorRepository.save(competitor)
    }

    private fun CompetitorEntity.createDefaultResults() {

        val disciplines = disciplineRepository.findAll()

        this.results = disciplines.map {
            ResultEntity(
                    distance = categoryRuleBook.getDistance(CategoryModel(this.age(), it.name)),
                    discipline = it,
                    value = 1.toLong() * it.unit.factor
            ).also {
                it.competitor = this
            }
        }.toSet()
    }

    private fun CompetitorEntity.age(): Int {
        return DateTime.now().year - DateTime(participant.birthday).year
    }
}
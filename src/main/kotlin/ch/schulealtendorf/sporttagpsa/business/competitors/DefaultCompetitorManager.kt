/*
 * Copyright (c) 2017 by Nicolas Märchy
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

package ch.schulealtendorf.sporttagpsa.business.competitors

import ch.schulealtendorf.sporttagpsa.business.participation.ParticipationStatus
import ch.schulealtendorf.sporttagpsa.entity.*
import ch.schulealtendorf.sporttagpsa.model.*
import ch.schulealtendorf.sporttagpsa.repository.AbsentCompetitorRepository
import ch.schulealtendorf.sporttagpsa.repository.CompetitorRepository
import ch.schulealtendorf.sporttagpsa.repository.SportRepository
import org.springframework.stereotype.Component
import java.util.*

/**
 * Provider for competitors.
 * 
 * @author nmaerchy
 * @version 1.1.0
 */
@Component
class DefaultCompetitorManager(
        private val competitorRepository: CompetitorRepository,
        private val sportRepository: SportRepository,
        private val participationStatus: ParticipationStatus,
        private val absentCompetitorRepository: AbsentCompetitorRepository
): CompetitorManager {

    /**
     * Gets all competitors that belong to the clazz with the passed in argument.
     * 
     * @param clazzId the id of the clazz wanted
     * @return a list of found competitors
     */
    override fun getCompetitorsByClazz(clazzId: Int): List<SimpleCompetitorModel> {
        return competitorRepository.findByClazzId(clazzId).map { it.map() }
    }

    /**
     * Get all competitors that belongs the the class matching the given {@code clazzId}.
     *
     * If the {@code clazzId} does not exists, an empty list will be returned.
     *
     * @param clazzId the id of the class
     *
     * @return a list of competitors
     */
    override fun getCompetitorListByClazz(clazzId: Int): List<Competitor> {

        val absentCompetitorList = absentCompetitorRepository.findAll()

        return competitorRepository.findByClazzId(clazzId)
                .map {
                    Competitor(
                            it.id!!,
                            it.surname,
                            it.prename,
                            Gender(it.gender),
                            Birthday(it.birthday),
                            it.address,
                            absentCompetitorList.any { absent -> absent.competitor.id == it.id },
                            Optional.ofNullable(it.sport())
                    )
                }
    }

    /**
     * Returns a {@link SimpleCompetitor} matching the given {@code id}.
     *
     * @param id id of the competitor
     *
     * @return the resulting competitor
     * @throws CompetitorNotFoundException if the given {@code id} does no exists
     */
    override fun getCompetitor(id: Int): SimpleCompetitor {

        val competitor: CompetitorEntity = competitorRepository.findOne(id) ?: throw CompetitorNotFoundException("Could not found competitor: id=$id")

        return SimpleCompetitor(
                competitor.id!!,
                competitor.surname,
                competitor.prename,
                Gender(competitor.gender),
                competitor.address
        )
    }

    /**
     * Saves the given {@code competitor}.
     *
     * @param competitor competitor data to save
     */
    override fun saveCompetitor(competitor: SimpleCompetitor) {

        val competitorEntity: CompetitorEntity = competitorRepository.findOne(competitor.id)!!

        competitorEntity.apply {
            prename = competitor.prename
            surname = competitor.surname
            gender = competitor.gender.value
            address = competitor.address
        }

        competitorRepository.save(competitorEntity)
    }

    /**
     * Sets the sport matching the given {@code sportId}
     * to the competitor matching the given {@code competitorId}.
     *
     * This method considers the {@link ParticipationStatus}.
     *
     * @param competitorId the competitor id to set the sport for
     * @param sportId the sport id to set to the competitor
     *
     * @throws IllegalStateException if the participation is already finished
     */
    override fun setSport(competitorId: Int, sportId: Int) {

        if (participationStatus.isFinished()) {
            throw IllegalStateException("Can not set sport: participation already finished")
        }

        val competitorEntity: CompetitorEntity = competitorRepository.findOne(competitorId)!!
        val sportEntity: SportEntity = sportRepository.findOne(sportId)!!

        competitorEntity.sport = sportEntity

        competitorRepository.save(competitorEntity)
    }

    /**
     * Un-sets the sport of the competitor matching the given {@code competitorId}.
     *
     * This method considers the {@link ParticipationStatus}.
     *
     * @param competitorId the competitor id to set the sport for
     *
     * @throws IllegalStateException if the participation is already finished
     */
    override fun unsetSport(competitorId: Int) {

        if (participationStatus.isFinished()) {
            throw IllegalStateException("Can not un-set sport: participation already finished")
        }

        val competitorEntity: CompetitorEntity = competitorRepository.findOne(competitorId)!!

        competitorEntity.sport = null

        competitorRepository.save(competitorEntity)
    }

    /**
     * Marks the competitor matching the given {@code competitorId} as absent.
     * If the given {@code competitorId} does not exists, this method will do nothing.
     *
     * @param competitorId id of the competitor to mark as absent
     */
    override fun markAsAbsent(competitorId: Int) {

        val competitorEntity = competitorRepository.findOne(competitorId)

        if (competitorEntity != null) {

            val absentCompetitorList = absentCompetitorRepository.findAll()

            if (!absentCompetitorList.any { it.competitor.id == competitorId }) {
                absentCompetitorRepository.save(AbsentCompetitorEntity(null, competitorEntity))
            }
        }
    }

    /**
     * Counter part of {@code markAsAbsent}.
     * Marks the competitor matching the given {@code competitorId} as present.
     * If the given {@code competitorId} does not exists, this method will do nothing.
     *
     * @param competitorId id of the competitor to mark as present
     */
    override fun markAsPresent(competitorId: Int) {

        val absentCompetitor = absentCompetitorRepository.findByCompetitorId(competitorId)

        if (absentCompetitor != null) {
            absentCompetitorRepository.delete(absentCompetitor)
        }
    }

    private fun CompetitorEntity.sport(): Sport? {

        if (sport == null) {
            return null
        }

        return Sport(sport!!.id!!, sport!!.name)
    }
}
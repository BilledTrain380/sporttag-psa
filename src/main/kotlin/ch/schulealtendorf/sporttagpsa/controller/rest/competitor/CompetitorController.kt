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

package ch.schulealtendorf.sporttagpsa.controller.rest.competitor

import ch.schulealtendorf.sporttagpsa.business.athletics.CompetitorManager
import ch.schulealtendorf.sporttagpsa.business.athletics.ResultCalculator
import ch.schulealtendorf.sporttagpsa.business.athletics.TemporaryResult
import ch.schulealtendorf.sporttagpsa.business.group.GroupManager
import ch.schulealtendorf.sporttagpsa.controller.rest.*
import ch.schulealtendorf.sporttagpsa.model.Gender
import org.springframework.web.bind.annotation.*
import java.util.*

/**
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 0.0.1
 */
@RestController
class CompetitorController(
        private val competitorManager: CompetitorManager,
        private val groupManager: GroupManager,
        private val resultCalculator: ResultCalculator
) {

    @GetMapping("/competitors")
    fun getCompetitors(@RequestParam("group", required = false) groupName: String?, @RequestParam("gender", required = false) gender: Gender?): List<RestCompetitor> {

        if (groupName == null && gender == null) {
            return competitorManager.getCompetitorList().map { json(it) }
        }

        if (groupName != null && gender == null) {
            val group = groupManager.getGroup(groupName).get()
            return competitorManager.getCompetitorList(group).map { json(it) }
        }

        if (gender != null && groupName == null) {
            return competitorManager.getCompetitorList(gender).map { json(it) }
        }

        if (groupName != null && gender != null) {
            val group = groupManager.getGroup(groupName).get()
            return competitorManager.getCompetitorList(group, gender).map { json(it) }
        }

        return listOf()
    }

    @GetMapping("/competitor/{competitor_id}")
    fun getCompetitor(@PathVariable("competitor_id") id: Int): RestCompetitor {
        return competitorManager.getCompetitor(id).map { json(it) }
                .orElseThrow { NotFoundException("Competitor does not exist: id=$id") }
    }

    @PutMapping("/competitor/{competitor_id}")
    fun updateResults(@PathVariable("competitor_id") id: Int, @RequestBody resultsWrapper: ResultWrapper): List<RestResult> {

        var competitor = competitorManager.getCompetitor(id)
                .orElseThrow { NotFoundException("Competitor does not exist: id=$id") }

        val results = resultsWrapper.results
                .map {

                    val tempResult = TemporaryResult(
                            it.id,
                            competitor.gender,
                            it.value,
                            it.discipline,
                            Optional.ofNullable(it.distance)
                    )

                    resultCalculator.calculate(tempResult)
                }

        competitor = competitorManager.mergeResults(competitor, results)
        competitorManager.saveCompetitorResults(competitor)

        return results.map { json(it) }
    }
}
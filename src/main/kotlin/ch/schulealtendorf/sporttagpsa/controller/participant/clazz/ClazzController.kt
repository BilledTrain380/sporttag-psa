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

package ch.schulealtendorf.sporttagpsa.controller.participant.clazz

import ch.schulealtendorf.sporttagpsa.business.competitors.CompetitorManager
import ch.schulealtendorf.sporttagpsa.business.competitors.SimpleCompetitorModel
import ch.schulealtendorf.sporttagpsa.business.competitors.SimpleSportModel
import ch.schulealtendorf.sporttagpsa.business.participation.ParticipationStatus
import ch.schulealtendorf.sporttagpsa.business.provider.ClazzProvider
import ch.schulealtendorf.sporttagpsa.business.provider.SportProvider
import ch.schulealtendorf.sporttagpsa.model.SimpleCompetitor
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import javax.validation.Valid

@Controller
@RequestMapping("/participant/clazz")
class ClazzController(
        private val competitorManager: CompetitorManager,
        private val participationStatus: ParticipationStatus,
        private val clazzProvider: ClazzProvider,
        sportProvider: SportProvider
) {

    private val sports = sportProvider.getAll()
            .map { ParticipantSport(it.id, it.name) }
    
    @GetMapping
    fun getClazzList(model: Model): String {

        model.addAttribute("clazzes", clazzProvider.getAll())

        return "participant/clazz/clazz-list"
    }

    @GetMapping("/{id}")
    fun getClazz(@PathVariable id: Int, model: Model): String {

        val competitorForm = ParticipantForm(
                competitorManager.getCompetitorsByClazz(id)
                        .map { Participant(it.id, it.surname, it.prename, it.gender, it.address, it.sport.toParticipantSport()) }
        )
        
        model.addAttribute("clazz", clazzProvider.getOne(id))
        model.addAttribute("sports", sports)
        model.addAttribute("participationStatus", participationStatus.isFinished())
        model.addAttribute("participantForm", competitorForm)

        return "participant/clazz/clazz-detail"
    }

    @PostMapping("/{id}")
    fun setSport(@PathVariable id: Int, @Valid @ModelAttribute("participantForm") competitorForm: ParticipantForm, redirectAttributes: RedirectAttributes): String {

        competitorForm.competitors
                .forEach {

                    if (it.sport.id == 0) {
                        competitorManager.unsetSport(it.id)
                    } else {
                        competitorManager.setSport(it.id, it.sport.id)
                    }
                }

        redirectAttributes.addFlashAttribute("success", true)

        return "redirect:/participant/clazz/$id"
    }
    
    private fun SimpleSportModel?.toParticipantSport(): ParticipantSport {
        if (this == null) {
            return ParticipantSport()
        }
        
        return ParticipantSport(id, name)
    }
}
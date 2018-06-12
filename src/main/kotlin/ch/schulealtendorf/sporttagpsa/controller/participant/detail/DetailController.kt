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

package ch.schulealtendorf.sporttagpsa.controller.participant.detail

import ch.schulealtendorf.sporttagpsa.business.participation.ParticipationManager
import ch.schulealtendorf.sporttagpsa.model.Gender
import ch.schulealtendorf.sporttagpsa.model.SingleParticipant
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import javax.validation.Valid

@Controller
@RequestMapping("/participant/detail")
class DetailController(
        private val participationManager: ParticipationManager
) {

    @GetMapping("/{id}")
    fun getCompetitor(@PathVariable id: Int, model: Model): String {

        val participant = participationManager.getParticipant(id)

        val participantModel = SingleParticipantModel(
                participant.id,
                participant.surname,
                participant.prename,
                participant.gender.value,
                participant.address
        )

        model.addAttribute("participant", participantModel)
        
        return "participant/detail/competitor-detail"
    }

    @PostMapping("/{id}")
    fun updateCompetitor(@PathVariable id: Int, @Valid @ModelAttribute("participantModel") participantModel: SingleParticipantModel, redirectAttributes: RedirectAttributes): String {

        val participant = SingleParticipant(
                participantModel.id,
                participantModel.surname,
                participantModel.prename,
                Gender(participantModel.gender),
                participantModel.address
        )

        participationManager.updateParticipant(participant)
        
        redirectAttributes.addFlashAttribute("success", true)
        
        return "redirect:/participant/detail/$id"
    }
}
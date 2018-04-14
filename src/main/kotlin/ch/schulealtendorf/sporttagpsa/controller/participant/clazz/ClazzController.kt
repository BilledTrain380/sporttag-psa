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

import ch.schulealtendorf.sporttagpsa.business.participation.ParticipationManager
import ch.schulealtendorf.sporttagpsa.business.participation.ParticipationStatus
import ch.schulealtendorf.sporttagpsa.business.provider.ClazzProvider
import ch.schulealtendorf.sporttagpsa.business.provider.SportProvider
import ch.schulealtendorf.sporttagpsa.model.*
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import javax.validation.Valid

@Controller
@RequestMapping("/participant/clazz")
class ClazzController(
        private val participationStatus: ParticipationStatus,
        private val clazzProvider: ClazzProvider,
        private val participationManager: ParticipationManager,
        sportProvider: SportProvider
) {

    private val sports = sportProvider.getAll()
    
    @GetMapping
    fun getClazzList(model: Model): String {

        val clazzes = clazzProvider.getAll()
                .map {
                    ClazzStatus(
                            it.id,
                            it.name,
                            participationManager.getParticipantListByClazz(it).all { it.sport.isPresent }
                    )
                }

        model.addAttribute("clazzes", clazzes)

        return "participant/clazz/clazz-list"
    }

    @GetMapping("/{id}")
    fun getClazz(@PathVariable id: Int, model: Model): String {

        val clazz = clazzProvider.getOne(id)

        val participantForm = ParticipantForm(
                participationManager.getParticipantListByClazz(clazz)
                        .map { ParticipantModel(
                                it.id,
                                it.surname,
                                it.prename,
                                it.gender.value,
                                it.address,
                                it.sport.map { ParticipantSportModel(it.id, it.name) }.orElse(ParticipantSportModel(0, "")),
                                it.absent)
                        }
        )
        
        model.addAttribute("clazz", clazzProvider.getOne(id))
        model.addAttribute("sports", sports)
        model.addAttribute("participationStatus", participationStatus.isFinished())
        model.addAttribute("participantForm", participantForm)

        return "participant/clazz/clazz-detail"
    }

    @PostMapping("/{id}")
    fun setSport(@PathVariable id: Int, @Valid @ModelAttribute("participantForm") participantForm: ParticipantForm, redirectAttributes: RedirectAttributes): String {

        participantForm.participantModelList
                .filter { it.sport.id != 0 } // only use participant that has a sport
                .forEach {

                    val participant = SingleParticipant(
                            it.id,
                            it.surname,
                            it.prename,
                            Gender(it.gender),
                            it.address
                    )

                    participationManager.setSport(participant, sports.single { sport -> it.sport.id == sport.id })

                    if (it.absent) {
                        participationManager.markAsAbsent(participant)
                    } else {
                        participationManager.markAsPresent(participant)
                    }
                }

        redirectAttributes.addFlashAttribute("success", true)

        return "redirect:/participant/clazz/$id"
    }
}
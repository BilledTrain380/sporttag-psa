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

package ch.schulealtendorf.sporttagpsa.controller.participant.management

import ch.schulealtendorf.sporttagpsa.business.competitors.CompetitorConsumer
import ch.schulealtendorf.sporttagpsa.business.parsing.FlatCompetitor
import ch.schulealtendorf.sporttagpsa.business.participation.ParticipationManager
import ch.schulealtendorf.sporttagpsa.business.provider.ClazzProvider
import ch.schulealtendorf.sporttagpsa.business.provider.SportProvider
import ch.schulealtendorf.sporttagpsa.model.SingleParticipant
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.validation.Valid

/**
 * @author nmaerchy
 * @version 0.0.1
 */
@Controller
@RequestMapping("/participant/management/late-registration")
class LateRegistrationController(
        private val competitorConsumer: CompetitorConsumer,
        private val participationManager: ParticipationManager,
        private val clazzProvider: ClazzProvider,
        private val sportProvider: SportProvider
) {

    @GetMapping
    fun index(model: Model): String {

        model.addAttribute("sports", sportProvider.getAll())
        model.addAttribute("clazzes", clazzProvider.getAll())
        model.addAttribute("participant", LateRegistrationParticipant())

        return "participant/management/late-registration"
    }

    @PostMapping
    fun saveParticipant(@Valid @ModelAttribute("participant") participantForm: LateRegistrationParticipant, redirectedAttributes: RedirectAttributes): String {

        return try {

            val clazz = clazzProvider.getAll().single { it.id == participantForm.clazzId }

            val flatCompetitor = FlatCompetitor(
                    participantForm.surname,
                    participantForm.prename,
                    participantForm.gender,
                    participantForm.birthday.parseDate(),
                    participantForm.address,
                    participantForm.zipCode,
                    participantForm.town,
                    clazz.name,
                    clazz.teacher
            )

            competitorConsumer.accept(flatCompetitor)

            val participant = participationManager.getParticipantListByClazz(clazz)
                    .filter {
                        it.prename == participantForm.prename
                                && it.surname == participantForm.surname
                                && it.gender.value == participantForm.gender
                                && it.address == participantForm.address
                    }.map { SingleParticipant(it.id, it.surname, it.prename, it.gender, it.address) }
                    .first()

            val sport = sportProvider.getAll().first { it.id == participantForm.sportId }

            participationManager.setSport(participant, sport)

            redirectedAttributes.addFlashAttribute("success", true)

            "redirect:/participant/management/late-registration"

        } catch (ex: ParseException) {

            redirectedAttributes.addFlashAttribute("invalidForm", "invalid-date")

            "redirect:/participant/management/late-registration"
        }
    }

    private fun String.parseDate(): Date {
        val format: DateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN)
        return format.parse(this)
    }
}
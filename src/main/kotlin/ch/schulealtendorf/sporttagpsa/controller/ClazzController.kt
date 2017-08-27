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

package ch.schulealtendorf.sporttagpsa.controller

import ch.schulealtendorf.sporttagpsa.business.competitors.CompetitorProvider
import ch.schulealtendorf.sporttagpsa.business.participation.ParticipationStatus
import ch.schulealtendorf.sporttagpsa.controller.model.SimpleCompetitorFomModel
import ch.schulealtendorf.sporttagpsa.entity.map
import ch.schulealtendorf.sporttagpsa.repository.ClazzRepository
import ch.schulealtendorf.sporttagpsa.repository.SportRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import javax.validation.Valid

/**
 * @author nmaerchy
 * @version 0.0.1
 */
@Controller
class ClazzController(
        private val clazzRepository: ClazzRepository,
        private val sportRepository: SportRepository,
        private val competitorProvider: CompetitorProvider,
        private val participationStatus: ParticipationStatus
) {
    
    
    companion object {
        const val CLAZZ: String = "/clazz"
    }

    @GetMapping(CLAZZ)
    fun getClazzList(model: Model): String {

        model.addAttribute("clazzes", clazzRepository.findAll())

        return "competitor/clazz-list"
    }

    @GetMapping("$CLAZZ/{id}")
    fun getClazz(@PathVariable id: Int, model: Model): String {

        // TODO: May use a clazz and sport provider class
        model.addAttribute("clazz", clazzRepository.findOne(id))
        model.addAttribute("sports", sportRepository.findAll().map { it?.map() })
        model.addAttribute("participationStatus", participationStatus.isFinished())
        model.addAttribute("competitorSportForm", SimpleCompetitorFomModel(competitorProvider.getCompetitorsByClazz(id)))

        return "competitor/class-detail"
    }

    @PostMapping("$CLAZZ/{id}")
    fun updateCompetitors(@PathVariable id: Int, @Valid @ModelAttribute("competitorSportForm") competitorForm: SimpleCompetitorFomModel, redirectAttributes: RedirectAttributes): String {

        competitorForm.competitors.forEach(competitorProvider::updateCompetitor)

        redirectAttributes.addFlashAttribute("success", true)

        return "redirect:$CLAZZ/$id"
    }
}
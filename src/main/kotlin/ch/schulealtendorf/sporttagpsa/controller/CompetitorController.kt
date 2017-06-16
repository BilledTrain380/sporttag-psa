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

import ch.schulealtendorf.sporttagpsa.competitors.CompetitorProvider
import ch.schulealtendorf.sporttagpsa.entity.map
import ch.schulealtendorf.sporttagpsa.model.SimpleCompetitorFomModel
import ch.schulealtendorf.sporttagpsa.model.SimpleCompetitorModel
import ch.schulealtendorf.sporttagpsa.repository.ClazzRepository
import ch.schulealtendorf.sporttagpsa.repository.SportRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import javax.validation.Valid

/**
 * Controller for GET and PATCH competitors.
 * 
 * @author nmaerchy
 * @version 0.0.2
 */
@Controller
@RequestMapping("/competitor")
class CompetitorController(
        private val clazzRepository: ClazzRepository,
        private val sportRepository: SportRepository,
        private val competitorProvider: CompetitorProvider
) {

    @GetMapping("/clazz")
    fun clazzList(model: Model): String {

        model.addAttribute("clazzes", clazzRepository.findAll())
        
        return "competitor/clazz-list"
    }

    // TODO: Move class depending methods to a own controller
    
    @GetMapping("/clazz/{id}")
    fun clazz(@PathVariable id: Int, model: Model): String {
        
        // TODO: May use a clazz and sport provider class
        model.addAttribute("clazz", clazzRepository.findOne(id))
        model.addAttribute("sports", sportRepository.findAll().map { it?.map() })
        model.addAttribute("competitorSportForm", SimpleCompetitorFomModel(competitorProvider.getCompetitorsByClazz(id)))
        
        return "competitor/class-detail"
    }

    @PostMapping("/clazz/{id}")
    fun updateCompetitors(@PathVariable id: Int, @Valid @ModelAttribute("competitorSportForm") competitorForm: SimpleCompetitorFomModel, redirectAttributes: RedirectAttributes): String {
        
        competitorForm.competitors.forEach(competitorProvider::updateCompetitor)
        
        // TODO: use message in html and just add true or false
        redirectAttributes.addFlashAttribute("messageSuccess", "Änderungen wurden erfolgreich übernommen")
        
        return "redirect:/competitor/clazz/$id"
    }

    @GetMapping("/{id}")
    fun getCompetitor(@PathVariable id: Int, model: Model): String {
        
        model.addAttribute("competitor", competitorProvider.getCompetitorById(id))
        
        return "competitor/competitor-detail"
    }

    @PostMapping("/{id}")
    fun updateCompetitor(@PathVariable id: Int, @Valid @ModelAttribute("competitor") competitor: SimpleCompetitorModel, redirectAttributes: RedirectAttributes): String {
        
        competitorProvider.updateCompetitor(competitor)

        // TODO: use message in html and just add true or false
        redirectAttributes.addFlashAttribute("messageSuccess", "Änderungen wurden erfolgreich übernommen")
        
        return "redirect:/competitor/$id"
    }
}
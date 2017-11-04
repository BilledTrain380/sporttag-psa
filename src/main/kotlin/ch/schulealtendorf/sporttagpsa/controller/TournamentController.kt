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

import ch.schulealtendorf.sporttagpsa.business.participation.ParticipationStatus
import ch.schulealtendorf.sporttagpsa.business.provider.ClazzProvider
import ch.schulealtendorf.sporttagpsa.business.provider.DisciplineProvider
import ch.schulealtendorf.sporttagpsa.business.rulebook.FormulaModel
import ch.schulealtendorf.sporttagpsa.business.rulebook.PSARuleBook
import ch.schulealtendorf.sporttagpsa.business.tournament.TournamentFilter
import ch.schulealtendorf.sporttagpsa.business.tournament.TournamentProvider
import ch.schulealtendorf.sporttagpsa.controller.model.ClazzModel
import ch.schulealtendorf.sporttagpsa.controller.model.DisciplineModel
import ch.schulealtendorf.sporttagpsa.controller.model.TournamentCompetitorFormModel
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import javax.validation.Valid

/**
 * Controller for the tournament.
 * 
 * @author nmaerchy
 * @version 1.0.0
 */
@Controller
class TournamentController(
        private val tournamentProvider: TournamentProvider,
        private val ruleBook: PSARuleBook,
        private val disciplineProvider: DisciplineProvider,
        private val clazzProvider: ClazzProvider,
        private val participationStatus: ParticipationStatus
) {
    
    companion object {
        const val TOURNAMENT = "/tournament"
    }
    
    @GetMapping("$TOURNAMENT/init")
    fun tournament(): String {
        
        val disciplineId = disciplineProvider.getAll().first().id
        val clazzId = clazzProvider.getAll().first().id
        
        // TODO: show error page if no clazz exist yet
        
        return "redirect:$TOURNAMENT?discipline_id=$disciplineId&clazz_id=$clazzId&gender=true"
    }
    
    @GetMapping(TOURNAMENT)
    fun tournament(@RequestParam("discipline_id") disciplineId: Int, @RequestParam("clazz_id") clazzId: Int, @RequestParam("gender") gender: Boolean, model: Model): String {
        
        val disciplineList: List<DisciplineModel> = disciplineProvider.getAll().map { DisciplineModel(it.id, it.name) }
        val clazzList: List<ClazzModel> = clazzProvider.getAll().map { ClazzModel(it.id, it.name) }
        
        val filter = TournamentFilter(disciplineId, clazzId, gender)
        val results = tournamentProvider.findByFilter(filter)
        
        val currentDiscipline = disciplineList.first { it.id == disciplineId }
        val currentClazz = clazzList.first { it.id == clazzId }
        
        val formModel = TournamentCompetitorFormModel(
                currentDiscipline,
                currentClazz,
                gender,
                results
        )
        
        model.addAttribute("tournamentForm", formModel)
        model.addAttribute("disciplines", disciplineList)
        model.addAttribute("clazzList", clazzList)
        model.addAttribute("participationStatus", !participationStatus.isFinished())
        
        return "tournament"
    }
    
    @PostMapping(TOURNAMENT)
    fun saveResults(@Valid @ModelAttribute tournamentModel: TournamentCompetitorFormModel, redirectAttributes: RedirectAttributes): String {
        
        tournamentModel.competitors.forEach { 
            
            val ruleModel = FormulaModel(
                    tournamentModel.discipline.name,
                    it.distance,
                    it.result,
                    it.gender
            )
  
            it.points = ruleBook.run(ruleModel) ?: 1
            
            tournamentProvider.updateResult(it)
        }
        
        val discipline = tournamentModel.discipline.id
        val clazz = tournamentModel.clazz.id
        val gender = tournamentModel.gender

        redirectAttributes.addFlashAttribute("success", true)
        
        return "redirect:$TOURNAMENT?discipline_id=$discipline&clazz_id=$clazz&gender=$gender"
    }
}
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

package ch.schulealtendorf.sporttagpsa.controller.tournament

import ch.schulealtendorf.sporttagpsa.business.participation.ParticipationStatus
import ch.schulealtendorf.sporttagpsa.business.provider.ClazzProvider
import ch.schulealtendorf.sporttagpsa.business.provider.DisciplineProvider
import ch.schulealtendorf.sporttagpsa.business.rulebook.FormulaModel
import ch.schulealtendorf.sporttagpsa.business.rulebook.ResultRuleBook
import ch.schulealtendorf.sporttagpsa.business.tournament.TournamentCompetitor
import ch.schulealtendorf.sporttagpsa.business.tournament.TournamentFilter
import ch.schulealtendorf.sporttagpsa.business.tournament.TournamentProvider
import ch.schulealtendorf.sporttagpsa.business.tournament.TournamentResult
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import javax.validation.Valid

/**
 * Controller for the tournament.
 * 
 * @author nmaerchy
 * @version 1.0.1
 */
@Controller
@RequestMapping("/tournament/result")
class TournamentController(
        private val tournamentProvider: TournamentProvider,
        private val ruleBook: ResultRuleBook,
        private val disciplineProvider: DisciplineProvider,
        private val clazzProvider: ClazzProvider,
        private val participationStatus: ParticipationStatus
) {
    
    @GetMapping("/index")
    fun index(model: Model): String {
        
        val clazzes = clazzProvider.getAll()
        
        if (clazzes.isEmpty()) {
            
            model.addAttribute("missingClazzes", true)
            
            return "tournament/result"
        }
        
        val disciplineId = disciplineProvider.getAll().first().id
        val clazzId = clazzes.first().id
        
        return "redirect:/tournament/result?discipline_id=$disciplineId&clazz_id=$clazzId&gender=true"
    }
    
    @GetMapping("/")
    fun tournament(@RequestParam("discipline_id") disciplineId: Int, @RequestParam("clazz_id") clazzId: Int, @RequestParam("gender") gender: Boolean, model: Model): String {
        
        val disciplineList: List<TournamentDisciplineModel> = disciplineProvider.getAll().map { TournamentDisciplineModel(it.id, it.name) }
        val clazzList: List<TournamentClazzModel> = clazzProvider.getAll().map { TournamentClazzModel(it.id, it.name) }
        
        val filter = TournamentFilter(disciplineId, clazzId, gender)
        val results = tournamentProvider.findByFilter(filter).map { it.toModel() }
        
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
        
        return "tournament/result"
    }
    
    @PostMapping("/save")
    fun saveResults(@Valid @ModelAttribute tournamentModel: TournamentCompetitorFormModel, redirectAttributes: RedirectAttributes): String {
        
        tournamentModel.competitors.forEach { 
            
            val ruleModel = FormulaModel(
                    tournamentModel.discipline.name,
                    it.distance,
                    it.result,
                    it.gender
            )
  
            it.points = ruleBook.calc(ruleModel)
            
            tournamentProvider.updateResult(it.toResult())
        }
        
        val discipline = tournamentModel.discipline.id
        val clazz = tournamentModel.clazz.id
        val gender = tournamentModel.gender

        redirectAttributes.addFlashAttribute("success", true)
        
        return "redirect:/tournament/result?discipline_id=$discipline&clazz_id=$clazz&gender=$gender"
    }
    
    private fun TournamentCompetitor.toModel(): TournamentCompetitorModel {
        return TournamentCompetitorModel(
                startNumber,
                resultId,
                prename,
                surname,
                gender,
                distance,
                result,
                unit,
                points
        )
    }
    
    private fun TournamentCompetitorModel.toResult(): TournamentResult {
        return TournamentResult(
                resultId,
                result,
                points
        )
    }
}
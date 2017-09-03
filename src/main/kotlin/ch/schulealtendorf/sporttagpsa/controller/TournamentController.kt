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

import ch.schulealtendorf.sporttagpsa.controller.model.ClazzModel
import ch.schulealtendorf.sporttagpsa.controller.model.SportModel
import ch.schulealtendorf.sporttagpsa.controller.model.TournamentCompetitorFormModel
import ch.schulealtendorf.sporttagpsa.controller.model.TournamentCompetitorModel
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

/**
 * @author nmaerchy
 * @version 0.0.1
 */
@Controller
class TournamentController {
    
    companion object {
        const val TOURNAMENT = "/tournament"
    }
    
    @GetMapping("$TOURNAMENT/init")
    fun tournament(): String {
        return "redirect:$TOURNAMENT?sport_id=1&clazz_id=1&gender=true"
    }
    
    @GetMapping(TOURNAMENT)
    fun tournament(@RequestParam("sport_id") sportId: Int, @RequestParam("clazz_id") clazzId: Int, @RequestParam("gender") gender: Boolean, model: Model): String {
        
        // example data
        val sportList: List<SportModel> = listOf(
                SportModel(1, "Schnelllauf"),
                SportModel(2, "Weitsprung")
        )
        
        val clazzList: List<ClazzModel> = listOf(
                ClazzModel(1, "2b"),
                ClazzModel(2, "2a")
        )
        
        val formModel = TournamentCompetitorFormModel(
                SportModel(2, "Weitsprung"),
                ClazzModel(1, "2a"),
                false,
                listOf(
                        TournamentCompetitorModel(1, 1,"Max", "Muster", "50m", 0.0, "sec"),
                        TournamentCompetitorModel(2, 2,"Max", "Muster", null, 0.0, "sec")
                )
        )
        
        model.addAttribute("tournamentForm", formModel)
        model.addAttribute("sportList", sportList)
        model.addAttribute("clazzList", clazzList)
        
        return "tournament"
    }
}
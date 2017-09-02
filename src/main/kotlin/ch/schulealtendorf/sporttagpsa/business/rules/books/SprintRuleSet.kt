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

package ch.schulealtendorf.sporttagpsa.business.rules.books

import ch.schulealtendorf.sporttagpsa.business.rules.RuleFormula
import ch.schulealtendorf.sporttagpsa.business.rules.RuleSet
import ch.schulealtendorf.sporttagpsa.business.rules.RuleTarget.Members

/**
 * @author nmaerchy
 * @version 0.0.1
 */
class SprintRuleSet : RuleSet() {

    /**
     * Defines the condition, to apply the rule set.
     */
    override val condition: String = "Schnelllauf"
    
    override val rules: Set<RuleFormula> = setOf(
            
            object: RuleFormula() {
                override val whenever: (condition: String, target: Members) -> Boolean = { condition, target ->
                    check(condition) {
                        target.getAsBoolean("gender").isFemale() &&
                        target.getAsString("distance") == "50m"
                    }    
                }
                
                override val formula: (Members) -> Int = {
                    withResult(it) {
                        if (it > 12.06) 1 else (26.011098 * ((1236 - (it * 100) / 100) pow  2.1)).toInt()
                    }
                }
            },
            
            object: RuleFormula() {
                override val whenever: (condition: String, target: Members) -> Boolean = { condition, target -> 
                    check(condition) {
                        target.getAsBoolean("gender").isFemale() &&
                        target.getAsString("distance") == "60m"
                    }
                }
                override val formula: (Members) -> Int = {
                    withResult(it) {
                        if (it > 13.83) 1 else (19.742424 * ((1417 - (it * 100) / 100) pow 2.1)).toInt()
                    }
                }
            },
            
            object: RuleFormula() {
                override val whenever: (condition: String, target: Members) -> Boolean = { condition, target -> 
                    check(condition) {
                        target.getAsBoolean("gender").isFemale() &&
                        target.getAsString("distance") == "80m"
                    }
                }
                override val formula: (Members) -> Int = {
                    withResult(it) {
                        if (it > 17.59) 1 else (11.754907 * ((1803 - (it * 100) / 100) pow 2.1)).toInt()
                    }
                }
            },
            
            object: RuleFormula() {
                override val whenever: (condition: String, target: Members) -> Boolean = { condition, target -> 
                    check(condition) {
                        target.getAsBoolean("gender").isMale() &&
                        target.getAsString("distance") == "50m"
                    }
                }
                override val formula: (Members) -> Int = {
                    withResult(it) {
                        if (it > 11.87) 1 else (23.327251 * ((1219 - (it * 100) / 100) pow 2.1)).toInt()
                    }
                }
            },

            object: RuleFormula() {
                override val whenever: (condition: String, target: Members) -> Boolean = { condition, target ->
                    check(condition) {
                        target.getAsBoolean("gender").isMale() &&
                                target.getAsString("distance") == "60m"
                    }
                }
                override val formula: (Members) -> Int = {
                    withResult(it) {
                        if (it > 13.61) 1 else (17.686955 * ((1397 - (it * 100) / 100) pow 2.1)).toInt()
                    }
                }
            },

            object: RuleFormula() {
                override val whenever: (condition: String, target: Members) -> Boolean = { condition, target ->
                    check(condition) {
                        target.getAsBoolean("gender").isMale() &&
                                target.getAsString("distance") == "80m"
                    }
                }
                override val formula: (Members) -> Int = {
                    withResult(it) {
                        if (it > 17.32) 1 else (10.54596 * ((1778 - (it * 100) / 100) pow 2.1)).toInt()
                    }
                }
            }
    )
    
    private inline fun withResult(target: Members, body: (Double) -> Int): Int {
        return body(target.getAsDouble("result"))
    }
}
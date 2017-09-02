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

import ch.schulealtendorf.sporttagpsa.business.rules.FactKeys
import ch.schulealtendorf.sporttagpsa.business.rules.RuleTarget
import com.deliveredtechnologies.rulebook.FactMap
import com.deliveredtechnologies.rulebook.NameValueReferableMap
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import kotlin.test.assertEquals

/**
 * @author nmaerchy
 * @version 0.0.1
 */
@RunWith(JUnitPlatform::class)
object SprintRuleSetSpec: Spek({
    
    describe("a sprint rule set") {
        
        val ruleBook = PSARuleBook()
        val facts: NameValueReferableMap<Any> = FactMap()
        
        beforeEachTest {
            
        }
        
        
        
        given("girls 50m") {

            val ruleTarget = RuleTarget()
            ruleTarget.add("gender", false)
            ruleTarget.add("distance", "50m")
            
            on("using default points") {
                facts.setValue(FactKeys.CONDITION.name, "Schnelllauf")
                ruleTarget.add("result", 50.0)
                facts.setValue(FactKeys.TARGET.name, ruleTarget)
                
                ruleBook.run(facts)
                
                val result = ruleBook.result.get().value
                
                it("should return 1") {
                    assertEquals(1, result)
                }
            }
        }
    }
    
    
})

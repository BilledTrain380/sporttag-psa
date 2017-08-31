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

package ch.schulealtendorf.sporttagpsa.business.rules

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * Spec for {@link RuleTarget}.
 * 
 * Only test one method, because the others are the same thing,
 * just with another type cast
 * 
 * @author nmaerchy
 * *
 * @version 1.0.0
 */
@RunWith(JUnitPlatform::class)
object RuleTargetSpec: Spek({
    
    describe("a rule target") {
        
        var ruleTarget = RuleTarget()
        
        beforeEachTest { 
            ruleTarget = RuleTarget()
        }
        
        given("a key to get a boolean") {
            
            on("successful type cast") {

                ruleTarget.add("foo", true)
                
                val value = ruleTarget.getAsBoolean("foo")
                
                it("it should return the boolean value") {
                    assertEquals(true, value)
                }
            }
            
            on("failed type cast") {
                
                ruleTarget.add("foo", "not a boolean")
                
                it("should throw an class cast exception") {
                    assertFailsWith<ClassCastException> { 
                        ruleTarget.getAsBoolean("foo")
                    }
                }
            }
            
            on("key does not exist") {
                
                it("should throw a no such element exception") {
                    assertFailsWith<NoSuchElementException> { 
                        ruleTarget.getAsBoolean("foo")
                    }
                }
            }
        }
    }
})

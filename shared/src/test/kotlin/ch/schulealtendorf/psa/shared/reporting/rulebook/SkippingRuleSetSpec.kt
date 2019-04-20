/*
 * Copyright (c) 2019 by Nicolas Märchy
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

package ch.schulealtendorf.psa.shared.reporting.rulebook

import ch.schulealtendorf.psa.shared.rulebook.FormulaModel
import ch.schulealtendorf.psa.shared.rulebook.SkippingRuleSet
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import kotlin.test.assertEquals

/**
 * Specification for a skipping rule set.
 *
 * @author nmaerchy
 * @version 1.0.0
 */
object SkippingRuleSetSpec : Spek({

    describe("a skipping rule set") {

        val male = true
        val female = false

        val ruleSet = SkippingRuleSet()

        given("a formula model") {

            on("girls") {

                val model = FormulaModel("Seilspringen", null, 170.0, female)
                val points: Int = ruleSet.getRules().first { it.whenever(model) }.then(model)

                it("should return the resulting points") {
                    val expected = 598
                    assertEquals(expected, points)
                }
            }

            on("boys") {

                val model = FormulaModel("Seilspringen", null, 88.0, male)
                val points: Int = ruleSet.getRules().first { it.whenever(model) }.then(model)

                it("should return the resulting points") {
                    val expected = 275
                    assertEquals(expected, points)
                }
            }
        }
    }
})
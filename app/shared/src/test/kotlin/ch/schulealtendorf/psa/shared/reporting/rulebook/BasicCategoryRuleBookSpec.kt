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

import ch.schulealtendorf.psa.shared.rulebook.BasicCategoryRuleBook
import ch.schulealtendorf.psa.shared.rulebook.CategoryModel
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import kotlin.test.assertEquals

/**
 * Specification for {@link CategoryRuleBook}.
 *
 * @author nmaerchy
 * @version 1.0.0
 */
object BasicCategoryRuleBookSpec : Spek({
    val ruleBook = BasicCategoryRuleBook()

    Feature("a target throwing discipline") {
        Scenario("age under 12") {
            val categoryModel = CategoryModel(11, "Ballzielwurf")

            val distance = ruleBook.run(categoryModel)

            Then("should return 4m") {
                val expected = "4m"
                assertEquals(expected, distance)
            }
        }

        Scenario("age higher than 11") {
            val categoryModel = CategoryModel(12, "Ballzielwurf")

            val distance = ruleBook.run(categoryModel)

            Then("should return 5m") {
                val expected = "5m"
                assertEquals(expected, distance)
            }
        }
    }

    Feature("a basket throwing discipline") {
        Scenario("age under 12") {
            val categoryModel = CategoryModel(11, "Korbeinwurf")

            val distance = ruleBook.run(categoryModel)

            Then("should return 2m") {
                val expected = "2m"
                assertEquals(expected, distance)
            }
        }

        Scenario("age higher than 11") {
            val categoryModel = CategoryModel(12, "Korbeinwurf")

            val distance = ruleBook.run(categoryModel)

            Then("should return 2.5m") {
                val expected = "2.5m"
                assertEquals(expected, distance)
            }
        }
    }

    Feature("a skipping discipline") {
        Scenario("running after an applied rule") {
            val ballThrowing = CategoryModel(12, "Korbeinwurf")
            val skipping = CategoryModel(12, "Seilspringen")

            ruleBook.run(ballThrowing)

            val distance = ruleBook.run(skipping)

            Then("should return null") {
                val expected = null
                assertEquals(expected, distance)
            }
        }
    }
})

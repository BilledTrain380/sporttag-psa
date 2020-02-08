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
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import kotlin.test.assertEquals

/**
 * Specification for {@link CategoryRuleBook}.
 *
 * @author nmaerchy
 * @version 1.0.0
 */
object BasicCategoryRuleBookSpec : Spek({

    describe("a category rule book") {

        val ruleBook = BasicCategoryRuleBook()

        given("a target throwing discipline") {

            on("age under 12") {

                // Arrange
                val categoryModel = CategoryModel(11, "Ballzielwurf")

                // Act
                val distance = ruleBook.run(categoryModel)

                // Assert
                it("should return 4m") {
                    val expected = "4m"
                    assertEquals(expected, distance)
                }
            }

            on("age higher than 11") {

                // Arrange
                val categoryModel = CategoryModel(12, "Ballzielwurf")

                // Act
                val distance = ruleBook.run(categoryModel)

                // Assert
                it("should return 5m") {
                    val expected = "5m"
                    assertEquals(expected, distance)
                }
            }
        }

        given("a basket throwing discipline") {

            on("age under 12") {

                // Arrange
                val categoryModel = CategoryModel(11, "Korbeinwurf")

                // Act
                val distance = ruleBook.run(categoryModel)

                // Assert
                it("should return 2m") {
                    val expected = "2m"
                    assertEquals(expected, distance)
                }
            }

            on("age higher than 11") {

                // Arrange
                val categoryModel = CategoryModel(12, "Korbeinwurf")

                // Act
                val distance = ruleBook.run(categoryModel)

                // Assert
                it("should return 2.5m") {
                    val expected = "2.5m"
                    assertEquals(expected, distance)
                }
            }
        }

        given("a skipping discipline") {

            on("running after an applied rule") {

                // Arrange
                val ballThrowing = CategoryModel(12, "Korbeinwurf")
                val skipping = CategoryModel(12, "Seilspringen")

                ruleBook.run(ballThrowing)

                // Act
                val distance = ruleBook.run(skipping)

                // Assert
                it("should return null") {
                    val expected = null
                    assertEquals(expected, distance)
                }
            }
        }
    }
})

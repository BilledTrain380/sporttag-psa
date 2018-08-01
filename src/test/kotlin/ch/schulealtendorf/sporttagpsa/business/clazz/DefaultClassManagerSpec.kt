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

package ch.schulealtendorf.sporttagpsa.business.clazz

import ch.schulealtendorf.sporttagpsa.entity.*
import ch.schulealtendorf.sporttagpsa.model.Clazz
import ch.schulealtendorf.sporttagpsa.model.Coach
import ch.schulealtendorf.sporttagpsa.repository.ClazzRepository
import ch.schulealtendorf.sporttagpsa.repository.CompetitorRepository
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.whenever
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import kotlin.test.assertEquals

/**
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.0.0
 */

object DefaultClassManagerSpec: Spek({

    describe("a class manager") {

        val mockCompetitorRepository: CompetitorRepository = mock()
        val mockClassRepository: ClazzRepository = mock()

        val classManager = DefaultClassManager(mockClassRepository, mockCompetitorRepository)


        val running = SportEntity("Running")
        val classes: List<ClazzEntity> = listOf(
                ClazzEntity("2a", CoachEntity(1, "Max Muster")),
                ClazzEntity("2b", CoachEntity(2, "Max Master")),
                ClazzEntity("2c", CoachEntity(3, "Max Mister"))
        )

        beforeEachTest {
            reset(mockClassRepository, mockClassRepository)
        }

        context("get all classes") {

            on("no pending participation") {

                val competitors: List<CompetitorEntity> = listOf(
                        CompetitorEntity().apply { sport = running },
                        CompetitorEntity().apply { sport = running },
                        CompetitorEntity().apply { sport = running },
                        CompetitorEntity().apply { sport = running }
                )

                whenever(mockClassRepository.findAll()).thenReturn(classes)
                whenever(mockCompetitorRepository.findByClazzName(any())).thenReturn(competitors)


                val result = classManager.getAllClasses()


                it("should return a list of classes with no pending participation") {

                    val expected: List<Clazz> = listOf(
                            Clazz("2a", Coach(1, "Max Muster")),
                            Clazz("2b", Coach(2, "Max Master")),
                            Clazz("2c", Coach(3, "Max Mister"))
                    )
                    assertEquals(expected, result)
                }
            }

            on("pending participation") {

                val competitors: List<CompetitorEntity> = listOf(
                        CompetitorEntity().apply { sport = running },
                        CompetitorEntity().apply { sport = running },
                        CompetitorEntity().apply { sport = running },
                        CompetitorEntity() // one competitor has no sport so the class has pending participation
                )

                whenever(mockClassRepository.findAll()).thenReturn(classes)
                whenever(mockCompetitorRepository.findByClazzName(any())).thenReturn(competitors)


                val result = classManager.getAllClasses()


                it("should return a list of classes with pending participation") {

                    val expected: List<Clazz> = listOf(
                            Clazz("2a", Coach(1, "Max Muster"), true),
                            Clazz("2b", Coach(2,"Max Master"), true),
                            Clazz("2c", Coach(3, "Max Mister"), true)
                    )
                    assertEquals(expected, result)
                }
            }
        }
    }
})

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

package ch.schulealtendorf.sporttagpsa.business.participation

import ch.schulealtendorf.sporttagpsa.entity.AbsentCompetitorEntity
import ch.schulealtendorf.sporttagpsa.entity.CompetitorEntity
import ch.schulealtendorf.sporttagpsa.repository.AbsentCompetitorRepository
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import java.util.*
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * @author nmaerchy <billedtrain380></billedtrain380>@gmail.com>
 * @since 2.0.0
 */
object DefaultAbsentManagerSpec: Spek({

    describe("an absent manager") {

        val mockAbsentCompetitorRepository: AbsentCompetitorRepository = mock()

        var absentManager = DefaultAbsentManager(mockAbsentCompetitorRepository)

        val competitor = CompetitorEntity(
                1,
                "Muster",
                "Max",
                true
        )

        beforeEachTest {
            absentManager = DefaultAbsentManager(mockAbsentCompetitorRepository)
        }

        describe("absent of a competitor") {

            given("a competitor which is not absent") {

                whenever(mockAbsentCompetitorRepository.findByCompetitorId(any())).thenReturn(Optional.empty())


                val result = absentManager.isAbsent(competitor)


                it("should return false") {
                    assertFalse(result)
                }
            }

            given("a competitor which is absent") {

                whenever(mockAbsentCompetitorRepository.findByCompetitorId(any())).thenReturn(Optional.of(AbsentCompetitorEntity()))


                val result = absentManager.isAbsent(competitor)


                it("should return true") {
                    assertTrue(result)
                }
            }

            given("no competitor id") {


                val result = absentManager.isAbsent(CompetitorEntity())


                it("should return false") {
                    assertFalse(result)
                }
            }
        }
    }
})

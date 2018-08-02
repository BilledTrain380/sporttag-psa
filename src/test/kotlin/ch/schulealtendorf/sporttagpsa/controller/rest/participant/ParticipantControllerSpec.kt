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

package ch.schulealtendorf.sporttagpsa.controller.rest.participant

import ch.schulealtendorf.sporttagpsa.business.clazz.ClassManager
import ch.schulealtendorf.sporttagpsa.business.participation.ParticipantManager
import ch.schulealtendorf.sporttagpsa.controller.rest.BadRequestException
import ch.schulealtendorf.sporttagpsa.controller.rest.RestClass
import ch.schulealtendorf.sporttagpsa.controller.rest.RestPatchParticipant
import ch.schulealtendorf.sporttagpsa.controller.rest.RestTown
import ch.schulealtendorf.sporttagpsa.model.*
import com.nhaarman.mockito_kotlin.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.0.0
 */
object ParticipantControllerSpec: Spek({

    describe("a participant rest controller") {

        val mockParticipantManager: ParticipantManager = mock()
        val mockClassManager: ClassManager = mock()

        val controller = ParticipantController(mockParticipantManager, mockClassManager)


        val participant = Participant(
                1,
                "Muster",
                "Max",
                Gender.male(),
                Birthday(0),
                false,
                "",
                Town(1, "3000", "Bern"),
                Clazz("2a", Coach(1, "Müller"))
        )

        beforeEachTest {
            reset(mockParticipantManager, mockClassManager)
        }

        context("patch a participant") {

            on("given town to update") {

                whenever(mockParticipantManager.getParticipant(any())).thenReturn(Optional.of(participant))


                val town = RestTown(2, "8000", "Zürich")
                val patchParticipant = RestPatchParticipant(town)
                controller.updateParticipant(1, patchParticipant)


                it("should update the town of the participant") {
                    val expected = participant.copy(
                            town = Town(2, "8000", "Zürich")
                    )
                    verify(mockParticipantManager, times(1)).saveParticipant(expected)
                }
            }

            on("given class to update") {

                whenever(mockParticipantManager.getParticipant(any())).thenReturn(Optional.of(participant))

                val clazz = Clazz("3a", Coach(2, "Muster"))
                whenever(mockClassManager.getClass(any())).thenReturn(Optional.of(clazz))


                val restClass = RestClass("3a", "Muster", false)
                val patchParticipant = RestPatchParticipant(clazz = restClass)
                controller.updateParticipant(1, patchParticipant)


                it("should update the class of the participant") {
                    val expected = participant.copy(
                            clazz = clazz
                    )
                    verify(mockParticipantManager, times(1)).saveParticipant(expected)
                }
            }

            on("given sport to update") {

                whenever(mockParticipantManager.getParticipant(any())).thenReturn(Optional.of(participant))

                val patchParticipant = RestPatchParticipant(sport = "Running")
                controller.updateParticipant(1, patchParticipant)


                it("should update the sport of the participant") {
                    val expected = participant.copy(
                            sport = Optional.of("Running")
                    )
                    verify(mockParticipantManager, times(1)).saveParticipant(expected)
                }
            }

            on("nothing is given") {

                it("should throw a bad request exception") {
                    val exception = assertFailsWith<BadRequestException> {
                        controller.updateParticipant(1, RestPatchParticipant())
                    }
                    assertEquals("Missing either 'clazz', 'town' or 'sport' in request body.", exception.message)
                }
            }
        }
    }
})

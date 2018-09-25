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

import ch.schulealtendorf.sporttagpsa.business.group.GroupManager
import ch.schulealtendorf.sporttagpsa.business.participation.ParticipantManager
import ch.schulealtendorf.sporttagpsa.business.participation.ParticipationManager
import ch.schulealtendorf.sporttagpsa.controller.rest.json
import ch.schulealtendorf.sporttagpsa.model.*
import com.nhaarman.mockito_kotlin.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.util.*
import kotlin.test.assertEquals

/**
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.0.0
 */
object ParticipantControllerSpec: Spek({

    describe("a participant controller") {

        val mockParticipantManager: ParticipantManager = mock()
        val mockParticipationManager: ParticipationManager = mock()
        val mockGroupManager: GroupManager = mock()

        val controller = ParticipantController(mockParticipantManager, mockParticipationManager, mockGroupManager)

        beforeEachTest {
            // we don't reset the mapper, because it only matters when a group is mapped. We just simulate the group mapping
            reset(mockParticipantManager, mockParticipationManager, mockGroupManager)

            // for less boilerplate we use the RestMapper and just set the pending status of a group to false
            whenever(mockGroupManager.hasPendingParticipation(any())).thenReturn(false)
        }

        val g2a = Group("2a", Coach(1, "Müller"))
        val g3a = Group("3a", Coach(2, "Meyer"))

        val mmuster = Participant(1, "Muster", "Max", Gender.MALE,
                Birthday(0), false, "",
                Town("", ""),
                g2a)

        val wwirbelwind = Participant(2, "Wirbelwind", "Willi", Gender.MALE,
                Birthday(0), false, "",
                Town("", ""),
                g3a)

        context("participant list request") {

            on("no group request parameter") {

                whenever(mockParticipantManager.getParticipants()).thenReturn(listOf(mmuster, wwirbelwind))


                val result = controller.getParticipants(null)


                it("should return all participants") {
                    assertEquals(listOf(json(mmuster), json(wwirbelwind)), result)
                }
            }

            on("group request parameter") {

                whenever(mockGroupManager.getGroup("2a")).thenReturn(Optional.of(g2a))
                whenever(mockParticipantManager.getParticipants(g2a)).thenReturn(listOf(mmuster))


                val result = controller.getParticipants("2a")


                it("should return only participants related to the group") {
                    assertEquals(listOf(json(mmuster)), result)
                }
            }

            on("group request parameter which does not exist") {

                whenever(mockGroupManager.getGroup("2a")).thenReturn(Optional.empty())


                val result = controller.getParticipants("2a")


                it("should return an empty list") {
                    assertEquals(listOf(), result)
                }
            }
        }

        context("a new participant to create") {

            on("open participation") {

                whenever(mockGroupManager.getGroup("2a")).thenReturn(Optional.of(g2a))
                whenever(mockParticipantManager.saveParticipant(any())).thenReturn(mmuster)
                whenever(mockParticipationManager.getParticipationStatus()).thenReturn(ParticipationStatus.OPEN)


                val newParticipant = CreateParticipant(mmuster.surname, mmuster.prename, mmuster.gender, mmuster.birthday.milliseconds, mmuster.address, mmuster.town, Sport("athletics"))
                controller.createParticipant("2a", newParticipant)


                it("should create the new participant") {
                    verify(mockParticipantManager, times(1)).saveParticipant(mmuster.copy(id = 0))
                }

                it("should participate the created participant in the given sport") {
                    verify(mockParticipationManager, times(1)).participate(mmuster, Sport("athletics"))
                }
            }

            on("closed participation") {

                whenever(mockGroupManager.getGroup("2a")).thenReturn(Optional.of(g2a))
                whenever(mockParticipantManager.saveParticipant(any())).thenReturn(mmuster)
                whenever(mockParticipationManager.getParticipationStatus()).thenReturn(ParticipationStatus.CLOSE)


                val newParticipant = CreateParticipant(mmuster.surname, mmuster.prename, mmuster.gender, mmuster.birthday.milliseconds, mmuster.address, mmuster.town, Sport("athletics"))
                controller.createParticipant("2a", newParticipant)


                it("should create the new participant") {
                    verify(mockParticipantManager, times(1)).saveParticipant(mmuster.copy(id = 0))
                }

                it("should re participate  the created participant in the given sport") {
                    verify(mockParticipationManager, times(1)).reParticipate(mmuster, Sport("athletics"))
                }
            }
        }

        context("patch a participant") {

            on("nullable values") {

                whenever(mockParticipantManager.getParticipant(1)).thenReturn(Optional.of(mmuster))
                whenever(mockParticipantManager.saveParticipant(any())).thenReturn(mmuster)


                val updateParticipant = UpdateParticipant(prename = "Hans")
                controller.patchParticipant(1, updateParticipant)


                it("should only update non null values") {
                    verify(mockParticipantManager, times(1)).saveParticipant(mmuster.copy(prename = "Hans"))
                }
            }

            on("absent set to true") {

                whenever(mockParticipantManager.getParticipant(1)).thenReturn(Optional.of(mmuster))
                whenever(mockParticipantManager.saveParticipant(any())).thenReturn(mmuster)


                val updateParticipant = UpdateParticipant(absent = true)
                controller.patchParticipant(1, updateParticipant)


                it("should mark the participant as absent") {
                    verify(mockParticipationManager, times(1)).markAsAbsent(mmuster)
                }
            }

            on("absent set to false") {

                whenever(mockParticipantManager.getParticipant(1)).thenReturn(Optional.of(mmuster))
                whenever(mockParticipantManager.saveParticipant(any())).thenReturn(mmuster)


                val updateParticipant = UpdateParticipant(absent = false)
                controller.patchParticipant(1, updateParticipant)


                it("should mark the participant as present") {
                    verify(mockParticipationManager, times(1)).markAsPresent(mmuster)
                }
            }
        }

        context("put a participant") {

            on("open participation") {

                whenever(mockParticipantManager.getParticipant(1)).thenReturn(Optional.of(mmuster))
                whenever(mockParticipantManager.saveParticipant(any())).thenReturn(mmuster)
                whenever(mockParticipationManager.getParticipationStatus()).thenReturn(ParticipationStatus.OPEN)


                val town = Town("8000", "Zürich")
                val sport = Sport("athletics")
                val updateParticipant = UpdateParticipant(town = town, sport = sport)
                controller.putParticipantTown(1, updateParticipant)


                it("should update the town") {
                    verify(mockParticipantManager, times(1)).saveParticipant(mmuster.copy(town = town))
                }

                it("should participate the participant on the given sport") {
                    verify(mockParticipationManager, times(1)).participate(mmuster, sport)
                }
            }

            on("closed participation") {

                whenever(mockParticipantManager.getParticipant(1)).thenReturn(Optional.of(mmuster))
                whenever(mockParticipantManager.saveParticipant(any())).thenReturn(mmuster)
                whenever(mockParticipationManager.getParticipationStatus()).thenReturn(ParticipationStatus.CLOSE)


                val sport = Sport("athletics")
                val updateParticipant = UpdateParticipant(sport = sport)
                controller.putParticipantTown(1, updateParticipant)


                it("should re-participate the participant on the given sport") {
                    verify(mockParticipationManager, times(1)).reParticipate(mmuster, sport)
                }
            }
        }
    }
})

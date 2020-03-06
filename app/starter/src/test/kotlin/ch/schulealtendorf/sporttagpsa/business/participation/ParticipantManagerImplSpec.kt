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

import ch.schulealtendorf.psa.dto.BirthdayDto
import ch.schulealtendorf.psa.dto.CoachDto
import ch.schulealtendorf.psa.dto.GenderDto
import ch.schulealtendorf.psa.dto.GroupDto
import ch.schulealtendorf.psa.dto.ParticipantDto
import ch.schulealtendorf.psa.dto.TownDto
import ch.schulealtendorf.sporttagpsa.entity.CoachEntity
import ch.schulealtendorf.sporttagpsa.entity.GroupEntity
import ch.schulealtendorf.sporttagpsa.entity.ParticipantEntity
import ch.schulealtendorf.sporttagpsa.entity.TownEntity
import ch.schulealtendorf.sporttagpsa.repository.GroupRepository
import ch.schulealtendorf.sporttagpsa.repository.ParticipantRepository
import ch.schulealtendorf.sporttagpsa.repository.TownRepository
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import java.util.NoSuchElementException
import java.util.Optional
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

/**
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.0.0
 */
object ParticipantManagerImplSpec : Spek({

    describe("a participant manager") {

        val mockParticipantRepository: ParticipantRepository = mock()
        val mockTownRepository: TownRepository = mock()
        val mockGroupRepository: GroupRepository = mock()

        val manager = ParticipantManagerImpl(
            mockParticipantRepository,
            mockTownRepository,
            mockGroupRepository
        )

        beforeEachTest {
            reset(
                mockParticipantRepository,
                mockTownRepository,
                mockGroupRepository
            )
        }

        val townEntity = TownEntity(
            id = 1,
            zip = "3000",
            name = "Bern"
        )

        val town = TownDto(
            zip = "3000",
            name = "Bern"
        )

        val groupEntity = GroupEntity(
            name = "2a",
            coach = CoachEntity(
                id = 1,
                name = "Willi"
            )
        )

        val group = GroupDto(
            name = "2a",
            coach = CoachDto(
                id = 1,
                name = "Willi"
            )
        )

        val participantEntity = ParticipantEntity(
            id = 1,
            surname = "Muster",
            prename = "Max",
            gender = GenderDto.MALE,
            birthday = 0,
            address = "Musterstrasse 8",
            town = townEntity,
            group = groupEntity
        )

        val participantModel = ParticipantDto(
            1,
            "Muster",
            "Max",
            GenderDto.MALE,
            BirthdayDto(0),
            false,
            "Musterstrasse 8",
            town,
            group
        )

        context("save a participant") {

            beforeEachTest {
                // we only set the nullable values to avoid the null pointer
                whenever(mockParticipantRepository.save(any<ParticipantEntity>()))
                    .thenReturn(
                        ParticipantEntity(1, town = TownEntity(1), group = GroupEntity(coach = CoachEntity(1)))
                    )
            }

            on("new participant") {

                whenever(mockParticipantRepository.findById(any())).thenReturn(Optional.empty())
                whenever(mockTownRepository.findByZipAndName(any(), any())).thenReturn(Optional.of(townEntity))
                whenever(mockGroupRepository.findById(any())).thenReturn(Optional.of(groupEntity))

                val participant = participantModel.copy(id = 0) // id 0 to create the participant
                manager.saveParticipant(participant)

                it("should create the participant") {
                    val expected = participantEntity.copy(id = null) // entity with id null will be created
                    verify(mockParticipantRepository, times(1)).save(expected)
                }
            }

            on("new town") {

                whenever(mockParticipantRepository.findById(any())).thenReturn(Optional.of(participantEntity))
                whenever(mockTownRepository.findByZipAndName(any(), any())).thenReturn(Optional.empty())
                whenever(mockGroupRepository.findById(any())).thenReturn(Optional.of(groupEntity))

                val participant = participantModel.copy(
                    town = TownDto("8000", "Zürich")

                )
                manager.saveParticipant(participant)

                it("should create the town") {
                    val expected = participantEntity.copy(
                        town = TownEntity( // entity with id null will be created
                            zip = "8000",
                            name = "Zürich"
                        )
                    )
                    verify(mockParticipantRepository, times(1)).save(expected)
                }
            }

            on("unkown group") {

                whenever(mockParticipantRepository.findById(any())).thenReturn(Optional.of(participantEntity))
                whenever(mockGroupRepository.findById(any())).thenReturn(Optional.empty())

                val participant = participantModel.copy(
                    group = GroupDto("3a", CoachDto(0, "Müller")) // coach id 0 to create the coach
                )

                it("should throw a no such element exception, indicating that the group does not exist") {
                    val exception = assertFailsWith<NoSuchElementException> {
                        manager.saveParticipant(participant)
                    }
                    assertEquals("Participant group does not exist: name=3a", exception.message)
                }
            }
        }
    }
})

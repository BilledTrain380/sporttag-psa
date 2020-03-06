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

package ch.schulealtendorf.sporttagpsa.business.setup

import ch.schulealtendorf.psa.dto.UserDto
import ch.schulealtendorf.sporttagpsa.business.user.USER_ADMIN
import ch.schulealtendorf.sporttagpsa.business.user.UserManager
import ch.schulealtendorf.sporttagpsa.entity.SetupEntity
import ch.schulealtendorf.sporttagpsa.repository.SetupRepository
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import java.util.Optional
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

object StatefulSetupManagerSpec : Spek({

    describe("a stateful setup manager") {

        val mockSetupRepository: SetupRepository = mock()
        val mockUserManager: UserManager = mock()

        beforeEachTest {
            reset(mockSetupRepository, mockUserManager)
        }

        val defaultSetup = SetupEntity(jwtSecret = "my_secret")

        context("initialize the setup") {

            on("successful initialization") {

                val admin = UserDto(1, USER_ADMIN, listOf())
                whenever(mockUserManager.getOne(USER_ADMIN)).thenReturn(Optional.of(admin))

                whenever(mockSetupRepository.findById(any())).thenReturn(Optional.of(defaultSetup.copy()))

                val manager = StatefulSetupManager(mockSetupRepository, mockUserManager)
                val setup = SetupInformation("admin")
                manager.initialize(setup)

                it("should set the admin's password") {
                    val expected = "admin"
                    verify(mockUserManager, times(1)).changePassword(admin.copy(), expected)
                }

                it("should save a generated JWT secret") {
                    // because the JWT secret is random, we can not assert its value
                    verify(mockSetupRepository, times(1)).save(any<SetupEntity>())
                }

                it("should update the JWT secret") {
                    assertNotEquals(manager.jwtSecret, defaultSetup.jwtSecret)
                }

                it("should update the initialized property") {
                    assertTrue(manager.isInitialized)
                }
            }

            on("already initialized setup") {

                whenever(mockSetupRepository.findById(any())).thenReturn(Optional.of(defaultSetup.copy(initialized = true)))

                val manager = StatefulSetupManager(mockSetupRepository, mockUserManager)

                it("should throw an illegal state exception, indicating that the setup is already initialized") {
                    val exception = assertFailsWith<IllegalStateException> {
                        val setup = SetupInformation("admin")
                        manager.initialize(setup)
                    }
                    assertEquals("Setup is already initialized", exception.message)
                }
            }
        }
    }
})

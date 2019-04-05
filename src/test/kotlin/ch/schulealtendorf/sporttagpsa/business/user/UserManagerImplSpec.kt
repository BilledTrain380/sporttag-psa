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

package ch.schulealtendorf.sporttagpsa.business.user

import ch.schulealtendorf.psa.dto.UserDto
import ch.schulealtendorf.sporttagpsa.business.user.validation.PasswordValidator
import ch.schulealtendorf.sporttagpsa.business.user.validation.ValidationResult
import ch.schulealtendorf.sporttagpsa.entity.AuthorityEntity
import ch.schulealtendorf.sporttagpsa.entity.UserEntity
import ch.schulealtendorf.sporttagpsa.repository.UserRepository
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argWhere
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

object UserManagerImplSpec : Spek({

    describe("a user manager") {

        val mockUserRepository: UserRepository = mock()
        val mockPasswordValidator: PasswordValidator = mock()

        val userManager = UserManagerImpl(mockUserRepository, mockPasswordValidator)

        beforeEachTest {
            reset(mockUserRepository, mockPasswordValidator)
        }

        val userEntity = UserEntity(username = "user", password = "secret".encode(), enabled = true, authorities = listOf(AuthorityEntity("ROLE_USER")))

        context("a user to save") {

            on("a new user") {

                whenever(mockUserRepository.findById(any())).thenReturn(Optional.empty())
                whenever(mockUserRepository.save(any<UserEntity>())).thenReturn(userEntity.copy(id = 1)) // the saved user has a id now
                whenever(mockPasswordValidator.validate(any())).thenReturn(ValidationResult(true))


                val user = UserDto(0, "user", listOf("ROLE_USER"), password = "secret")
                val result = userManager.save(user)

                it("should validate the password") {
                    verify(mockPasswordValidator, times(1)).validate("secret")
                }

                it("should create the user") {
                    val expected = userEntity.copy()
                    verify(mockUserRepository, times(1)).save(argWhere<UserEntity> {
                        expected.id == it.id &&
                                expected.username == it.username &&
                                BCryptPasswordEncoder(4).matches("secret", it.password) &&
                                expected.enabled == it.enabled &&
                                expected.authorities == it.authorities
                    })
                }

                it("should return the created user") {
                    val expected = UserDto(1, "user", listOf("ROLE_USER"))
                    assertEquals(expected, result)
                }
            }

            on("existing user") {

                whenever(mockUserRepository.findById(any())).thenReturn(Optional.of(userEntity.copy(id = 1)))

                val updatedEntity = userEntity.copy(id = 1, username = "user1", authorities = listOf(AuthorityEntity("ROLE_USER"), AuthorityEntity("ROLE_ADMIN")), enabled = false)
                whenever(mockUserRepository.save(any<UserEntity>())).thenReturn(updatedEntity.copy())


                val user = UserDto(1, "user1", listOf("ROLE_USER", "ROLE_ADMIN"), password = "should not be considered", enabled = false)
                val result = userManager.save(user)


                it("should not update the password property") {
                    val expected = updatedEntity.copy()
                    verify(mockUserRepository, times(1)).save(argWhere<UserEntity> {
                        expected.id == it.id &&
                                expected.username == it.username &&
                                BCryptPasswordEncoder(4).matches("secret", it.password) && // password is not changed
                                expected.enabled == it.enabled &&
                                expected.authorities == it.authorities
                    })
                }

                it("should return the created user") {
                    val expected = UserDto(1, "user1", listOf("ROLE_USER", "ROLE_ADMIN"), false)
                    assertEquals(expected, result)
                }
            }
        }

        context("change password of a user") {

            on("existing user") {

                whenever(mockUserRepository.findById(any())).thenReturn(Optional.of(userEntity.copy(id = 1)))
                whenever(mockPasswordValidator.validate(any())).thenReturn(ValidationResult(true))


                val user = UserDto(1, "username", listOf())
                userManager.changePassword(user, "newPassword")


                it("should validate the password") {
                    verify(mockPasswordValidator, times(1)).validate("newPassword")
                }

                it("should save the password encoded") {
                    verify(mockUserRepository, times(1)).save(argWhere<UserEntity> {
                        BCryptPasswordEncoder(4).matches("newPassword", it.password)
                    })
                }
            }

            on("user does not exist") {

                whenever(mockUserRepository.findById(any())).thenReturn(Optional.empty())


                val user = UserDto(1, "username", listOf())


                it("should throw a user not found exception, indicating that the user could not be found") {
                    val exception = assertFailsWith<UserNotFoundException> {
                        userManager.changePassword(user, "newPassword")
                    }
                    assertEquals("The user could not be found: user=$user", exception.message)
                }
            }
        }
    }
})

private fun String.encode(): String {
    return BCryptPasswordEncoder(4).encode(this)
}

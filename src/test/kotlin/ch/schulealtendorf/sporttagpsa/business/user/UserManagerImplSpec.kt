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

import ch.schulealtendorf.sporttagpsa.entity.AuthorityEntity
import ch.schulealtendorf.sporttagpsa.entity.UserEntity
import ch.schulealtendorf.sporttagpsa.repository.UserRepository
import com.nhaarman.mockito_kotlin.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * @author nmaerchy
 * @version 0.0.1
 */
@RunWith(JUnitPlatform::class)
object UserManagerImplSpec: Spek({
    
    describe("a user manager") {
        
        val mockUserRepository: UserRepository = mock {  }
        
        var userManager = UserManagerImpl(mockUserRepository)
        
        beforeEachTest { 
            reset(mockUserRepository)
            userManager = UserManagerImpl(mockUserRepository)
        }
        
        given("a fresh user to create") {
            
            on("saving the user") {
                
                val user = FreshUser("mmuster", "password", true)
                
                
                userManager.create(user)
                
                it("should encode the password") {
                    val expected = UserEntity(null, "mmuster", "password", true, listOf(
                            AuthorityEntity("USER")
                    ))
                    verify(mockUserRepository, times(1)).save(argWhere<UserEntity> {  
                        expected.id == it.id &&
                                expected.username == it.username &&
                                BCryptPasswordEncoder(4).matches(expected.password, it.password) &&
                                expected.enabled == it.enabled &&
                                expected.authorities == it.authorities
                    })
                }
            }
            
            on("already existing username") {
                
                whenever(mockUserRepository.findByUsername(any())).thenReturn(UserEntity())
                
                it("should throw a user already exists exception") {
                    
                    val exception = assertFailsWith<UserAlreadyExistsException> { 
                        userManager.create(FreshUser("mmuster", "", true))
                    }
                    
                    assertEquals("User exists already: username=mmuster", exception.message)
                }
            }
        }
    }
})

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

package ch.schulealtendorf.sporttagpsa.controller.rest.user

import ch.schulealtendorf.psa.dto.UserDto
import ch.schulealtendorf.sporttagpsa.business.user.USER_ADMIN
import ch.schulealtendorf.sporttagpsa.business.user.UserManager
import ch.schulealtendorf.sporttagpsa.business.user.validation.InvalidPasswordException
import ch.schulealtendorf.sporttagpsa.controller.rest.BadRequestException
import ch.schulealtendorf.sporttagpsa.controller.rest.ForbiddenException
import ch.schulealtendorf.sporttagpsa.controller.rest.NotFoundException
import ch.schulealtendorf.sporttagpsa.controller.rest.RestUser
import ch.schulealtendorf.sporttagpsa.controller.rest.json
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

/**
 * Rest controller for user domain.
 *
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.0.0
 */
@RestController
@RequestMapping("/api/rest")
class UserController(
        private val userManager: UserManager
) {

    @GetMapping("/users")
    fun getUsers(): List<RestUser> {
        return userManager.getAll().map { json(it) }
    }

    @PostMapping("/users")
    fun createUser(@RequestBody newUser: NewUser) {

        val user = UserDto(0, newUser.username, listOf("ROLE_USER"), newUser.enabled, newUser.password)
        userManager.save(user)
    }

    @GetMapping("/user/{user_id}")
    fun getUser(@PathVariable("user_id") id: Int): RestUser {
        return json(userManager.findOne(id))
    }

    @PatchMapping("/user/{user_id}")
    fun updateUser(@PathVariable("user_id") id: Int, @RequestBody updateUser: UpdateUser, principal: Principal) {

        val user = userManager.findOne(id)

        if (principal.isAuthorized(user).not())
            throw ForbiddenException("The current user is not authorized to perform this operation.")

        userManager.save(
                user.copy(username = updateUser.username, enabled = updateUser.enabled)
        )
    }

    @PutMapping("/user/{user_id}")
    fun updateUser(@PathVariable("user_id") id: Int, @RequestBody wrapper: PasswordWrapper, principal: Principal) {

        try {

            val user = userManager.findOne(id)

            if (principal.isAuthorized(user).not())
                throw ForbiddenException("The current user is not authorized to perform this operation.")

            userManager.changePassword(user, wrapper.password)

        } catch (ex: InvalidPasswordException) {
            throw BadRequestException(ex.message, ex)
        }
    }

    @DeleteMapping("/user/{user_id}")
    fun deleteUser(@PathVariable("user_id") id: Int) {
        userManager.delete(id)
    }

    private fun UserManager.findOne(id: Int): UserDto {
        return getOne(id)
                .orElseThrow { NotFoundException("Could not find user: user_id=$id") }
    }

    private fun Principal.isAuthorized(user: UserDto) = (name == USER_ADMIN || name == user.username)
}
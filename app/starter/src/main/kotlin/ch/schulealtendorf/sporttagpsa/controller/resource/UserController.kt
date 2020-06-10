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

package ch.schulealtendorf.sporttagpsa.controller.resource

import ch.schulealtendorf.psa.dto.user.UserDto
import ch.schulealtendorf.psa.dto.user.UserElement
import ch.schulealtendorf.psa.dto.user.UserInput
import ch.schulealtendorf.psa.dto.user.UserRelation
import ch.schulealtendorf.sporttagpsa.business.user.IllegalUserOperationException
import ch.schulealtendorf.sporttagpsa.business.user.UserManager
import ch.schulealtendorf.sporttagpsa.business.user.validation.InvalidPasswordException
import ch.schulealtendorf.sporttagpsa.controller.resource.exceptions.BadRequestException
import ch.schulealtendorf.sporttagpsa.controller.resource.exceptions.NotFoundException
import ch.schulealtendorf.sporttagpsa.lib.ifNotNull
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.access.prepost.PreAuthorize
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
@RequestMapping("/api")
@Tag(name = "User", description = "Manage users")
class UserController(
    private val userManager: UserManager
) {

    @Operation(
        summary = "List users",
        tags = ["User"]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "List users",
                content = [Content(array = ArraySchema(schema = Schema(implementation = UserDto::class)))]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content()]
            )
        ]
    )
    @PreAuthorize("#oauth2.hasScope('user')")
    @GetMapping("/users")
    fun getUsers(): List<UserDto> {
        return userManager.getAll()
    }

    @Operation(
        summary = "Create a new user",
        tags = ["User"]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "The created user",
                content = [Content(schema = Schema(implementation = UserDto::class))]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content()]
            )
        ]
    )
    @PreAuthorize("#oauth2.hasScope('user')")
    @PostMapping("/users")
    fun createUser(@RequestBody userInput: UserInput): UserDto {
        val user = UserDto(
            id = 0,
            username = userInput.username,
            authorities = listOf("ROLE_USER"),
            enabled = userInput.enabled,
            password = userInput.password
        )

        return userManager.save(user)
    }

    @Operation(
        summary = "Get a single user",
        tags = ["User"],
        parameters = [
            Parameter(
                name = "user_id",
                description = "The id of the user to get"
            )
        ]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "The created user",
                content = [Content(schema = Schema(implementation = UserDto::class))]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content()]
            )
        ]
    )
    @PreAuthorize("#oauth2.hasScope('user')")
    @GetMapping("/user/{user_id}")
    fun getUser(@PathVariable("user_id") id: Int): UserDto {
        return userManager.getOneOrFail(id)
    }

    @Operation(
        summary = "Update a user",
        tags = ["User"],
        parameters = [
            Parameter(
                name = "user_id",
                description = "The id of the user to update"
            )
        ]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Successful updated the user",
                content = [Content()]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content()]
            )
        ]
    )
    @PreAuthorize("#oauth2.hasScope('user')")
    @PatchMapping("/user/{user_id}")
    fun updateUser(@PathVariable("user_id") id: Int, @RequestBody userElement: UserElement, principal: Principal) {
        val userBuilder = userManager.getOneOrFail(id).toBuilder()

        userElement.enabled.ifNotNull {
            userBuilder.setEnabled(it)
        }

        userElement.username.ifNotNull {
            userBuilder.setUsername(it)
        }

        userManager.save(userBuilder.build())
    }

    @Operation(
        summary = "Update the password of a user",
        tags = ["User"],
        parameters = [
            Parameter(
                name = "user_id",
                description = "The id of the user to update"
            )
        ]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Successful updated the user",
                content = [Content()]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content()]
            )
        ]
    )
    @PreAuthorize("#oauth2.hasScope('user')")
    @PutMapping("/user/{user_id}")
    fun updateUser(@PathVariable("user_id") id: Int, @RequestBody userRelation: UserRelation, principal: Principal) {
        val user = userManager.getOneOrFail(id)

        try {
            userManager.changePassword(user, userRelation.password)
        } catch (ex: InvalidPasswordException) {
            throw BadRequestException(ex.message)
        }
    }

    @Operation(
        summary = "Delete a user",
        tags = ["User"],
        parameters = [
            Parameter(
                name = "user_id",
                description = "The id of the user to delete"
            )
        ]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Successful deleted the user",
                content = [Content()]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content()]
            )
        ]
    )
    @PreAuthorize("#oauth2.hasScope('user')")
    @DeleteMapping("/user/{user_id}")
    fun deleteUser(@PathVariable("user_id") id: Int) {
        try {
            userManager.delete(id)
        } catch (exception: IllegalUserOperationException) {
            throw BadRequestException("Admin user can not be deleted")
        }
    }

    private fun UserManager.getOneOrFail(id: Int): UserDto {
        return getOne(id)
            .orElseThrow { NotFoundException("Could not find user: user_id=$id") }
    }
}

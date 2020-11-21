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

package ch.schulealtendorf.psa.core.user

import ch.schulealtendorf.psa.dto.user.UserDto
import ch.schulealtendorf.sporttagpsa.business.user.validation.InvalidPasswordException
import ch.schulealtendorf.sporttagpsa.business.user.validation.PasswordValidator
import ch.schulealtendorf.sporttagpsa.entity.AuthorityEntity
import ch.schulealtendorf.sporttagpsa.entity.UserEntity
import ch.schulealtendorf.sporttagpsa.lib.userDtoOf
import ch.schulealtendorf.sporttagpsa.repository.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import java.util.Optional

/**
 * Default implementation for managing a user.
 *
 * @author nmaerchy
 * @since 1.0.0
 */
@Component
class UserManagerImpl(
    private val userRepository: UserRepository,
    private val passwordValidator: PasswordValidator
) : UserManager {
    override fun save(user: UserDto): UserDto {
        val userEntity = userRepository.findById(user.id)
            .orElseGet {
                user.password.validate()
                UserEntity(password = user.password.encode())
            }

        if (userEntity.username == USER_ADMIN) {
            throw IllegalUserOperationException("Not allowed to modify admin user")
        }

        userEntity.apply {
            username = user.username
            enabled = user.enabled
            authorities = user.authorities.map { AuthorityEntity(it) }
        }

        return userRepository.save(userEntity).toDto()
    }

    override fun changePassword(user: UserDto, password: String) {
        val userEntity = userRepository.findById(user.id)
            .orElseThrow { UserNotFoundException("Could not find user: username=${user.username}") }

        password.validate()
        userEntity.password = password.encode()

        userRepository.save(userEntity)
    }

    override fun getAll(): List<UserDto> = userRepository.findAll().map { userDtoOf(it) }

    override fun getOne(userId: Int): Optional<UserDto> = userRepository.findById(userId).map { userDtoOf(it) }

    override fun getOne(username: String): Optional<UserDto> =
        userRepository.findByUsername(username).map { userDtoOf(it) }

    override fun delete(userId: Int) {
        val user = userRepository.findById(userId)

        if (user.isPresent && user.get().username == USER_ADMIN)
            throw IllegalUserOperationException("Not allowed to delete admin user")

        userRepository.deleteById(userId)
    }

    private fun UserEntity.toDto() = userDtoOf(this)

    private fun String.encode(): String = BCryptPasswordEncoder(4).encode(this)

    private fun String.validate() {
        val validationResult = passwordValidator.validate(this)
        if (validationResult.isValid.not())
            throw InvalidPasswordException(validationResult.messages.joinToString(", "))
    }
}

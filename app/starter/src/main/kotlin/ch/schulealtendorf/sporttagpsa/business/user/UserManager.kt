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
import ch.schulealtendorf.sporttagpsa.business.user.validation.InvalidPasswordException
import java.util.Optional

/**
 * Describes a manager to create, update or delete a user.
 * The manager encrypts the password field.
 *
 * @author nmaerchy
 * @version 1.0.0
 */
interface UserManager {

    /**
     * Saves the given {@code user}. If the user does not exist yet, it will be created.
     *
     * The {@code password} property will
     * not be considered at all.
     *
     * To update the password use the
     * {@link UserManager#update} method.
     *
     * @param user the user to save
     *
     * @return the saved user
     * @throws InvalidPasswordException if the password does not match the validation requirements
     */
    fun save(user: UserDto): UserDto

    /**
     * Changes the password for the given {@code user}.
     * The password will be encrypted before its being saved.
     *
     * @param user the user to change its password
     * @param password the password to use
     *
     * @throws UserNotFoundException if the given {@code user} does not exist
     */
    fun changePassword(user: UserDto, password: String)

    /**
     * @return all users
     */
    fun getAll(): List<UserDto>

    /**
     * Gets the user by the given {@code userId} or an empty Optional,
     * if the user does not exist.
     *
     * @param userId id of the user
     *
     * @return the resulting user or an empty Optional, if the user does not exist
     */
    fun getOne(userId: Int): Optional<UserDto>

    /**
     * Gets the user by the given {@code username} or an empty Optional,
     * if the username does not exist.
     *
     * @param username the username to look up
     *
     * @return the user or an empty Optional, if the username does not exist
     */
    fun getOne(username: String): Optional<UserDto>

    /**
     * Deletes the user matching the given {@code userId}.
     *
     * The user representing the administrator can not be deleted.
     *
     * @param userId id of the user to delete
     *
     * @throws IllegalArgumentException if the given id belongs to the administrator user
     */
    fun delete(userId: Int)
}

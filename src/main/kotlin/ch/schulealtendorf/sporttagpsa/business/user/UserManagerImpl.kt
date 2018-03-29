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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

/**
 * Default implementation for managing a user.
 * 
 * @author nmaerchy
 * @version 1.0.0
 */
@Component
class UserManagerImpl(
        private val userRepository: UserRepository
): UserManager {

    /**
     * Creates the given {@code user}.
     * The {@code FreshUser#password} field will be encrypted with {@link BCryptPasswordEncoder}.
     *
     * @param user the user to create
     * 
     * @throws UserAlreadyExistsException if the user exists already
     */
    override fun create(user: FreshUser) {
        
        if(userRepository.findByUsername(user.username) != null) {
            throw UserAlreadyExistsException("User exists already: username=${user.username}")
        }
        
        val encodedPassword = BCryptPasswordEncoder(4).encode(user.password)
        
        val userEntity = UserEntity(null, user.username, encodedPassword, user.enabled, listOf(
                AuthorityEntity("USER")
        ))
        
        userRepository.save(userEntity)
    }

    /**
     * Updates the password for the given {@code user}.
     * The {@code UserPassword#password} field will be encrypted.
     *
     * @param user the user password to update
     */
    override fun update(user: UserPassword) {
        throw UnsupportedOperationException("This method is not implemented yet.") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * Updates the given {@code user}.
     *
     * @param user the user to update
     */
    override fun update(user: User) {
        
        val userEntity: UserEntity = userRepository.findOne(user.userId)
        
        userEntity.username = user.username
        userEntity.enabled = user.enabled
        
        userRepository.save(userEntity)
    }

    /**
     * @return all users
     */
    override fun getAll(): List<User> {
        
        return userRepository.findAll()
                .map { 
                    User(it.id!!, it.username, it.enabled)
                }
    }

    /**
     * Gets the user by the given {@code userId}.
     *
     * @param userId id of the user
     *
     * @return an {@code Optional} of the user
     */
    override fun getOne(userId: Int): User {
        
        val userEntity: UserEntity = userRepository.findOne(userId)
        
        return User(userEntity.id!!, userEntity.username, userEntity.enabled)
    }

    /**
     * Deletes the user matching the given {@code userId}.
     *
     * @param userId id of the user to delete
     */
    override fun delete(userId: Int) {
        userRepository.delete(userId)
    }
}
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

/**
 * Describes a manager for the setup of PSA.
 *
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.0.0
 */
interface SetupManager {

    /**
     * True if Sporttag PSA is initialized, otherwise false.
     */
    val isInitialized: Boolean

    /**
     * The JWT secret.
     */
    val jwtSecret: String

    /**
     * Initializes Sporttag PSA by consuming the given {@code setup}.
     *
     * This method will generate a random JWT secret and stores it in the setup.
     *
     * @param setup the setup information to consume
     *
     * @throws IllegalStateException if the setup is already initialized
     */
    fun initialize(setup: SetupInformation)

    /**
     * @param length the length of the JWT secret, must be at least 8 characters
     *
     * @return a random generated JWT secret in for of a hex number
     * @throws IllegalArgumentException if the length is less then 8
     */
    fun generateJWTSecret(length: Int = 8): String

    /**
     * Replaces the existing JWT secret with the given {@code secret}.
     *
     * @param secret the secret to use
     */
    fun replaceJWTSecret(secret: String)
}
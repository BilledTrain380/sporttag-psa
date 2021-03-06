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

package ch.schulealtendorf.psa.core.user.validation

/**
 * Describes a validator for a password.
 *
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.0.0
 */
interface PasswordValidator {

    /**
     * Checks the given {@code password} against a set of rules.
     *
     * @param password the password to check
     *
     * @return true if the password is valid, otherwise false
     */
    fun isValid(password: String): Boolean

    /**
     * Checks the given {@code password} against a set of rules.
     *
     * In contrast to the {@link PasswordValidator#isValid} method,
     * this method results a {@link ValidationResult} which contains
     * a message in case that the password is invalid.
     *
     * @param password the password to validate
     *
     * @return a validation result
     */
    fun validate(password: String): ValidationResult

    /**
     * Checks the given {@code password} against a set of rules,
     * as well as a case sensitive equals check against the given {@code passwordRepeat}
     *
     * @param password the password to validate
     * @param passwordRepeat the password repeat to equals check
     *
     * @return a validation result
     */
    fun validateEquals(password: String, passwordRepeat: String): ValidationResult
}

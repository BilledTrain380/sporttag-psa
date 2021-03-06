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

import mu.KotlinLogging
import org.passay.CharacterRule
import org.passay.EnglishCharacterData
import org.passay.LengthRule
import org.passay.PasswordData
import org.passay.RuleResult
import org.passay.WhitespaceRule
import org.springframework.stereotype.Component

@Component
class PSAPasswordValidator : PasswordValidator {
    private val log = KotlinLogging.logger {}

    private val validator = org.passay.PasswordValidator(
        LengthRule(8, 64),
        CharacterRule(EnglishCharacterData.Digit),
        CharacterRule(EnglishCharacterData.LowerCase),
        CharacterRule(EnglishCharacterData.UpperCase),
        CharacterRule(EnglishCharacterData.Alphabetical),
        CharacterRule(EnglishCharacterData.Special),
        WhitespaceRule()
    )

    override fun isValid(password: String): Boolean {
        return validator.validate(PasswordData(password)).isValid
    }

    override fun validate(password: String): ValidationResult {
        log.info { "Validate password" }

        val result: RuleResult = validator.validate(PasswordData(password))

        return ValidationResult(result.isValid, validator.getMessages(result))
    }

    override fun validateEquals(password: String, passwordRepeat: String): ValidationResult {
        val validationResult = validate(password)

        if (password != passwordRepeat) {
            return validationResult.copy(
                isValid = false,
                messages = validationResult.messages.plus("Passwords do not match.")
            )
        }

        return validationResult
    }
}

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

package ch.schulealtendorf.psa.web

import ch.schulealtendorf.psa.core.setup.SetupInformation
import ch.schulealtendorf.psa.core.setup.SetupManager
import ch.schulealtendorf.psa.core.user.validation.PasswordValidator
import mu.KotlinLogging
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@Controller
class SetupController(
    private val setupManager: SetupManager,
    private val passwordValidator: PasswordValidator
) {
    private val log = KotlinLogging.logger {}

    @GetMapping("/setup")
    fun index(model: Model): String {
        model.addAttribute("setupForm", SetupForm.empty())
        return "setup/index"
    }

    @PostMapping("/setup")
    fun applySetup(
        @Valid @ModelAttribute setupForm: SetupForm,
        request: HttpServletRequest,
        redirectAttributes: RedirectAttributes
    ): String {
        log.info { "Perform setup" }

        val setupInformation = SetupInformation(setupForm.password)
        val validationResult = passwordValidator.validate(setupInformation.adminPassword)

        if (validationResult.isValid) {
            setupManager.initialize(setupInformation)
            redirectAttributes.addFlashAttribute("hasBeenInitialized", true)
            return "redirect:/login"
        }

        redirectAttributes.addFlashAttribute("pwValidationErrors", validationResult.messages)
        return "redirect:/setup"
    }
}

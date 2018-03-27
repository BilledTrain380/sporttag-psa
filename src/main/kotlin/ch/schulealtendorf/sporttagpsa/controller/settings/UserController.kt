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

package ch.schulealtendorf.sporttagpsa.controller.settings

import ch.schulealtendorf.sporttagpsa.business.user.FreshUser
import ch.schulealtendorf.sporttagpsa.business.user.User
import ch.schulealtendorf.sporttagpsa.business.user.UserManager
import ch.schulealtendorf.sporttagpsa.business.user.UserPassword
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import javax.validation.Valid

@Controller
@RequestMapping("/settings/user")
class UserController(
        private val userManager: UserManager
) {
    
    companion object {
        private const val USER_ACTION_STATUS = "user_action_status"
        private const val EDITED_SUCCESSFUL = "edited_successful"
        private const val ADDED_SUCCESSFUL = "added_successful"
        private const val DELETED_SUCCESSFUL = "deleted_successful"
    }
    
    @GetMapping
    fun index(model: Model): String {
        
        model.addAttribute("userList", userManager.getAll().filter { it.username != "admin" })
        model.addAttribute("userForm", UserForm())
        
        return "settings/user/user-list"
    }
    
    @PostMapping
    fun addUser(@Valid @ModelAttribute("userForm") user: UserForm, redirectedAttributes: RedirectAttributes): String {
        
        if (user.password != user.passwordRepeat) {
            
            redirectedAttributes.addFlashAttribute("invalidForm", "passwordMissMatch")
            
            return "redirect:/settings/user"
        }
        
        userManager.create(FreshUser(user.username, user.password, user.enabled))
        
        redirectedAttributes.addFlashAttribute(USER_ACTION_STATUS, ADDED_SUCCESSFUL)
        
        return "redirect:/settings/user"
    }
    
    @GetMapping("/{id}")
    fun editUser(@PathVariable id: Int, model: Model): String {
        
        val user = userManager.getOne(id)
        
        model.addAttribute("user", UserEditForm(user.userId, user.username, user.enabled))
        
        return "settings/user/user-edit"
    }
    
    @PostMapping("/{id}")
    fun editUser(@Valid @ModelAttribute("user") userEditForm: UserEditForm, @PathVariable id: Int, redirectedAttributes: RedirectAttributes): String {
        
        val user = User(id, userEditForm.username, userEditForm.enabled)
        
        userManager.update(user)
        
        redirectedAttributes.addFlashAttribute(USER_ACTION_STATUS, EDITED_SUCCESSFUL)
        
        return "redirect:/settings/user"
    }
    
    @GetMapping("/{id}/passwd")
    fun changePassword(@PathVariable id: Int, model: Model): String {
        
        val user = userManager.getOne(id)
        
        model.addAttribute("userId", user.userId)
        model.addAttribute("username", user.username)
        model.addAttribute("userPasswd", UserPasswdForm())
        
        return "settings/user/user-passwd"
    }
    
    @PostMapping("/{id}/passwd")
    fun changePassword(@Valid @ModelAttribute("userPasswd") userPasswdForm: UserPasswdForm, @PathVariable id: Int, redirectedAttributes: RedirectAttributes): String {

        if (userPasswdForm.password != userPasswdForm.passwordRepeat) {

            redirectedAttributes.addFlashAttribute("invalidForm", "passwordMissMatch")

            return "redirect:/settings/user/$id/passwd"
        }
        
        val userPassword = UserPassword(id, userPasswdForm.password)
        
        userManager.update(userPassword)
        
        redirectedAttributes.addFlashAttribute(USER_ACTION_STATUS, EDITED_SUCCESSFUL)
        
        return "redirect:/settings/user"
    }
    
    @GetMapping("/{id}/delete")
    fun delete(@PathVariable id: Int, model: Model): String {
        
        model.addAttribute("userId", id)
        
        return "settings/user/user-delete"
    }
    
    @PostMapping("/{id}/delete")
    fun delete(@PathVariable id: Int, redirectedAttributes: RedirectAttributes): String {
        
        userManager.delete(id)
        
        redirectedAttributes.addFlashAttribute(USER_ACTION_STATUS, DELETED_SUCCESSFUL)
        
        return "redirect:/settings/user"
    }
}
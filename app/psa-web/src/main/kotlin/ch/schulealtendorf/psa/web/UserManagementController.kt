package ch.schulealtendorf.psa.web

import ch.schulealtendorf.psa.core.user.UserManager
import ch.schulealtendorf.psa.core.user.validation.PasswordValidator
import ch.schulealtendorf.psa.core.web.UnauthorizedException
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping("/user")
class UserManagementController(
    private val passwordValidator: PasswordValidator,
    private val userManager: UserManager
) {

    @GetMapping("/change-pw")
    fun changePasswordPage(): String {
        return "change-password"
    }

    @PostMapping("/change-pw")
    fun changePassword(
        @ModelAttribute form: ChangePasswordForm,
        request: HttpServletRequest,
        redirectAttributes: RedirectAttributes,
    ): String {
        val validationResult = passwordValidator.validateEquals(form.password, form.passwordRepeat)

        if (validationResult.isValid) {
            val user = userManager.getOne(request.userPrincipal.name)
                .orElseThrow { UnauthorizedException() }

            userManager.changePassword(user, form.password)

            request.logout()
            return "redirect:/app"
        }

        redirectAttributes.addFlashAttribute("pwValidationErrors", validationResult.messages)
        return "redirect:/user/change-pw"
    }
}

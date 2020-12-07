package ch.schulealtendorf.psa.web

import ch.schulealtendorf.psa.configuration.test.PsaWebMvcTest
import ch.schulealtendorf.psa.configuration.test.formContent
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

internal class UserManagementControllerTest : PsaWebMvcTest() {

    @Test
    internal fun getChangePasswordPageWhenUnauthorized() {
        mockMvc.perform(get("/user/change-pw"))
            .andExpect(status().is3xxRedirection)
    }

    @Test
    internal fun getChangePasswordPageWhenAuthorized() {
        mockMvc.perform(
            get("/user/change-pw")
                .with(user(ADMIN_USER))
        ).andExpect(status().isOk)
    }

    @Test
    internal fun changePasswordWhenInvalidPassword() {
        val form = ChangePasswordForm("pass", "pass")

        mockMvc.perform(
            post("/user/change-pw")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .formContent(form)
                .with(user(ADMIN_USER))
        ).andExpect(status().is3xxRedirection)
            .andExpect { assertThat(it.response.redirectedUrl).contains("/user/change-pw") }
            .andExpect(flash().attributeExists("pwValidationErrors"))
    }

    @Test
    internal fun changePasswordWhenValidPassword() {
        val validPassword = "Psa123456$"
        val form = ChangePasswordForm(validPassword, validPassword)

        mockMvc.perform(
            post("/user/change-pw")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .formContent(form)
                .with(user(ADMIN_USER))
        ).andExpect(status().is3xxRedirection)
            .andExpect { assertThat(it.response.redirectedUrl).contains("/app") }
    }
}

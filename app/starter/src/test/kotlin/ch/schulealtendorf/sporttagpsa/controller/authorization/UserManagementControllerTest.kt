package ch.schulealtendorf.sporttagpsa.controller.authorization

import ch.schulealtendorf.sporttagpsa.controller.PsaWebMvcTest
import ch.schulealtendorf.sporttagpsa.controller.formContent
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash

internal class UserManagementControllerTest : PsaWebMvcTest() {

    @Test
    internal fun getChangePasswordPageWhenUnauthorized() {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/change-pw"))
            .andExpect(MockMvcResultMatchers.status().is3xxRedirection)
    }

    @Test
    internal fun getChangePasswordPageWhenAuthorized() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/user/change-pw")
                .with(user(ADMIN_USER))
        ).andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    internal fun changePasswordWhenInvalidPassword() {
        val form = ChangePasswordForm("pass", "pass")

        mockMvc.perform(
            MockMvcRequestBuilders.post("/user/change-pw")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .formContent(form)
                .with(user(ADMIN_USER))
        ).andExpect(MockMvcResultMatchers.status().is3xxRedirection)
            .andExpect { Assertions.assertThat(it.response.redirectedUrl).contains("/user/change-pw") }
            .andExpect(flash().attributeExists("pwValidationErrors"))
    }

    @Test
    internal fun changePasswordWhenValidPassword() {
        val validPassword = "Psa123456$"
        val form = ChangePasswordForm(validPassword, validPassword)

        mockMvc.perform(
            MockMvcRequestBuilders.post("/user/change-pw")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .formContent(form)
                .with(user(ADMIN_USER))
        ).andExpect(MockMvcResultMatchers.status().is3xxRedirection)
            .andExpect { Assertions.assertThat(it.response.redirectedUrl).contains("/app") }
    }
}

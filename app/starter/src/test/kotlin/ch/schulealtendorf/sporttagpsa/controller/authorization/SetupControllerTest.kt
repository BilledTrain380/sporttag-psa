package ch.schulealtendorf.sporttagpsa.controller.authorization

import ch.schulealtendorf.sporttagpsa.controller.PsaWebMvcTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.model
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest(properties = ["spring.flyway.locations=classpath:/db/migration"])
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
internal class SetupControllerTest : PsaWebMvcTest() {
    companion object {
        private const val SETUP_PATH = "/setup"
    }

    @Test
    internal fun getSetupPage() {
        mockMvc.perform(get(SETUP_PATH))
            .andExpect(status().isOk)
            .andExpect(model().attributeExists("setupForm"))
    }

    @Test
    internal fun getStaticResources() {
        mockMvc.perform(get("/favicon.ico"))
            .andExpect(status().isOk)
    }

    @Test
    internal fun applySetup() {
        val form = SetupForm(password = "Secrete12345$")

        mockMvc.perform(
            post(SETUP_PATH)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .formContent(form)
        ).andExpect(status().is3xxRedirection)
            .andExpect { assertThat(it.response.redirectedUrl).doesNotContain(SETUP_PATH) }

        // Setup should no longer be accessible now
        mockMvc.perform(get(SETUP_PATH))
            .andExpect(status().isNotFound)

        // Static resources still have to be accessible
        mockMvc.perform(get("/favicon.ico"))
            .andExpect(status().isOk)
    }

    @Test
    internal fun applySetupWhenPasswordPolicyDoesNotMatch() {
        val form = SetupForm(password = "too weak")

        mockMvc.perform(
            post(SETUP_PATH)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .formContent(form)
        ).andExpect(status().is3xxRedirection)
            .andExpect { assertThat(it.response.redirectedUrl).contains(SETUP_PATH) }
            .andExpect(flash().attributeExists("pwValidationErrors"))
    }
}

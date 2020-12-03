package ch.schulealtendorf.psa.web

import ch.schulealtendorf.psa.configuration.test.formContent
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.model
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ActiveProfiles("test")
@Tag("ctrl-test")
@SpringBootTest(
    properties = [
        "spring.flyway.locations=classpath:/db/migration",
        "spring.datasource.url=jdbc:h2:mem:psa-4833e875-390b-4897-b7f4-3b3e8bbd2e40;USER=psa;PASSWORD=psa"
    ]
)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
internal class SetupControllerTest {
    companion object {
        private const val SETUP_PATH = "/setup"
    }

    @Autowired
    private lateinit var mockMvc: MockMvc

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
            .andExpect(flash().attributeExists("hasBeenInitialized"))

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

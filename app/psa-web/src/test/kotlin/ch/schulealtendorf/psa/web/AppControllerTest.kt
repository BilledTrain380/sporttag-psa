package ch.schulealtendorf.psa.web

import ch.schulealtendorf.psa.configuration.test.PsaWebMvcTest
import ch.schulealtendorf.psa.core.user.USER_ADMIN
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

internal class AppControllerTest : PsaWebMvcTest() {

    @Test
    internal fun redirectWhenAuthorized() {
        mockMvc.perform(
            get("/app")
                .with(user(USER_ADMIN))
        ).andExpect(status().is3xxRedirection)
            .andExpect { assertThat(it.response.redirectedUrl).contains("app/en/index.html") }
    }

    @Test
    internal fun redirectWhenUnauthorized() {
        mockMvc.perform(get("/app"))
            .andExpect(status().is3xxRedirection)
            .andExpect { assertThat(it.response.redirectedUrl).contains("login") }
    }

    @Test
    internal fun forwardToAppWithUser() {
        mockMvc.perform(
            get("/app/de")
                .with(user(USER_ADMIN))
        ).andExpect(status().isOk)
            .andExpect { assertThat(it.response.forwardedUrl).contains("app/en/index.html") }
    }

    @Test
    internal fun forwardToAppWithoutUser() {
        mockMvc.perform(get("/app/en/pages/athletics"))
            .andExpect(status().isOk)
            .andExpect { assertThat(it.response.forwardedUrl).contains("/app/en/index.html") }
    }
}

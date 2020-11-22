package ch.schulealtendorf.psa.web

import ch.schulealtendorf.psa.configuration.test.PsaWebMvcTest
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.model
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

internal class HomeControllerTest : PsaWebMvcTest() {

    @Test
    internal fun getHomepageWhenUnauthorized() {
        mockMvc.perform(get("/"))
            .andExpect(status().isOk)
            .andExpect(model().attribute("isAuthenticated", false))
    }

    @Test
    internal fun getHomepageWhenAuthorized() {
        mockMvc.perform(
            get("/")
                .with(user(ADMIN_USER))
        ).andExpect(status().isOk)
            .andExpect(model().attribute("isAuthenticated", true))
            .andExpect(model().attribute("username", ADMIN_USER))
    }
}

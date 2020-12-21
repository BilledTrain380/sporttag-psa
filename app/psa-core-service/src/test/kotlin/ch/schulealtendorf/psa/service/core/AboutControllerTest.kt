package ch.schulealtendorf.psa.service.core

import ch.schulealtendorf.psa.configuration.test.PsaWebMvcTest
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

internal class AboutControllerTest : PsaWebMvcTest() {
    @Test
    internal fun getBuildInfo() {
        mockMvc.perform(
            get("/api/build-info")
                .with(bearerTokenUser())
        ).andExpect(status().isOk)
    }

    @Test
    internal fun getBuildInfoWhenUnauthorized() {
        mockMvc.perform(get("/api/build-info"))
            .andExpect(status().isUnauthorized)
    }
}

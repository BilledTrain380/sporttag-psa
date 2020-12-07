package ch.schulealtendorf.psa.service.user

import ch.schulealtendorf.psa.configuration.test.PsaWebMvcTest
import ch.schulealtendorf.psa.core.i18n.PsaLocale
import ch.schulealtendorf.psa.dto.oauth.PSAScope
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

internal class ProfileControllerTest : PsaWebMvcTest() {

    @Test
    internal fun updateLocale() {
        mockMvc.perform(
            MockMvcRequestBuilders.patch("/api/profile")
                .with(bearerTokenUser(PSAScope.PROFILE))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBodyOf(PsaLocale.DE))
        ).andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    internal fun updateLocaleWhenUnauthorized() {
        mockMvc.perform(
            MockMvcRequestBuilders.patch("/api/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBodyOf(PsaLocale.DE))
        ).andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }

    @Test
    internal fun updateLocaleWhenMissingScope() {
        mockMvc.perform(
            MockMvcRequestBuilders.patch("/api/profile")
                .with(bearerTokenAdmin(PSAScope.USER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBodyOf(PsaLocale.DE))
        ).andExpect(MockMvcResultMatchers.status().isNotFound)
    }
}

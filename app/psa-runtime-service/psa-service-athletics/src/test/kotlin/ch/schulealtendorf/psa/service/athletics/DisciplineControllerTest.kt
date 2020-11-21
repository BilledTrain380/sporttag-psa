package ch.schulealtendorf.psa.service.athletics

import ch.schulealtendorf.psa.configuration.test.PsaWebMvcTest
import ch.schulealtendorf.psa.configuration.web.oauth.PSAScope
import ch.schulealtendorf.psa.dto.participation.athletics.SCHNELLLAUF
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

internal class DisciplineControllerTest : PsaWebMvcTest() {
    companion object {
        private const val DISCIPLINE_ENDPOINT = "/api/disciplines"
        private const val SCHNELLLAUF_ENDPOINT = "/api/discipline/$SCHNELLLAUF"
    }

    @Test
    internal fun getDisciplines() {
        mockMvc.perform(
            get(DISCIPLINE_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.DISCIPLINE_READ))
        ).andExpect(status().isOk)

        mockMvc.perform(
            get(DISCIPLINE_ENDPOINT)
                .with(bearerTokenUser(PSAScope.DISCIPLINE_READ))
        ).andExpect(status().isOk)
    }

    @Test
    internal fun getDisciplinesWhenUnauthorized() {
        mockMvc.perform(get(DISCIPLINE_ENDPOINT))
            .andExpect(status().isUnauthorized)
    }

    @Test
    internal fun getDisciplinesWhenMissingScope() {
        mockMvc.perform(
            get(DISCIPLINE_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.COMPETITOR_READ))
        ).andExpect(status().isNotFound)
    }

    @Test
    internal fun getDisciplineByName() {
        mockMvc.perform(
            get(SCHNELLLAUF_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.DISCIPLINE_READ))
        ).andExpect(status().isOk)

        mockMvc.perform(
            get(SCHNELLLAUF_ENDPOINT)
                .with(bearerTokenUser(PSAScope.DISCIPLINE_READ))
        ).andExpect(status().isOk)
    }

    @Test
    internal fun getDisciplineWhenUnauthorized() {
        mockMvc.perform(get(DISCIPLINE_ENDPOINT))
            .andExpect(status().isUnauthorized)
    }

    @Test
    internal fun getDisciplineWhenMissingScope() {
        mockMvc.perform(
            get(SCHNELLLAUF_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.COMPETITOR_READ))
        ).andExpect(status().isNotFound)
    }
}

package ch.schulealtendorf.psa.service.athletics

import ch.schulealtendorf.psa.configuration.test.PsaWebMvcTest
import ch.schulealtendorf.psa.dto.participation.athletics.ResultElement
import ch.schulealtendorf.psa.service.standard.manager.ParticipationManager
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

internal class CompetitorControllerTest : PsaWebMvcTest() {
    companion object {
        private const val COMPETITORS_ENDPOINT = "/api/competitors"
        private const val COMPETITOR_1_ENDPOINT = "/api/competitor/7"
    }

    @Autowired
    private lateinit var participationManager: ParticipationManager

    @BeforeEach
    internal fun beforeEach() {
        participationManager.closeParticipation()
    }

    @Test
    internal fun getCompetitors() {
        mockMvc.perform(
            get(COMPETITORS_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.COMPETITOR_READ))
        ).andExpect(status().isOk)

        mockMvc.perform(
            get(COMPETITORS_ENDPOINT)
                .with(bearerTokenUser(PSAScope.COMPETITOR_READ))
        ).andExpect(status().isOk)
    }

    @Test
    internal fun getCompetitorsWhenUnauthorized() {
        mockMvc.perform(get(COMPETITORS_ENDPOINT))
            .andExpect(status().isUnauthorized)
    }

    @Test
    internal fun getCompetitorsWhenMissingScope() {
        mockMvc.perform(
            get(COMPETITORS_ENDPOINT).with(bearerTokenAdmin(PSAScope.GROUP_READ))
        ).andExpect(status().isNotFound)
    }

    @Test
    internal fun getCompetitor() {
        mockMvc.perform(
            get(COMPETITOR_1_ENDPOINT).with(bearerTokenAdmin(PSAScope.COMPETITOR_READ))
        ).andExpect(status().isOk)

        mockMvc.perform(
            get(COMPETITOR_1_ENDPOINT).with(bearerTokenUser(PSAScope.COMPETITOR_READ))
        ).andExpect(status().isOk)
    }

    @Test
    internal fun getCompetitorWhenUnauthorized() {
        mockMvc.perform(get(COMPETITOR_1_ENDPOINT))
            .andExpect(status().isUnauthorized)
    }

    @Test
    internal fun getCompetitorWhenMissingScope() {
        mockMvc.perform(
            get(COMPETITOR_1_ENDPOINT).with(bearerTokenAdmin(PSAScope.GROUP_READ))
        ).andExpect(status().isNotFound)
    }

    @Test
    @DirtiesContext
    internal fun updateCompetitorResult() {
        val resultElement = ResultElement(id = 1, value = 5)

        mockMvc.perform(
            put(COMPETITOR_1_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.COMPETITOR_WRITE))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBodyOf(resultElement))
        ).andExpect(status().isOk)

        mockMvc.perform(
            put(COMPETITOR_1_ENDPOINT)
                .with(bearerTokenUser(PSAScope.COMPETITOR_WRITE))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBodyOf(resultElement))
        ).andExpect(status().isOk)
    }

    @Test
    internal fun updateCompetitorResultsWhenUnauthorized() {
        mockMvc.perform(
            put(COMPETITOR_1_ENDPOINT)
        ).andExpect(status().isUnauthorized)
    }

    @Test
    internal fun updateCompetitorResultWhenMissingScope() {
        val resultElement = ResultElement(id = 1, value = 5)

        mockMvc.perform(
            put(COMPETITOR_1_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.COMPETITOR_READ))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBodyOf(resultElement))
        ).andExpect(status().isNotFound)
    }
}

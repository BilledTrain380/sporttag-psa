package ch.schulealtendorf.sporttagpsa.controller.resource

import ch.schulealtendorf.psa.dto.participation.ParticipationCommand
import ch.schulealtendorf.sporttagpsa.controller.PsaWebMvcTest
import ch.schulealtendorf.sporttagpsa.controller.oauth.PSAScope
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

internal class ParticipationControllerTest : PsaWebMvcTest() {
    companion object {
        private const val PARTICIPATION_ENDPOINT = "/api/participation"
        private const val SPORTS_ENDPOINT = "/api/sports"
    }

    @Test
    internal fun getParticipation() {
        mockMvc.perform(
            get(PARTICIPATION_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.PARTICIPATION))
        ).andExpect(status().isOk)

        mockMvc.perform(
            get(PARTICIPATION_ENDPOINT)
                .with(bearerTokenUser(PSAScope.PARTICIPATION))
        ).andExpect(status().isOk)
    }

    @Test
    internal fun getParticipationWhenUnauthorized() {
        mockMvc.perform(get(PARTICIPATION_ENDPOINT))
            .andExpect(status().isUnauthorized)
    }

    @Test
    internal fun getParticipationWhenMissingScope() {
        mockMvc.perform(
            get(PARTICIPATION_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.PARTICIPANT_WRITE))
        ).andExpect(status().isNotFound)
    }

    @Test
    internal fun updateParticipation() {
        mockMvc.perform(
            patch(PARTICIPATION_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.PARTICIPATION))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBodyOf(ParticipationCommand.CLOSE))
        ).andExpect(status().isOk)
    }

    @Test
    internal fun updateParticipationWhenUnauthorized() {
        mockMvc.perform(
            patch(PARTICIPATION_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBodyOf(ParticipationCommand.CLOSE))
        ).andExpect(status().isUnauthorized)
    }

    @Test
    internal fun updateParticipationWhenMissingScope() {
        mockMvc.perform(
            patch(PARTICIPATION_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.PARTICIPANT_WRITE))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBodyOf(ParticipationCommand.CLOSE))
        ).andExpect(status().isNotFound)
    }

    @Test
    internal fun updateParticipationWhenMissingAuthority() {
        mockMvc.perform(
            patch(PARTICIPATION_ENDPOINT)
                .with(bearerTokenUser(PSAScope.PARTICIPATION))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBodyOf(ParticipationCommand.CLOSE))
        ).andExpect(status().isNotFound)
    }

    @Test
    internal fun getSportTypes() {
        mockMvc.perform(
            get(SPORTS_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.SPORT_READ))
        ).andExpect(status().isOk)

        mockMvc.perform(
            get(SPORTS_ENDPOINT)
                .with(bearerTokenUser(PSAScope.SPORT_READ))
        ).andExpect(status().isOk)
    }

    @Test
    internal fun getSportTypesWhenUnauthorized() {
        mockMvc.perform(get(SPORTS_ENDPOINT))
            .andExpect(status().isUnauthorized)
    }

    @Test
    internal fun getSportTypesWhenMissingScope() {
        mockMvc.perform(
            get(SPORTS_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.PARTICIPANT_WRITE))
        ).andExpect(status().isNotFound)
    }
}
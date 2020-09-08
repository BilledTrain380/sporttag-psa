package ch.schulealtendorf.sporttagpsa.controller.resource

import ch.schulealtendorf.psa.dto.participation.ATHLETICS
import ch.schulealtendorf.psa.dto.participation.ParticipationCommand
import ch.schulealtendorf.psa.dto.participation.SportDto
import ch.schulealtendorf.sporttagpsa.controller.PsaWebMvcTest
import ch.schulealtendorf.sporttagpsa.controller.oauth.PSAScope
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

internal class ParticipationControllerTest : PsaWebMvcTest() {
    companion object {
        private const val PARTICIPATION_ENDPOINT = "/api/participation"
        private const val PARTICIPATION_STATUS_ENDPOINT = "/api/participation-status"
        private const val SPORTS_ENDPOINT = "/api/sports"
        private const val PARTICIPATION_LIST_DOWNLOAD_ENDPOINT = "/api/participation-list/download"
        private const val START_LIST_DOWNLOAD_ENDPOINT = "/api/start-list/download"

        private val PARTICIPANT_List_BODY = listOf(
            SportDto(ATHLETICS)
        )
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
    internal fun getParticipationStatus() {
        mockMvc.perform(
            get(PARTICIPATION_STATUS_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.PARTICIPATION))
        ).andExpect(status().isOk)

        mockMvc.perform(
            get(PARTICIPATION_STATUS_ENDPOINT)
                .with(bearerTokenUser(PSAScope.PARTICIPATION))
        ).andExpect(status().isOk)
    }

    @Test
    internal fun getParticipationStatusWhenUnauthorized() {
        mockMvc.perform(get(PARTICIPATION_STATUS_ENDPOINT))
            .andExpect(status().isUnauthorized)
    }

    @Test
    internal fun getParticipationStatusWhenMissingScope() {
        mockMvc.perform(
            get(PARTICIPATION_STATUS_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.PARTICIPANT_WRITE))
        ).andExpect(status().isNotFound)
    }

    @Test
    @DirtiesContext
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

    @Test
    internal fun downloadParticipationList() {
        mockMvc.perform(
            post(PARTICIPATION_LIST_DOWNLOAD_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.PARTICIPATION))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBodyOf(PARTICIPANT_List_BODY))
        ).andExpect(status().isOk)
    }

    @Test
    internal fun downloadParticipationListWhenUnauthorized() {
        mockMvc.perform(
            post(PARTICIPATION_LIST_DOWNLOAD_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBodyOf(PARTICIPANT_List_BODY))
        ).andExpect(status().isUnauthorized)
    }

    @Test
    internal fun downloadParticipationListWhenMissingScope() {
        mockMvc.perform(
            post(PARTICIPATION_LIST_DOWNLOAD_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.COMPETITOR_WRITE))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBodyOf(PARTICIPANT_List_BODY))
        ).andExpect(status().isNotFound)
    }

    @Test
    internal fun downloadParticipationListWhenMissingAuthority() {
        mockMvc.perform(
            post(PARTICIPATION_LIST_DOWNLOAD_ENDPOINT)
                .with(bearerTokenUser(PSAScope.PARTICIPATION))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBodyOf(PARTICIPANT_List_BODY))
        ).andExpect(status().isNotFound)
    }

    @Test
    internal fun downloadStartList() {
        mockMvc.perform(
            get(START_LIST_DOWNLOAD_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.PARTICIPATION))
        ).andExpect(status().isOk)
    }

    @Test
    internal fun downloadStartListWhenUnauthorized() {
        mockMvc.perform(get(START_LIST_DOWNLOAD_ENDPOINT))
            .andExpect(status().isUnauthorized)
    }

    @Test
    internal fun downloadStartListWhenMissingScope() {
        mockMvc.perform(
            get(START_LIST_DOWNLOAD_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.COMPETITOR_WRITE))
        ).andExpect(status().isNotFound)
    }

    @Test
    internal fun downloadStartListWhenMissingAuthority() {
        mockMvc.perform(
            get(START_LIST_DOWNLOAD_ENDPOINT)
                .with(bearerTokenUser(PSAScope.PARTICIPATION))
        ).andExpect(status().isNotFound)
    }
}

package ch.schulealtendorf.sporttagpsa.controller.resource

import ch.schulealtendorf.psa.dto.participation.ATHLETICS
import ch.schulealtendorf.psa.dto.participation.BirthdayDto
import ch.schulealtendorf.psa.dto.participation.GenderDto
import ch.schulealtendorf.psa.dto.participation.ParticipantElement
import ch.schulealtendorf.psa.dto.participation.ParticipantInput
import ch.schulealtendorf.psa.dto.participation.ParticipantRelation
import ch.schulealtendorf.psa.dto.participation.TownDto
import ch.schulealtendorf.sporttagpsa.controller.PsaWebMvcTest
import ch.schulealtendorf.sporttagpsa.controller.oauth.PSAScope
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

internal class ParticipantControllerTest : PsaWebMvcTest() {
    companion object {
        private const val PARTICIPANTS_ENDPOINT = "/api/participants"
        private const val PARTICIPANT_1_ENDPOINT = "/api/participant/1"
        private const val PARTICIPANT_2_ENDPOINT = "/api/participant/2"

        private val PARTICIPANT_INPUT = ParticipantInput(
            prename = "Willi",
            surname = "Wirbelwind",
            gender = GenderDto.MALE,
            address = "Musterstrasse 21",
            birthday = BirthdayDto.parse("2020-01-01"),
            group = "2a",
            town = TownDto("8000", "Zürich")
        )

        private val PARTICIPANT_ELEMENT = ParticipantElement(
            id = 1,
            prename = "Fritz",
            surname = "Fisher",
            gender = GenderDto.MALE,
            birthday = BirthdayDto.parse("2020-01-02"),
            address = "Fischerstrasse 65",
            isAbsent = true,
            town = TownDto(
                zip = "8000",
                name = "Zürich"
            )
        )

        private val PARTICIPANT_RELATION = ParticipantRelation(
            sportType = ATHLETICS
        )
    }

    @Test
    internal fun getParticipants() {
        mockMvc.perform(
            get(PARTICIPANTS_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.PARTICIPANT_READ))
        ).andExpect(status().isOk)
    }

    @Test
    internal fun getParticipantsWhenUnauthorized() {
        mockMvc.perform(get(PARTICIPANTS_ENDPOINT))
            .andExpect(status().isUnauthorized)
    }

    @Test
    internal fun getParticipantsWhenMissingScope() {
        mockMvc.perform(
            get(PARTICIPANTS_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.COMPETITOR_READ))
        ).andExpect(status().isNotFound)
    }

    @Test
    internal fun getParticipantsWhenMissingAuthority() {
        mockMvc.perform(
            get(PARTICIPANTS_ENDPOINT)
                .with(bearerTokenUser(PSAScope.PARTICIPANT_READ))
        ).andExpect(status().isNotFound)
    }

    @Test
    internal fun getParticipantById() {
        mockMvc.perform(
            get(PARTICIPANT_1_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.PARTICIPANT_READ))
        ).andExpect(status().isOk)
    }

    @Test
    internal fun getParticipantByIdWhenUnauthorized() {
        mockMvc.perform(get(PARTICIPANT_1_ENDPOINT))
            .andExpect(status().isUnauthorized)
    }

    @Test
    internal fun getParticipantByIdWhenMissingScope() {
        mockMvc.perform(
            get(PARTICIPANT_1_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.COMPETITOR_READ))
        ).andExpect(status().isNotFound)
    }

    @Test
    internal fun getParticipantWhenMissingAuthority() {
        mockMvc.perform(
            get(PARTICIPANT_1_ENDPOINT)
                .with(bearerTokenUser(PSAScope.PARTICIPANT_READ))
        ).andExpect(status().isNotFound)
    }

    @Test
    @DirtiesContext
    internal fun createParticipant() {
        mockMvc.perform(
            post(PARTICIPANTS_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.PARTICIPANT_WRITE))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBodyOf(PARTICIPANT_INPUT))
        ).andExpect(status().isOk)
    }

    @Test
    internal fun createParticipantWhenUnauthorized() {
        mockMvc.perform(post(PARTICIPANTS_ENDPOINT))
            .andExpect(status().isUnauthorized)
    }

    @Test
    internal fun createParticipantWhenMissingScope() {
        mockMvc.perform(
            post(PARTICIPANTS_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.COMPETITOR_WRITE))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBodyOf(PARTICIPANT_INPUT))
        ).andExpect(status().isNotFound)
    }

    @Test
    internal fun createParticipantWhenMissingAuthority() {
        mockMvc.perform(
            post(PARTICIPANTS_ENDPOINT)
                .with(bearerTokenUser(PSAScope.PARTICIPANT_WRITE))
        ).andExpect(status().isNotFound)
    }

    @Test
    @DirtiesContext
    internal fun updateParticipant() {
        mockMvc.perform(
            patch(PARTICIPANT_1_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.PARTICIPANT_WRITE))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBodyOf(PARTICIPANT_ELEMENT))
        ).andExpect(status().isOk)
    }

    @Test
    internal fun updateParticipantWhenUnauthorized() {
        mockMvc.perform(patch(PARTICIPANT_1_ENDPOINT))
            .andExpect(status().isUnauthorized)
    }

    @Test
    internal fun updateParticipantWhenMissingScope() {
        mockMvc.perform(
            patch(PARTICIPANT_1_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.COMPETITOR_WRITE))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBodyOf(PARTICIPANT_ELEMENT))
        ).andExpect(status().isNotFound)
    }

    @Test
    internal fun updateParticipantWhenMissingAuthority() {
        mockMvc.perform(
            patch(PARTICIPANT_1_ENDPOINT)
                .with(bearerTokenUser(PSAScope.PARTICIPANT_WRITE))
        ).andExpect(status().isNotFound)
    }

    @Test
    @DirtiesContext
    internal fun updateParticipantRelation() {
        mockMvc.perform(
            put(PARTICIPANT_1_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.PARTICIPANT_WRITE))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBodyOf(PARTICIPANT_RELATION))
        ).andExpect(status().isOk)
    }

    @Test
    internal fun updateParticipantRelationWhenUnauthorized() {
        mockMvc.perform(put(PARTICIPANT_1_ENDPOINT))
            .andExpect(status().isUnauthorized)
    }

    @Test
    internal fun updateParticipantRelationWhenMissingScope() {
        mockMvc.perform(
            put(PARTICIPANT_1_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.COMPETITOR_WRITE))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBodyOf(PARTICIPANT_RELATION))
        ).andExpect(status().isNotFound)
    }

    @Test
    internal fun updateParticipantRelationWhenMissingAuthority() {
        mockMvc.perform(
            put(PARTICIPANT_1_ENDPOINT)
                .with(bearerTokenUser(PSAScope.PARTICIPANT_WRITE))
        ).andExpect(status().isNotFound)
    }

    @Test
    @DirtiesContext
    internal fun deleteParticipant() {
        mockMvc.perform(
            delete(PARTICIPANT_2_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.PARTICIPANT_WRITE))
        ).andExpect(status().isOk)
    }

    @Test
    internal fun deleteParticipantWhenUnauthorized() {
        mockMvc.perform(delete(PARTICIPANT_2_ENDPOINT))
            .andExpect(status().isUnauthorized)
    }

    @Test
    internal fun deleteParticipantWhenMissingScope() {
        mockMvc.perform(
            delete(PARTICIPANT_2_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.COMPETITOR_WRITE))
        ).andExpect(status().isNotFound)
    }

    @Test
    internal fun deleteParticipantWhenMissingAuthority() {
        mockMvc.perform(
            delete(PARTICIPANT_2_ENDPOINT)
                .with(bearerTokenUser(PSAScope.PARTICIPANT_WRITE))
        ).andExpect(status().isNotFound)
    }
}

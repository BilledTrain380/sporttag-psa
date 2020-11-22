package ch.schulealtendorf.psa.service.event

import ch.schulealtendorf.psa.configuration.test.PsaWebMvcTest
import ch.schulealtendorf.psa.dto.event.EventSheetData
import ch.schulealtendorf.psa.dto.participation.ATHLETICS
import ch.schulealtendorf.psa.dto.participation.GenderDto
import ch.schulealtendorf.psa.dto.participation.SportDto
import ch.schulealtendorf.psa.dto.participation.athletics.SCHNELLLAUF
import ch.schulealtendorf.psa.service.standard.manager.ParticipationManager
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

internal class EventSheetControllerTest : PsaWebMvcTest() {

    @Autowired
    private lateinit var participationManager: ParticipationManager

    companion object {
        private const val EVENT_SHEET_DOWNLOAD_ENDPOINT = "/api/event-sheets/download/sheets"
        private const val PARTICIPATION_LIST_DOWNLOAD_ENDPOINT = "/api/event-sheets/download/participant-list"
        private const val START_LIST_DOWNLOAD_ENDPOINT = "/api/event-sheets/download/startlist"

        private val PARTICIPANT_List_BODY = listOf(
            SportDto(ATHLETICS)
        )

        private val EVENT_SHEET_DATA = EventSheetData(
            discipline = SCHNELLLAUF,
            gender = GenderDto.MALE,
            group = "2a"
        )
    }

    @BeforeEach
    internal fun beforeEach() {
        participationManager.closeParticipation()
    }

    @Test
    internal fun downloadEventSheets() {
        mockMvc.perform(
            post(EVENT_SHEET_DOWNLOAD_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.EVENT_SHEETS))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBodyOf(listOf(EVENT_SHEET_DATA)))
        ).andExpect(status().isOk)
    }

    @Test
    internal fun downloadEventSheetsWhenUnauthorized() {
        mockMvc.perform(
            post(EVENT_SHEET_DOWNLOAD_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBodyOf(listOf(EVENT_SHEET_DATA)))
        ).andExpect(status().isUnauthorized)
    }

    @Test
    internal fun downloadEventSheetsWhenMissingScope() {
        mockMvc.perform(
            post(EVENT_SHEET_DOWNLOAD_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.RANKING))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBodyOf(listOf(EVENT_SHEET_DATA)))
        ).andExpect(status().isNotFound)
    }

    @Test
    internal fun downloadEventSheetsWhenMissingAuthority() {
        mockMvc.perform(
            post(EVENT_SHEET_DOWNLOAD_ENDPOINT)
                .with(bearerTokenUser(PSAScope.EVENT_SHEETS))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBodyOf(listOf(EVENT_SHEET_DATA)))
        ).andExpect(status().isNotFound)
    }

    @Test
    internal fun downloadParticipationList() {
        mockMvc.perform(
            post(PARTICIPATION_LIST_DOWNLOAD_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.EVENT_SHEETS))
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
            MockMvcRequestBuilders.get(START_LIST_DOWNLOAD_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.EVENT_SHEETS))
        ).andExpect(status().isOk)
    }

    @Test
    internal fun downloadStartListWhenUnauthorized() {
        mockMvc.perform(MockMvcRequestBuilders.get(START_LIST_DOWNLOAD_ENDPOINT))
            .andExpect(status().isUnauthorized)
    }

    @Test
    internal fun downloadStartListWhenMissingScope() {
        mockMvc.perform(
            MockMvcRequestBuilders.get(START_LIST_DOWNLOAD_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.COMPETITOR_WRITE))
        ).andExpect(status().isNotFound)
    }

    @Test
    internal fun downloadStartListWhenMissingAuthority() {
        mockMvc.perform(
            MockMvcRequestBuilders.get(START_LIST_DOWNLOAD_ENDPOINT)
                .with(bearerTokenUser(PSAScope.PARTICIPATION))
        ).andExpect(status().isNotFound)
    }
}

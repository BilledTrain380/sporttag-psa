package ch.schulealtendorf.sporttagpsa.controller.resource

import ch.schulealtendorf.psa.dto.participation.GenderDto
import ch.schulealtendorf.psa.dto.participation.athletics.SCHNELLLAUF
import ch.schulealtendorf.sporttagpsa.business.participation.ParticipationManager
import ch.schulealtendorf.sporttagpsa.controller.PsaWebMvcTest
import ch.schulealtendorf.sporttagpsa.controller.oauth.PSAScope
import ch.schulealtendorf.sporttagpsa.controller.resource.models.EventSheetData
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

internal class EventSheetControllerTest : PsaWebMvcTest() {

    @Autowired
    private lateinit var participationManager: ParticipationManager

    companion object {
        private const val EVENT_SHEET_DOWNLOAD_ENDPOINT = "/api/event-sheets/download"

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
}

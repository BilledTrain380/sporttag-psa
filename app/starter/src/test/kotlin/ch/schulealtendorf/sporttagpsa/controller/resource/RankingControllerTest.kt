package ch.schulealtendorf.sporttagpsa.controller.resource

import ch.schulealtendorf.psa.dto.participation.GenderDto
import ch.schulealtendorf.psa.dto.participation.athletics.SCHNELLLAUF
import ch.schulealtendorf.sporttagpsa.business.participation.ParticipationManager
import ch.schulealtendorf.sporttagpsa.controller.PsaWebMvcTest
import ch.schulealtendorf.sporttagpsa.controller.oauth.PSAScope
import ch.schulealtendorf.sporttagpsa.controller.resource.models.DisciplineRanking
import ch.schulealtendorf.sporttagpsa.controller.resource.models.RankingData
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

internal class RankingControllerTest : PsaWebMvcTest() {
    companion object {
        private const val RANKING_DOWNLOAD_ENDPOINT = "/api/ranking/download"

        private val RANKING_DATA = RankingData(
            total = listOf(GenderDto.MALE),
            discipline = listOf(
                DisciplineRanking(
                    discipline = SCHNELLLAUF,
                    gender = GenderDto.MALE
                )
            )
        )
    }

    @Autowired
    private lateinit var participationManager: ParticipationManager

    @BeforeEach
    internal fun beforeEach() {
        participationManager.closeParticipation()
    }

    @Test
    internal fun downloadRanking() {
        mockMvc.perform(
            post(RANKING_DOWNLOAD_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.RANKING))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBodyOf(RANKING_DATA))
        ).andExpect(status().isOk)

        mockMvc.perform(
            post(RANKING_DOWNLOAD_ENDPOINT)
                .with(bearerTokenUser(PSAScope.RANKING))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBodyOf(RANKING_DATA))
        ).andExpect(status().isOk)
    }

    @Test
    internal fun downloadRankingWhenUnauthorized() {
        mockMvc.perform(
            post(RANKING_DOWNLOAD_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBodyOf(RANKING_DATA))
        ).andExpect(status().isUnauthorized)
    }

    @Test
    internal fun downloadRankingWhenMissingScope() {
        mockMvc.perform(
            post(RANKING_DOWNLOAD_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.COMPETITOR_WRITE))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBodyOf(RANKING_DATA))
        ).andExpect(status().isNotFound)
    }
}

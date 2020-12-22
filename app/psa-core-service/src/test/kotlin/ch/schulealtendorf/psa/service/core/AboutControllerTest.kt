package ch.schulealtendorf.psa.service.core

import ch.schulealtendorf.psa.configuration.test.PsaWebMvcTest
import ch.schulealtendorf.psa.service.core.github.GithubApi
import ch.schulealtendorf.psa.service.core.github.VersionResponse
import com.nhaarman.mockito_kotlin.whenever
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

internal class AboutControllerTest : PsaWebMvcTest() {
    @Autowired
    private lateinit var mockGithubApi: GithubApi

    @Test
    internal fun getBuildInfo() {
        whenever(mockGithubApi.getLatestVersion()).thenReturn(VersionResponse("1.0.0"))

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

package ch.schulealtendorf.psa.web

import ch.schulealtendorf.psa.configuration.test.PsaWebMvcTest
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

internal class HomeControllerTest : PsaWebMvcTest() {

    @Test
    internal fun getHomepageWhenUnauthorized() {
        mockMvc.perform(get("/"))
            .andExpect(status().is3xxRedirection)
    }
}

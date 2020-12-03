package ch.schulealtendorf.psa.configuration.web.authorization

import ch.schulealtendorf.psa.configuration.test.PsaWebMvcTest
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

class SecurityConfigTest : PsaWebMvcTest() {

    @Test
    internal fun getHealth() {
        mockMvc.perform(MockMvcRequestBuilders.get("/actuator/health"))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }
}

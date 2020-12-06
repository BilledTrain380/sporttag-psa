package ch.schulealtendorf.psa.web

import ch.schulealtendorf.psa.configuration.test.PsaWebMvcTest
import ch.schulealtendorf.psa.core.user.USER_ADMIN
import ch.schulealtendorf.psa.core.user.UserManager
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

internal class AppControllerTest : PsaWebMvcTest() {

    @Autowired
    private lateinit var userManager: UserManager

    @Test
    internal fun forwardToAppWhenNoUser() {
        mockMvc.perform(get("/app"))
            .andExpect(status().isOk)
            .andExpect { assertThat(it.response.forwardedUrl).contains("app/en/index.html") }
    }

    @Test
    internal fun forwardToAppWithUser() {
        val admin = userManager.getOne(USER_ADMIN)
            .map {
                it.toBuilder()
                    .setLocale("de")
                    .build()
            }

        assertThat(admin).isNotEmpty
        userManager.save(admin.get())

        mockMvc.perform(
            get("/app")
                .with(user(USER_ADMIN))
        ).andExpect(status().isOk)
            .andExpect { assertThat(it.response.forwardedUrl).contains("app/de/index.html") }
    }
}

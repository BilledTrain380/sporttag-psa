package ch.schulealtendorf.sporttagpsa.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.fail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.RequestPostProcessor
import org.springframework.web.util.UriComponentsBuilder

@ActiveProfiles("test")
@Tag("ctrl-test")
@SpringBootTest
@AutoConfigureMockMvc
class PsaWebMvcTest {
    companion object {
        const val ADMIN_USER = "admin"
        const val NORMAL_USER = "user"

        private const val AUTHORIZE_ENDPOINT =
            "/oauth/authorize?response_type=token&client_id=psa-kitten&redirect_uri=http://test-client"
    }

    protected val mockMvc: MockMvc get() = mvc

    @Autowired
    @Qualifier("psa-user-service")
    private lateinit var userDetailsService: UserDetailsService

    @Autowired
    private lateinit var mvc: MockMvc

    private val objectMapper = ObjectMapper().apply {
        registerModule(JavaTimeModule())
        disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    }

    protected fun bearerTokenAdmin(vararg scope: String): RequestPostProcessor {
        return bearerToken(ADMIN_USER, *scope)
    }

    protected fun bearerTokenUser(vararg scope: String): RequestPostProcessor {
        return bearerToken(NORMAL_USER, *scope)
    }

    protected fun bearerToken(username: String, vararg scope: String): RequestPostProcessor {
        return RequestPostProcessor {
            it.addHeader("Authorization", "Bearer ${createToken(username, *scope)}")
            it
        }
    }

    protected fun user(username: String): RequestPostProcessor {
        val userDetails = userDetailsService.loadUserByUsername(username)
        return user(userDetails)
    }

    protected fun jsonBodyOf(obj: Any): String {
        return objectMapper.writeValueAsString(obj)
    }

    private fun createToken(username: String, vararg scope: String): String {
        val endpoint = StringBuilder(AUTHORIZE_ENDPOINT)
            .append("&scope=")
            .append(scope.joinToString { it })
            .toString()

        val userDetails = userDetailsService.loadUserByUsername(username)

        val redirectUrl = mockMvc.perform(get(endpoint).with(user(userDetails)))
            .andReturn()
            .response
            .redirectedUrl
            ?: fail("Redirect uri is null")

        val fragment = UriComponentsBuilder.fromUriString(redirectUrl)
            .build()
            .fragment
            ?: fail("No fragment to get access token")

        return UriComponentsBuilder.newInstance()
            .query(fragment)
            .build()
            .queryParams
            .getFirst("access_token")
            ?: fail("No access token in fragment")
    }
}

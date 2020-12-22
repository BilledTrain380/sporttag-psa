package ch.schulealtendorf.psa.service.core

import ch.schulealtendorf.psa.service.core.github.GithubApi
import com.nhaarman.mockito_kotlin.mock
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile

@Configuration
@Profile("test")
class CoreMocks {
    @Bean
    @Primary
    fun githubApi(): GithubApi = mock()
}

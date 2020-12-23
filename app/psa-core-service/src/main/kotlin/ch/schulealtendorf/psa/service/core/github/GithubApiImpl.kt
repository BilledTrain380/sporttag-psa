package ch.schulealtendorf.psa.service.core.github

import mu.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class GithubApiImpl : GithubApi {
    private val log = KotlinLogging.logger {}

    override fun getLatestVersion(): VersionResponse {
        log.info { "Get latest PSA version from github" }

        return RestTemplate().getForObject(
            "https://billedtrain380.github.io/sporttag-psa/assets/version.json",
            VersionResponse::class.java
        )!!
    }
}

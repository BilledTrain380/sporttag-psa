package ch.schulealtendorf.psa.service.core.github

import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class GithubApiImpl : GithubApi {
    override fun getLatestVersion(): VersionResponse {
        return RestTemplate().getForObject(
            "https://billedtrain380.github.io/sporttag-psa/assets/version.json",
            VersionResponse::class.java
        )!!
    }
}

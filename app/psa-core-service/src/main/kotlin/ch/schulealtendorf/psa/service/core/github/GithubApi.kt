package ch.schulealtendorf.psa.service.core.github

interface GithubApi {
    fun getLatestVersion(): VersionResponse
}

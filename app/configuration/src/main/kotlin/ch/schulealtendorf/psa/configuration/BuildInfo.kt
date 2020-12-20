package ch.schulealtendorf.psa.configuration

import org.codehaus.jackson.map.ObjectMapper
import java.time.Instant

object BuildInfo {
    val BUILD_INFO_JSON: String

    val VERSION: String
    val HASH: String
    val BUILD_TIME: Instant
    val ENVIRONMENT: String

    init {
        val localBuildInfo = BuildInfo::class.java.getResourceAsStream("/build-info.json").bufferedReader().use { it.readText() }
        val values = ObjectMapper().readValue(localBuildInfo, HashMap::class.java)

        BUILD_INFO_JSON = localBuildInfo
        VERSION = values.getOrDefault("version", "Uknown") as String
        HASH = values.getOrDefault("hash", "") as String
        BUILD_TIME = Instant.parse(values.getOrDefault("build_time", Instant.EPOCH.toString()) as String)
        ENVIRONMENT = values.getOrDefault("environment", "dev") as String
    }
}

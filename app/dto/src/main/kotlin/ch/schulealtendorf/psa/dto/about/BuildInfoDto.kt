package ch.schulealtendorf.psa.dto.about

import java.time.Instant

data class BuildInfoDto(
    val version: String,
    val hash: String,
    val buildTime: Instant,
    val environment: String
)

package ch.schulealtendorf.psa.dto.ranking

import ch.schulealtendorf.psa.dto.participation.GenderDto

data class DisciplineRankingDto(
    val discipline: String,
    val gender: GenderDto
)

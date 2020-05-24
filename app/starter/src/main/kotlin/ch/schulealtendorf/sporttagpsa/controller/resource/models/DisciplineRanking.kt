package ch.schulealtendorf.sporttagpsa.controller.resource.models

import ch.schulealtendorf.psa.dto.participation.GenderDto

data class DisciplineRanking(
    val discipline: String,
    val gender: GenderDto
)

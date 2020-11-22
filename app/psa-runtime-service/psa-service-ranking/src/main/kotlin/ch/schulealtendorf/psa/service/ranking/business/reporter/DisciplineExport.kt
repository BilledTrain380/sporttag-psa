package ch.schulealtendorf.psa.service.ranking.business.reporter

import ch.schulealtendorf.psa.dto.participation.GenderDto
import ch.schulealtendorf.psa.dto.participation.athletics.DisciplineDto

data class DisciplineExport(
    val discipline: DisciplineDto,
    val gender: GenderDto
)

package ch.schulealtendorf.psa.service.ranking.business

import ch.schulealtendorf.psa.dto.participation.GenderDto
import ch.schulealtendorf.psa.service.ranking.business.reporter.DisciplineExport

data class RankingExport(
    val disciplines: Iterable<DisciplineExport>,
    val triathlon: Iterable<GenderDto>,
    val total: Iterable<GenderDto>,
    val ubsCup: Iterable<GenderDto>
)

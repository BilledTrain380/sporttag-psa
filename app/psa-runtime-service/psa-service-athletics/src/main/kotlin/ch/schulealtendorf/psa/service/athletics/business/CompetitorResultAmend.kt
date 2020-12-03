package ch.schulealtendorf.psa.service.athletics.business

import ch.schulealtendorf.psa.dto.participation.athletics.ResultElement

data class CompetitorResultAmend(
    val competitorId: Int,
    val result: ResultElement
)

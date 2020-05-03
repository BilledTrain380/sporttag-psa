package ch.schulealtendorf.sporttagpsa.business.athletics

import ch.schulealtendorf.psa.dto.participation.athletics.ResultElement

data class CompetitorResultAmend(
    val competitorId: Int,
    val result: ResultElement
)

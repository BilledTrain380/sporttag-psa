package ch.schulealtendorf.psa.dto.participation

import ch.schulealtendorf.psa.dto.participation.athletics.ResultDto
import java.util.Optional

data class CompetitorDto(
    val startnumber: Int,
    val participant: ParticipantDto,
    val results: Map<String, ResultDto>
) {
    fun findResultByDiscipline(discipline: String) = Optional.ofNullable(results[discipline])

    fun findResultByDisciplineOrFail(discipline: String): ResultDto {
        return findResultByDiscipline(discipline)
            .orElseThrow { NoSuchElementException("Result with discipline $discipline does not exist") }
    }
}

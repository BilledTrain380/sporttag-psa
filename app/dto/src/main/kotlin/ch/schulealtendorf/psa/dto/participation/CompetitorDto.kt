package ch.schulealtendorf.psa.dto.participation

import ch.schulealtendorf.psa.dto.group.SimpleGroupDto
import ch.schulealtendorf.psa.dto.participation.athletics.ResultDto
import java.util.Optional

data class CompetitorDto(
    val startnumber: Int,
    override val id: Int = 0,
    override val surname: String,
    override val prename: String,
    override val gender: GenderDto,
    override val birthday: BirthdayDto,
    override val isAbsent: Boolean = false,
    override val address: String,
    override val town: TownDto,
    override val group: SimpleGroupDto,
    val results: Map<String, ResultDto>
) : ParticipantBase {
    fun findResultByDiscipline(discipline: String) = Optional.ofNullable(results[discipline])

    fun findResultByDisciplineOrFail(discipline: String): ResultDto {
        return findResultByDiscipline(discipline)
            .orElseThrow { NoSuchElementException("Result with discipline $discipline does not exist") }
    }
}

package ch.schulealtendorf.psa.dto.participation

import ch.schulealtendorf.psa.dto.group.SimpleGroupDto
import ch.schulealtendorf.psa.dto.status.StatusDto

data class ParticipationDto(
    val status: StatusDto,
    val unfinishedGroups: List<SimpleGroupDto>
)

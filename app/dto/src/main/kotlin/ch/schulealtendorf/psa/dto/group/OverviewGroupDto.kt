package ch.schulealtendorf.psa.dto.group

import ch.schulealtendorf.psa.dto.status.StatusDto

/**
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.2.0
 */
data class OverviewGroupDto(
    val group: SimpleGroupDto,
    val status: StatusDto
)

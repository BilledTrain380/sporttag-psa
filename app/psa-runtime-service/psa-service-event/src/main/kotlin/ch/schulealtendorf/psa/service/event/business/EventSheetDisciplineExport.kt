package ch.schulealtendorf.psa.service.event.business

import ch.schulealtendorf.psa.dto.group.SimpleGroupDto
import ch.schulealtendorf.psa.dto.participation.GenderDto
import ch.schulealtendorf.psa.dto.participation.athletics.DisciplineDto

data class EventSheetDisciplineExport(
    val discipline: DisciplineDto,
    val group: SimpleGroupDto,
    val gender: GenderDto
)

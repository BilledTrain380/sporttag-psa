package ch.schulealtendorf.psa.dto.event

import ch.schulealtendorf.psa.dto.participation.GenderDto

data class EventSheetExport(
    val discipline: String,
    val group: String,
    val gender: GenderDto
)

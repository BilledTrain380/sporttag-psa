package ch.schulealtendorf.psa.dto.event

data class EventSheetData(
    val isParticipationOpen: Boolean,
    val groups: List<String>
)

package ch.schulealtendorf.psa.service.event.business

import ch.schulealtendorf.psa.dto.participation.SportDto

data class ParticipantListExport(
    val sports: Iterable<SportDto>
)
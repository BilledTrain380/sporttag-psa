package ch.schulealtendorf.psa.dto.participation

import ch.schulealtendorf.psa.dto.status.StatusType

enum class ParticipationStatusType(
    code: Int
) : StatusType {
    OPEN(0),
    CLOSED(1);

    private val range = 2000

    override val code = range + code
    override val text = this.name
}

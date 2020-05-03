package ch.schulealtendorf.psa.dto.participation

import ch.schulealtendorf.psa.dto.group.SimpleGroupDto

internal interface ParticipantBase {
    val id: Int
    val surname: String
    val prename: String
    val gender: GenderDto
    val birthday: BirthdayDto
    val address: String
    val town: TownDto
    val isAbsent: Boolean
    val group: SimpleGroupDto
}

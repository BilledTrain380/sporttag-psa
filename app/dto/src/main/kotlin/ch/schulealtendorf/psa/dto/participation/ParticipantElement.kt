package ch.schulealtendorf.psa.dto.participation

class ParticipantElement(
    val id: Int,
    val surname: String?,
    val prename: String?,
    val gender: GenderDto?,
    val birthday: BirthdayDto?,
    val isAbsent: Boolean?,
    val address: String?,
    val town: TownDto?
)

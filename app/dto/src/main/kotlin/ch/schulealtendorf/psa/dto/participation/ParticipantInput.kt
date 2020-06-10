package ch.schulealtendorf.psa.dto.participation

data class ParticipantInput(
    val surname: String,
    val prename: String,
    val gender: GenderDto,
    val birthday: String,
    val address: String,
    val town: TownDto,
    val group: String
)

package ch.schulealtendorf.psa.dto.group

/**
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.2.0
 */
data class SimpleGroupDto(
    val name: String,
    val coach: String
) {
    companion object {
        fun ofNameOnly(name: String): SimpleGroupDto {
            return SimpleGroupDto(name, "n/a")
        }
    }
}

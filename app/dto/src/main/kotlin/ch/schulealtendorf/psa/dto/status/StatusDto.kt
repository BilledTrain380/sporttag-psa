package ch.schulealtendorf.psa.dto.status

/**
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.2.0
 */
data class StatusDto @JvmOverloads constructor(
    val severity: StatusSeverity,
    val entries: List<StatusEntry> = listOf()
) {
    fun contains(type: StatusType): Boolean {
        return entries.any { it.type.text == type.text }
    }
}

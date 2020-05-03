package ch.schulealtendorf.psa.dto.status

/**
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.2.0
 */
data class StatusEntry(
    val severity: StatusSeverity,
    val type: StatusType
)

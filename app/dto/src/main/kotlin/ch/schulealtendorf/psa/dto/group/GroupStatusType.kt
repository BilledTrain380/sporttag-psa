package ch.schulealtendorf.psa.dto.group

import ch.schulealtendorf.psa.dto.status.StatusType

/**
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.2.0
 */
enum class GroupStatusType(
    code: Int
) : StatusType {
    OK(0),
    UNFINISHED_PARTICIPANTS(1),
    GROUP_TYPE_COMPETITIVE(2),
    GROUP_TYPE_FUN(3);

    private val range = 1000

    override val code = range + code
    override val text: String = this.name
}

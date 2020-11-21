package ch.schulealtendorf.psa.service.athletics.business

import ch.schulealtendorf.psa.dto.participation.GenderDto

data class CompetitorFilter(
    val group: String? = null,
    val gender: GenderDto? = null,
    val isAbsent: Boolean? = null
) {
    fun hasAnyFilter(): Boolean {
        if (group != null) {
            return true
        }

        if (gender != null) {
            return true
        }

        return isAbsent != null
    }

    fun filterByGroup(group: String): Boolean {
        if (this.group != null) {
            return this.group == group
        }

        return true
    }

    fun filterByGender(gender: GenderDto): Boolean {
        if (this.gender != null) {
            return this.gender == gender
        }

        return true
    }

    fun filterByAbsent(isAbsent: Boolean): Boolean {
        if (this.isAbsent != null) {
            return this.isAbsent == isAbsent
        }

        return true
    }
}

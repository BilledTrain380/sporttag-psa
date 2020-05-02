package ch.schulealtendorf.psa.dto.participation.athletics

data class ResultDto @JvmOverloads constructor(
    val id: Int,
    val value: Long,
    val points: Int,
    val discipline: DisciplineDto,
    val distance: String? = null
) {
    val relativeValue: String

    init {
        val factor = discipline.unit.factor

        if (factor == 1) {
            this.relativeValue = this.value.toString()
        } else {
            this.relativeValue = (this.value.toDouble() / factor).toString()
        }
    }

    fun toBuilder() = Builder(this)

    class Builder internal constructor(
        private val dto: ResultDto
    ) {
        private var value = dto.value
        private var points = dto.points

        fun setValue(value: Long): Builder {
            this.value = value
            return this
        }

        fun setPoints(points: Int): Builder {
            this.points = points
            return this
        }

        fun build() = dto.copy(value = value, points = points)
    }
}

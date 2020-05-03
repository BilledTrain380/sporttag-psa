package ch.schulealtendorf.psa.core.io

enum class AppDirectory {
    REPORTING,
    EXPORT;

    override fun toString(): String {
        return super.toString().toLowerCase()
    }
}

package ch.schulealtendorf.sporttagpsa.lib

inline fun <T> T?.ifNotNull(block: (T) -> Unit) {
    if (this != null) {
        block(this)
    }
}

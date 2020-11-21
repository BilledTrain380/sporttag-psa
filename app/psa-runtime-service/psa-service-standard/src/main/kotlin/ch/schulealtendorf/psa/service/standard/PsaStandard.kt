package ch.schulealtendorf.psa.service.standard

import java.util.Optional

inline fun <T> T?.ifNotNull(block: (T) -> Unit) {
    if (this != null) {
        block(this)
    }
}

fun <T> T?.toOptional(): Optional<T> {
    return Optional.ofNullable(this)
}

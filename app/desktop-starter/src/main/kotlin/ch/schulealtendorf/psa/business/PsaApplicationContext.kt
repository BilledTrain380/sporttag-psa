package ch.schulealtendorf.psa.business

import ch.schulealtendorf.psa.PsaApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.ConfigurableApplicationContext

object PsaApplicationContext {
    private var context: ConfigurableApplicationContext? = null

    fun start(args: Array<String>) {
        context = SpringApplicationBuilder(PsaApplication::class.java)
            .profiles("standalone")
            .run(*args)
    }

    fun stop() {
        context?.close()
        context = null
    }
}

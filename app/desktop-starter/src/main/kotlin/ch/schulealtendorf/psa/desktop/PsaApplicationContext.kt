package ch.schulealtendorf.psa.desktop

import ch.schulealtendorf.psa.PsaApplication
import ch.schulealtendorf.psa.setup.AppDirsApplicationDirectory
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.ConfigurableApplicationContext
import java.nio.file.Path

object PsaApplicationContext {
    val logsDirectory: Path = AppDirsApplicationDirectory().path.resolve("logs")

    private var context: ConfigurableApplicationContext? = null

    fun start(args: Array<String>) {
        val properties = HashMap<String, Any>().apply {
            put("psa.logging.path", logsDirectory.toString())
        }

        context = SpringApplicationBuilder(PsaApplication::class.java)
            .profiles("standalone")
            .properties(properties)
            .run(*args)
    }

    fun stop() {
        context?.close()
        context = null
    }
}

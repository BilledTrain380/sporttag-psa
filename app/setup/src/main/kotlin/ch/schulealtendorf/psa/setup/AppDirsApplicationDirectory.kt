package ch.schulealtendorf.psa.setup

import net.harawata.appdirs.AppDirsFactory
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

@Profile("standalone")
@Component
class AppDirsApplicationDirectory : ApplicationDirectory {
    companion object {
        private val APP_DIRS = AppDirsFactory.getInstance()
    }

    override val path: Path = File(APP_DIRS.getUserDataDir("PSA", "", "")).toPath()

    init {
        Files.createDirectories(path)
    }
}

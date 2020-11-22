package ch.schulealtendorf.psa.core.io

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.nio.file.Path
import java.nio.file.Paths

@Component
@Profile("dev")
class RelativeFileSystem : FileSystem {
    override val applicationDir: Path = Paths.get("./application-dir")
}

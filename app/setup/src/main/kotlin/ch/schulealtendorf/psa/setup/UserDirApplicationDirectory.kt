package ch.schulealtendorf.psa.setup

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.io.File
import java.nio.file.Path

@Profile("prod")
@Component
class UserDirApplicationDirectory : ApplicationDirectory {
    override val path: Path = File(System.getProperty("user.dir")).toPath()
}

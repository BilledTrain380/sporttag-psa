package ch.schulealtendorf.psa.service.event.business

import ch.schulealtendorf.psa.core.io.AppDirectory
import ch.schulealtendorf.psa.core.io.ApplicationFile
import ch.schulealtendorf.psa.core.io.FileSystem
import ch.schulealtendorf.psa.service.event.business.reporter.StartlistReporter
import ch.schulealtendorf.psa.service.standard.export.ArchiveGenerationException
import ch.schulealtendorf.psa.service.standard.export.ExportManager
import mu.KotlinLogging
import org.springframework.stereotype.Component
import java.io.File
import java.util.ResourceBundle

@Component
class StartlistExportManager(
    private val fileSystem: FileSystem,
    private val startlistReporter: StartlistReporter
) : ExportManager<Void> {
    private val resourceBundle = ResourceBundle.getBundle("i18n.event-sheets")
    private val log = KotlinLogging.logger {}

    override fun generateArchive(data: Void) = generateArchive()

    /**
     * Generates an archive file for the startlist.
     *
     * @return the generated archive
     * @throws ArchiveGenerationException if the archive could not be generated
     */
    fun generateArchive(): File {
        log.info { "Create archive for startlist" }
        val report = startlistReporter.generateReport()

        val file = ApplicationFile(AppDirectory.EXPORT, resourceBundle.getString("startlist"))
        return fileSystem.createArchive(file, setOf(report))
    }
}

package ch.schulealtendorf.psa.service.event.business

import ch.schulealtendorf.psa.core.io.AppDirectory
import ch.schulealtendorf.psa.core.io.ApplicationFile
import ch.schulealtendorf.psa.core.io.FileSystem
import ch.schulealtendorf.psa.dto.participation.SportDto
import ch.schulealtendorf.psa.service.event.business.reporter.ParticipantListReporter
import ch.schulealtendorf.psa.service.standard.export.ArchiveGenerationException
import ch.schulealtendorf.psa.service.standard.export.ExportManager
import mu.KotlinLogging
import org.springframework.stereotype.Component
import java.io.File
import java.util.ResourceBundle

@Component
class ParticipantListExportManager(
    private val fileSystem: FileSystem,
    private val participantListReporter: ParticipantListReporter
) : ExportManager<List<SportDto>> {
    private val resourceBundle = ResourceBundle.getBundle("i18n.event-sheets")
    private val log = KotlinLogging.logger {}

    /**
     * Generates an archive file for the participant list by the given {@code data}.
     *
     * @param data contains the data to generate the archive
     *
     * @return the generated archive
     * @throws ArchiveGenerationException if the archive could not be generated
     */
    override fun generateArchive(data: List<SportDto>): File {
        try {
            log.info { "Create archive for participant list" }
            val reports = participantListReporter.generateReport(data)

            val file = ApplicationFile(AppDirectory.EXPORT, resourceBundle.getString("participant-list"))
            return fileSystem.createArchive(file, reports)
        } catch (ex: Exception) {
            throw ArchiveGenerationException("Could not generate archive: cause=${ex.message}", ex)
        }
    }
}

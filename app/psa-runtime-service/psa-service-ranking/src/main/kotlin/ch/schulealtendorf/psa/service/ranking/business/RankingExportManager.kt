package ch.schulealtendorf.psa.service.ranking.business

import ch.schulealtendorf.psa.core.io.AppDirectory
import ch.schulealtendorf.psa.core.io.ApplicationFile
import ch.schulealtendorf.psa.core.io.FileSystem
import ch.schulealtendorf.psa.dto.participation.athletics.DisciplineDto
import ch.schulealtendorf.psa.service.ranking.business.reporter.DisciplineRankingReporter
import ch.schulealtendorf.psa.service.ranking.business.reporter.TotalRankingReporter
import ch.schulealtendorf.psa.service.ranking.business.reporter.TriathlonRankingReporter
import ch.schulealtendorf.psa.service.standard.disciplineDtoOf
import ch.schulealtendorf.psa.service.standard.export.ArchiveGenerationException
import ch.schulealtendorf.psa.service.standard.export.ExportManager
import ch.schulealtendorf.psa.service.standard.repository.DisciplineRepository
import org.springframework.stereotype.Component
import java.io.File
import java.util.Optional
import java.util.ResourceBundle

@Component
class RankingExportManager(
    private val fileSystem: FileSystem,
    private val totalRankingReporter: TotalRankingReporter,
    private val triathlonRankingReporter: TriathlonRankingReporter,
    private val disciplineRankingReporter: DisciplineRankingReporter,
    private val disciplineRepository: DisciplineRepository
) : ExportManager<RankingExport> {
    private val resourceBundle = ResourceBundle.getBundle("i18n.file-names")

    /**
     * Generates an archive file for the rankings by the given {@code data}.
     *
     * @param data contains the data to generate the archive
     *
     * @return the generated archive
     * @throws ArchiveGenerationException if the archive could not be generated
     */
    override fun generateArchive(data: RankingExport): File {
        try {
            val reports = setOf(
                totalRankingReporter.generateReport(data.total),
                triathlonRankingReporter.generateReport(data.triathlon),
                disciplineRankingReporter.generateReport(data.disciplines),
                triathlonRankingReporter.generateCSV(data.ubsCup)
            ).flatten()

            val file = ApplicationFile(AppDirectory.EXPORT, resourceBundle.getString("ranking"))
            return fileSystem.createArchive(file, reports)
        } catch (ex: Exception) {
            throw ArchiveGenerationException("Could not generate archive: case=${ex.message}", ex)
        }
    }

    fun getDiscipline(name: String): Optional<DisciplineDto> {
        return disciplineRepository.findById(name).map { disciplineDtoOf(it) }
    }
}

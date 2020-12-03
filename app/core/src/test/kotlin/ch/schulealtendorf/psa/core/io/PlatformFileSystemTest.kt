package ch.schulealtendorf.psa.core.io

import ch.schulealtendorf.psa.setup.ApplicationDirectory
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Files
import java.util.zip.ZipInputStream

@Tag("unit-test")
internal class PlatformFileSystemTest {

    @TempDir
    lateinit var applicationDir: File

    private val mockApplicationDirectory: ApplicationDirectory = mock()

    private lateinit var fileSystem: PlatformFileSystem

    private val firstZipEntryFileName = "zip-entry-1.txt"
    private val secondZipEntryFileName = "zip-entry-2.txt"

    @BeforeEach
    internal fun beforeEach() {
        whenever(mockApplicationDirectory.path).thenReturn(applicationDir.toPath())
        fileSystem = PlatformFileSystem(mockApplicationDirectory)
    }

    @Test
    internal fun writeLines() {
        val lines = listOf("First line", "Second line")
        val file = ApplicationFile("test", "file.txt")

        val writtenFile = fileSystem.write(file, lines)

        assertThat(writtenFile).exists()
        assertThat(writtenFile).canRead()
        assertThat(writtenFile).hasName("file.txt")
        assertThat(writtenFile).hasExtension("txt")
        assertThat(writtenFile).hasContent("First line\nSecond line")
    }

    @Test
    internal fun writeLinesWhenFileAlreadyExists() {
        val lines = listOf("First line", "Second line")
        val newLines = listOf("First line", "Second line", "Third line")
        val file = ApplicationFile("test", "file.txt")

        fileSystem.write(file, lines)
        val writtenFile = fileSystem.write(file, newLines)

        assertThat(writtenFile).exists()
        assertThat(writtenFile).canRead()
        assertThat(writtenFile).hasName("file.txt")
        assertThat(writtenFile).hasExtension("txt")
        assertThat(writtenFile).hasContent("First line\nSecond line\nThird line")
    }

    @Test
    internal fun writeInputStream() {
        val inputStream = "Content of the file".byteInputStream()
        val file = ApplicationFile("test", "file.txt")

        val writtenFile = fileSystem.write(file, inputStream)

        assertThat(writtenFile).exists()
        assertThat(writtenFile).canRead()
        assertThat(writtenFile).hasName("file.txt")
        assertThat(writtenFile).hasExtension("txt")
        assertThat(writtenFile).hasContent("Content of the file")
    }

    @Test
    internal fun writeInputStreamWhenFileAlreadyExists() {
        val inputStream = "Content of the file".byteInputStream()
        val newInputStream = "Other content".byteInputStream()
        val file = ApplicationFile("test", "file.txt")

        fileSystem.write(file, inputStream)
        val writtenFile = fileSystem.write(file, newInputStream)

        assertThat(writtenFile).exists()
        assertThat(writtenFile).canRead()
        assertThat(writtenFile).hasName("file.txt")
        assertThat(writtenFile).hasExtension("txt")
        assertThat(writtenFile).hasContent("Other content")
    }

    @Test
    internal fun createArchive() {
        val firstFile = applicationDir.resolve(firstZipEntryFileName).toPath()
        Files.copy(
            PlatformFileSystemTest::class.java.getResourceAsStream("/io/$firstZipEntryFileName"),
            firstFile
        )

        val secondFile = applicationDir.resolve(secondZipEntryFileName).toPath()
        Files.copy(
            PlatformFileSystemTest::class.java.getResourceAsStream("/io/$secondZipEntryFileName"),
            secondFile
        )

        val firstEntry = applicationDir.resolve(firstZipEntryFileName)
        val secondEntry = applicationDir.resolve(secondZipEntryFileName)
        val zip = ApplicationFile("test", "zip-file")

        val writtenFile = fileSystem.createArchive(zip, listOf(firstEntry, secondEntry))

        assertThat(writtenFile).exists()
        assertThat(writtenFile).canRead()
        assertThat(writtenFile).hasName("zip-file.zip")
        assertThat(writtenFile).hasExtension("zip")

        val zipInputStream = ZipInputStream(writtenFile.inputStream())

        val firstZipEntry = zipInputStream.nextEntry
        assertThat(firstZipEntry.name).endsWith(firstZipEntryFileName)

        val secondZipEntry = zipInputStream.nextEntry
        assertThat(secondZipEntry.name).endsWith(secondZipEntryFileName)
    }
}

/*
 * Copyright (c) 2019 by Nicolas Märchy
 *
 * This file is part of Sporttag PSA.
 *
 * Sporttag PSA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Sporttag PSA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Sporttag PSA.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Diese Datei ist Teil von Sporttag PSA.
 *
 * Sporttag PSA ist Freie Software: Sie können es unter den Bedingungen
 * der GNU General Public License, wie von der Free Software Foundation,
 * Version 3 der Lizenz oder (nach Ihrer Wahl) jeder späteren
 * veröffentlichten Version, weiterverbreiten und/oder modifizieren.
 *
 * Sporttag PSA wird in der Hoffnung, dass es nützlich sein wird, aber
 * OHNE JEDE GEWÄHRLEISTUNG, bereitgestellt; sogar ohne die implizite
 * Gewährleistung der MARKTFÄHIGKEIT oder EIGNUNG FÜR EINEN BESTIMMTEN ZWECK.
 * Siehe die GNU General Public License für weitere Details.
 *
 * Sie sollten eine Kopie der GNU General Public License zusammen mit diesem
 * Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.
 *
 *
 */

package ch.schulealtendorf.psa.core.io

import net.harawata.appdirs.AppDirs
import org.springframework.stereotype.Component
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.nio.file.Files
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/**
 * The file system considers the special directory of the platform.
 * The application directory will be created by the constructor.
 *
 * @author nmaerchy
 * @version 1.0.0
 */
@Component
class PlatformFileSystem(
    appDirs: AppDirs
) : FileSystem {
    private val applicationDir = File(appDirs.getUserDataDir("PSA", "", "")).toPath()

    init {
        Files.createDirectories(applicationDir)
    }

    /**
     * @return the application directory which Sporttag PSA can use
     */
    override fun getApplicationDir(): File = applicationDir.toFile()

    override fun write(file: ApplicationFile, lines: List<String>): File {
        val newFile = createFile(file)

        newFile.bufferedWriter().use { writer ->
            lines.forEach {
                writer.write(it)
                writer.newLine()
            }
        }

        return newFile
    }

    override fun write(file: ApplicationFile, input: InputStream): File {
        val newFile = createFile(file)

        input.use { inputStream ->
            newFile.outputStream().use {
                inputStream.copyTo(it)
            }
        }

        return newFile
    }

    override fun createArchive(file: ApplicationFile, files: Iterable<File>): File {
        val zipFile = createFile(file, ".zip")

        ZipOutputStream(FileOutputStream(zipFile)).use { outputStream ->
            files
                .map { ZipEntry(it.absolutePath) }
                .forEach { outputStream.putNextEntry(it) }
        }

        return zipFile
    }

    private fun createFile(appFile: ApplicationFile, extension: String = ""): File {
        val file = applicationDir.resolve("${appFile.path}$extension")
        Files.createDirectories(file.parent)
        Files.deleteIfExists(file)
        Files.createFile(file)
        return file.toFile()
    }
}

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

import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/**
 * Describes a abstraction of the file system used by Sporttag PSA.
 *
 * @author nmaerchy
 * @version 1.0.0
 */
interface FileSystem {

    /**
     * The application directory which PSA can use
     */
    val applicationDir: Path

    /**
     * Writes the given {@code lines} to the given {@code file}.
     * If the file does not exists, it will be created.
     * If the file does exists already, it will be replaced.
     *
     * If the directory where the file belongs does not exist, it will be created first.
     *
     * @param file the file to write to
     * @param lines the lines to append
     *
     * @return the created file
     * @throws java.io.IOException If the file could not be created
     */
    @JvmDefault
    fun write(file: ApplicationFile, lines: List<String>): File {
        val newFile = createFile(file)

        newFile.bufferedWriter().use { writer ->
            lines.forEach {
                writer.write(it)
                writer.newLine()
            }
        }

        return newFile
    }

    /**
     * Writes the given {@code input} to the given {@code file}.
     * If the file does not exists, it will be created.
     * If the file does exists already, it will be replaced.
     *
     * If the directory where the file belongs does not exist, it will be created first.
     *
     * The file will be created relative to the application dir
     * @see FileSystem#getApplicationDir
     *
     * @param file the file to write
     * @param input content of the file
     *
     * @return the created file
     * @throws java.io.IOException if the file could not be created
     */
    @JvmDefault
    fun write(file: ApplicationFile, input: InputStream): File {
        val newFile = createFile(file)

        input.use { inputStream ->
            newFile.outputStream().use {
                inputStream.copyTo(it)
            }
        }

        return newFile
    }

    /**
     * Creates an archive with the given {@code files}.
     * The given {@code file} should not include any file extension.
     * The file extension will be set by this method.
     *
     * If the directory where the file belongs does not exist, it will be created first.
     * If the {@code file} exists already, it will be replaced.
     *
     * @param file the file to to create the archive
     * @param files files that should be added to the archive
     *
     * @return the created archive
     * @throws java.io.IOException if the archive could not be created
     */
    @JvmDefault
    fun createArchive(file: ApplicationFile, files: Iterable<File>): File {
        val zipFile = createFile(file, ".zip")

        ZipOutputStream(BufferedOutputStream(FileOutputStream(zipFile))).use { outputStream ->
            files.forEach {
                outputStream.putNextEntry(ZipEntry(it.name))
                outputStream.write(it.readBytes())
                outputStream.closeEntry()
            }
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

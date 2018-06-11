/*
 * Copyright (c) 2018 by Nicolas Märchy
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

package ch.schulealtendorf.sporttagpsa.filesystem

import net.harawata.appdirs.AppDirs
import net.lingala.zip4j.core.ZipFile
import net.lingala.zip4j.model.ZipParameters
import net.lingala.zip4j.util.Zip4jConstants
import org.springframework.stereotype.Component
import java.io.File
import java.io.InputStream

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
): FileSystem {
    
    private val applicationDir = File(appDirs.getUserDataDir("Sporttag PSA", "1.0.0", "Schule Altendorf"))
    
    init {
        
        if (!applicationDir.exists())
            applicationDir.mkdirs()
    }
    
    /**
     * @return the application directory which Sporttag PSA can use
     */
    override fun getApplicationDir() = applicationDir

    /**
     * Creates the given {@code fileName} in the application directory.
     * @see FileSystem.getApplicationDir
     *
     * If the given {@code fileName} exists already, it will be replaced.
     *
     * @param fileName the file to create
     * @throws java.io.IOException if the file could not be created
     */
    override fun createFile(fileName: String): File {
        val file = applicationDir.resolve(fileName)

        if (file.exists())
            file.delete()

        file.createNewFile()

        return file
    }

    /**
     * Writes the given {@code input} to the given {@code fileName}.
     * If the file does not exists, it will be created.
     * If the file does exists already, it will be replaced.
     *
     * The file will be created relative to the application dir
     * @see FileSystem#getApplicationDir
     *
     * @param fileName the name of the file that is created
     * @param input content of the file
     *
     * @return the created file
     * @throws java.io.IOException if the file could not be created
     */
    override fun write(fileName: String, input: InputStream): File {
        
        val file = createFile(fileName)

        input.use { 
            file.outputStream().use { fileOut ->
                it.copyTo(fileOut)
            }
        }

        return file
    }

    /**
     * Writes the given {@code lines} to the given {@code fileName}.
     * If the file does not exists, it will be created.
     * If the file does exists already, it will be replaced.
     *
     * @param fileName the file to write to
     * @param lines the lines to append
     *
     * @return the created file
     * @throws java.io.IOException If the file could not be created
     */
    override fun write(fileName: String, lines: List<String>): File {

        val file = applicationDir.resolve(fileName)

        file.bufferedWriter().use {

            lines.forEach { line ->
                it.write(line)
                it.newLine()
            }
        }

        return file
    }

    /**
     * Creates an archive with the given {@code files}.
     * The given {@code fileName} may not include any file extension.
     * The file extension will be set by this method.
     *
     * If the {@code fileName} exists already, it will be replaced.
     *
     * @param fileName the file name of the archive without extension
     * @param files files that should be added to the archive
     *
     * @return the created archive
     * @throws java.io.IOException if the archive could not be created
     */
    override fun createArchive(fileName: String, files: Iterable<File>): File {

        val zipFile = applicationDir.resolve("$fileName.zip")
        
        if(zipFile.exists())
            zipFile.delete()

        val rankingZip = ZipFile(zipFile).apply {
            setFileNameCharset("UTF-8")
        }
        val parameters = ZipParameters().apply {
            compressionMethod = Zip4jConstants.COMP_DEFLATE
            compressionLevel = Zip4jConstants.DEFLATE_LEVEL_NORMAL
        }

        files.forEach { rankingZip.addFile(it, parameters) }

        return zipFile
    }
}
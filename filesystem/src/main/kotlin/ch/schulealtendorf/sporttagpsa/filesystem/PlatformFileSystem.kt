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
    
    private val applicationDir = File(appDirs.getUserDataDir("PSA", "", ""))
    
    init {
        
        if (!applicationDir.exists())
            applicationDir.mkdirs()
    }
    
    /**
     * @return the application directory which Sporttag PSA can use
     */
    override fun getApplicationDir() = applicationDir

    override fun write(file: ApplicationFile, lines: List<String>): File {

        val newFile = createFile(file)

        newFile.bufferedWriter().use {

            lines.forEach { line ->
                it.write(line)
                it.newLine()
            }
        }

        return newFile
    }

    override fun write(file: ApplicationFile, input: InputStream): File {

        val newFile = createFile(file)

        input.use {
            newFile.outputStream().use { fileOut ->
                it.copyTo(fileOut)
            }
        }

        return newFile
    }

    override fun createArchive(file: ApplicationFile, files: Iterable<File>): File {

        val zipFile = createFile(file, ".zip")

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

    private fun createFile(appFile: ApplicationFile, extension: String = ""): File {

        val file = applicationDir.resolve("${appFile.path}$extension")

        if (file.exists())
            file.delete()

        file.parentFile.mkdirs()
        file.createNewFile()

        return file
    }
}
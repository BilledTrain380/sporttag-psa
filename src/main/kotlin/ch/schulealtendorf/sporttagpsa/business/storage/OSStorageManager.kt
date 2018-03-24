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

package ch.schulealtendorf.sporttagpsa.business.storage

import net.harawata.appdirs.AppDirs
import org.springframework.stereotype.Component
import java.io.File
import java.io.IOException
import java.io.InputStream

/**
 * OS specific storage supplier.
 * 
 * @author nmaerchy
 * @version 1.0.0
 */
@Component
class OSStorageManager(
        private val appDirs: AppDirs
): StorageManager {

    /**
     * Provides a path to the platform depended special directory.
     * https://github.com/harawata/appdirs
     *
     * @return the OS depended application directory
     */
    override fun getApplicationDir() = File(appDirs.getUserDataDir("Sporttag PSA", "1.0.0", "BilledTrain380"))

    /**
     * Writes the given {@code input} to the given {@code file}.
     *
     * The file path is relative to the platform depended special directory.
     * 
     * If the file exists already it will be replaced.
     *
     * @param relativeFile relative file from the platform's special directory
     * @param input content to write to the file
     * 
     * @return the created file
     * @throws java.io.IOException if the write process fails
     */
    override fun write(relativeFile: String, input: InputStream): File {
        
        val file = getApplicationDir().resolve(relativeFile)
        
        if (file.exists()) {
            file.delete()
        }
        
        file.createNewFile()
        
        val bufferedInputStream = input.buffered()
        
        file.bufferedWriter().use {
            
            try {

                var bytes = bufferedInputStream.read()

                while (bytes != -1) {
                    it.write(bytes)
                    bytes = bufferedInputStream.read()
                }
                
            } catch (ex: IOException) {
                it.close()
                it.flush()
                
                throw ex
            }
        }
        
        return file
    }
}
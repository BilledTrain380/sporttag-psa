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

import java.io.File
import java.io.InputStream

/**
 * Describes a abstraction of the file system used by Sporttag PSA.
 * 
 * @author nmaerchy
 * @version 1.0.0
 */
interface FileSystem {

    /**
     * @return the application directory which Sporttag PSA can use
     */
    fun getApplicationDir(): File

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
    fun write(fileName: String, input: InputStream): File

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
    fun createArchive(fileName: String, files: Iterable<File>): File
}
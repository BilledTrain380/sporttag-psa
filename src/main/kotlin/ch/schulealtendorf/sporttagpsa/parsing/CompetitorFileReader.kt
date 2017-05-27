/*
 * Copyright (c) 2017 by Nicolas Märchy
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

package ch.schulealtendorf.sporttagpsa.parsing

import com.opencsv.bean.CsvToBean
import com.opencsv.bean.HeaderColumnNameMappingStrategy
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.InputStreamReader
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * An implementation of a FileReader for a competitor input file.
 * 
 * @author nmaerchy
 * @version 0.0.2
 */
@Component
class CompetitorFileReader : FileReader {

    /**
     * Parses the passed in file by the following rules:
     *  The file MUST be a mime type text/csv.
     *  The file MUST NOT be empty.
     *  The file MUST have a header line:
     *      Klasse,Nachname,Vorname,Geschlecht,Strasse,PLZ,Ort,Geburtsdatum,Klassenlehrer
     *  The Geburtsdatum field MUST be in format: dd.MM.yyyy
     *  
     *  @param file the file to parse
     *  
     *  @return a list of the parsed entries
     */
    override fun parseToCompetitor(file: MultipartFile): List<FlatCompetitor> {
        
        if (file.contentType != "text/csv") throw IllegalArgumentException("The input file MUST be the mime type \"text/csv\".")
        if (file.isEmpty) throw IllegalArgumentException("Competitor input file is empty.")
        
        try {
            
            // setup parser
            val strategy: HeaderColumnNameMappingStrategy<CSVCompetitor> =  HeaderColumnNameMappingStrategy()
            strategy.type = CSVCompetitor::class.java
            val csvToBean: CsvToBean<CSVCompetitor> = CsvToBean()

            return csvToBean.parse(strategy, InputStreamReader(file.inputStream))
                    .map { (clazz, surname, prename, gender, address, zipCode, town, birthday, teacher) -> FlatCompetitor(
                            surname, prename, gender == "m", convertDate(birthday), address, zipCode, town, clazz, teacher
                    )}
            
        } catch (ex: ParseException) {
            throw IllegalArgumentException("Error during CSV parsing.", ex)
        }
    }

    /**
     * Converts the passed in date to a {@link Date} type.
     * The date format MUST be: dd.MM.yyyy
     * 
     * @param date the date to convert
     * 
     * @return the parsed date
     */
    private fun convertDate(date: String): Date {

        val format: DateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN)
        return format.parse(date)
    }
}
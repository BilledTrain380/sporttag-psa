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

package ch.schulealtendorf.sporttagpsa.business.parsing

import com.opencsv.bean.CsvBindByName

/**
 * Data class for CSV entries of a competitor file input.
 * 
 * @author nmaerchy
 * @version 0.0.1
 */
data class CSVCompetitor(
        
        @CsvBindByName(column = "Klasse", required = true)
        var clazz: String,
        
        @CsvBindByName(column = "Nachname", required = true)
        var surname: String,
        
        @CsvBindByName(column = "Vorname", required = true)
        var prename: String,
        
        @CsvBindByName(column = "Geschlecht", required = true)
        var gender: String,
        
        @CsvBindByName(column = "Adresse", required = true)
        var address: String,
        
        @CsvBindByName(column = "PLZ", required = true)
        var zipCode: String,
        
        @CsvBindByName(column = "Ort", required = true)
        var town: String,
        
        @CsvBindByName(column = "Geburtsdatum", required = true)
        var birthday: String,
        
        @CsvBindByName(column = "Klassenlehrer", required = true)
        var teacher: String
) {
        constructor(): this("", "", "", "", "", "", "", "", "")
}
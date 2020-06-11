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

package ch.schulealtendorf.psa.dto.participation

import com.fasterxml.jackson.annotation.JsonIgnore
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Year
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.ResourceBundle

data class BirthdayDto(
    val value: LocalDate
) {
    private val resourceBundle = ResourceBundle.getBundle("i18n.dto-terms")

    @JsonIgnore
    val age: Int = Year.now().value - value.year

    @JsonIgnore
    val year: Year = Year.of(value.year)

    companion object {
        fun parse(text: String) =
            BirthdayDto(LocalDate.parse(text))

        fun parse(text: String, formatter: DateTimeFormatter) = BirthdayDto(LocalDate.parse(text, formatter))

        fun ofDate(date: LocalDate) = BirthdayDto(date)

        fun min() = BirthdayDto(LocalDate.MIN)

        @Deprecated(message = "Will be removed", replaceWith = ReplaceWith("parse"))
        fun ofMillis(milliSeconds: Long): BirthdayDto =
            TODO()
    }

    /**
     * Formats this Birthday by the given {@code pattern}.
     *
     * Valid values are the same used in the {@link SimpleDateFormat} class constructor.
     *
     * @param pattern the format pattern of the date
     */
    fun format(pattern: String): String = SimpleDateFormat(pattern).format(value)

    /**
     * Formats the birthday based on the default locale.
     * @see Locale.getDefault
     */
    fun format() = format(resourceBundle.getString("birthday.format"))
}

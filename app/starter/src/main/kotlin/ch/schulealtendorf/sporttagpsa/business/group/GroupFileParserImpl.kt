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

package ch.schulealtendorf.sporttagpsa.business.group

import ch.schulealtendorf.psa.dto.BirthdayDto
import ch.schulealtendorf.psa.dto.GenderDto
import java.nio.charset.Charset
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Optional
import java.util.stream.Stream
import kotlin.streams.toList
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

/**
 * An implementation of a FileReader for a competitor input file.
 *
 * @author nmaerchy
 * @version 2.0.0
 * @since 1.0.0
 */
@Component
class GroupFileParserImpl : GroupFileParser {

    /**
     * Parses the given csv {@code file}.
     *
     * The file is being parsed according to the {@link CSVParticipant} class annotations.
     *
     * @param file the file to parse
     *
     * @return the content of the parsed file in form of a list of {@link FlatParticipant}
     * @throws IllegalArgumentException if the file does not match the format csv or is empty
     * @throws CSVParsingException if the given file can not be parsed
     */
    override fun parseCSV(file: MultipartFile): List<FlatParticipant> {

        if (file.contentType != "text/csv") throw IllegalArgumentException("Invalid file type: type=${file.contentType}")
        if (file.isEmpty) throw IllegalArgumentException("Can not parse empty file")

        file.inputStream.bufferedReader(Charset.forName("UTF-8")).use {

            return it.lines()
                .mapIndexed { index, line ->

                    val parts = line.split(',')

                    if (parts.size != 9) {
                        throw CSVParsingException("Can not parse line: Missing values.", index, 0)
                    }

                    val group: String = parts[0].trim()
                    val surname: String = parts[1].trim()
                    val prename: String = parts[2].trim()

                    val genderValue: String = parts[3].trim()

                    if (!(genderValue == "w" || genderValue == "m")) {
                        throw CSVParsingException("Can not parse gender: value=$genderValue", index, parts.column(3))
                    }
                    val gender = if (genderValue == "m") GenderDto.MALE else GenderDto.FEMALE

                    val address: String = parts[4]
                    val zipCode: String = parts[5]
                    val town: String = parts[6]

                    val birthdayValue: String = parts[7]
                    val birthday = birthdayValue.toDate()
                        .orElseThrow {
                            CSVParsingException(
                                "Can not parse birthday: value=$birthdayValue",
                                index,
                                parts.column(7)
                            )
                        }

                    val coach: String = parts[8]

                    FlatParticipant(
                        surname,
                        prename,
                        gender,
                        BirthdayDto.ofMillis(birthday.time),
                        address,
                        zipCode,
                        town,
                        group,
                        coach
                    )
                }.toList()
        }
    }

    private fun <T, R> Stream<T>.mapIndexed(mapper: (Int, T) -> R): Stream<R> {
        var index = 0
        return map { mapper(index++, it) }
    }

    private fun List<String>.column(endIndex: Int) = this.subList(0, endIndex).joinToString(",").length + 1

    private fun String.toDate(): Optional<Date> {

        return try {
            val format: DateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN)
            Optional.of(format.parse(this))
        } catch (exception: Exception) {
            Optional.empty()
        }
    }
}

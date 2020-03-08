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

package ch.schulealtendorf.psa.dto

/**
 * Data class representing a participant.
 *
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.0.0
 */
data class ParticipantDto @JvmOverloads constructor(
    val id: Int,
    val surname: String,
    val prename: String,
    val gender: GenderDto,
    val birthday: BirthdayDto,
    val absent: Boolean,
    val address: String,
    val town: TownDto,
    val sport: SportDto? = null
) {
    fun toBuilder() = Builder(this)

    class Builder internal constructor(
        private val dto: ParticipantDto
    ) {
        private var surname = dto.surname
        private var prename = dto.prename
        private var gender = dto.gender
        private var birthday = dto.birthday
        private var isAbsent = dto.absent
        private var address = dto.address
        private var town = dto.town
        private var sport = dto.sport

        fun setSurname(surname: String): Builder {
            this.surname = surname
            return this
        }

        fun setPrename(prename: String): Builder {
            this.prename = prename
            return this
        }

        fun setGender(gender: GenderDto): Builder {
            this.gender = gender
            return this
        }

        fun setBirthday(birthday: BirthdayDto): Builder {
            this.birthday = birthday
            return this
        }

        fun setAbsent(isAbsent: Boolean): Builder {
            this.isAbsent = isAbsent
            return this
        }

        fun setAddress(address: String): Builder {
            this.address = address
            return this
        }

        fun setTown(town: TownDto): Builder {
            this.town = town
            return this
        }

        fun setSport(sport: SportDto?): Builder {
            this.sport = sport
            return this
        }

        fun build() = dto.copy(
            surname = surname,
            prename = prename,
            gender = gender,
            birthday = birthday,
            absent = isAbsent,
            address = address,
            town = town,
            sport = sport
        )
    }
}

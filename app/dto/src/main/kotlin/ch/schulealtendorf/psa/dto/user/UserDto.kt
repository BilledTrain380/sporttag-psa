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

package ch.schulealtendorf.psa.dto.user

/**
 * Data class representing a user.
 * The {@code password} property should only be set
 * to create a new user.
 *
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.0.0
 */
data class UserDto @JvmOverloads constructor(
    val id: Int,
    val username: String,
    val authorities: List<String>,
    val enabled: Boolean = true,
    val locale: String = "en",
    val password: String = "protected"
) {
    companion object

    fun toBuilder() = Builder(this)

    class Builder internal constructor(
        private val dto: UserDto
    ) {
        private var username = dto.username
        private var isEnabled = dto.enabled
        private var locale = dto.locale

        fun setUsername(username: String): Builder {
            this.username = username
            return this
        }

        fun setEnabled(isEnabled: Boolean): Builder {
            this.isEnabled = isEnabled
            return this
        }

        fun setLocale(locale: String): Builder {
            this.locale = locale
            return this
        }

        fun build() = dto.copy(username = username, enabled = isEnabled, locale = locale)
    }
}

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

package ch.schulealtendorf.sporttagpsa.controller.rest

import javax.validation.constraints.NotNull

/*
 * Because POJOs for Spring Rest Controller needs to have a default parameter
 * and we can validate our POJOs with Spring Annotations, we make every property nullable
 * and initialize it with null (like in Java where null-safe does not exist).
 *
 * If a property is required we annotate it with @NotNull, in order to validate it with Spring.
 */

/**
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.0.0
 */
data class RestClass(

        @NotNull
        var name: String? = null,

        @NotNull
        var coach: String? = null,

        @NotNull
        var pendingParticipation: Boolean? = null
)

/**
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.0.0
 */
data class RestTown(

        @NotNull
        var id: Int? = null,

        @NotNull
        var zip: String? = null,

        @NotNull
        var name: String? = null
)

/**
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.0.0
 */
data class RestParticipant(

        @NotNull
        var id: Int? = null,

        @NotNull
        var surname: String? = null,

        @NotNull
        var prename: String? = null,

        @NotNull
        var gender: Boolean? = null,

        @NotNull
        var birthday: Long? = null,

        @NotNull
        var absent: Boolean? = null,

        @NotNull
        var address: String? = null,

        @NotNull
        var town: RestTown? = null,

        @NotNull
        var clazz: RestClass? = null,

        @NotNull
        var sport: String? = null
)

/**
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.0.0
 */
data class RestPutParticipant(

        @NotNull
        var surname: String? = null,

        @NotNull
        var prename: String? = null,

        @NotNull
        var gender: Boolean? = null,

        @NotNull
        var birthday: Long? = null,

        @NotNull
        var address: String? = null,

        @NotNull
        var absent: Boolean? = null
)

/**
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.0.0
 */
data class RestPatchParticipant(
        var town: RestTown? = null,
        var clazz: RestClass? = null,
        var sport: String? = null
)
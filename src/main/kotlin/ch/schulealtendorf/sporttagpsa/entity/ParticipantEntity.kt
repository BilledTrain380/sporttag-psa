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

package ch.schulealtendorf.sporttagpsa.entity

import ch.schulealtendorf.psa.dto.GenderDto
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

/**
 * @author nmaerchy
 * @since 2.0.0
 */
@Entity
@Table(name = "PARTICIPANT")
data class ParticipantEntity(

        @Id
        @NotNull
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int? = null,

        @NotNull
        @Size(min = 1, max = 30)
        var surname: String = "",

        @NotNull
        @Size(min = 1, max = 30)
        var prename: String = "",

        @Enumerated(EnumType.STRING)
        @NotNull
        @Size(min = 1, max = 6)
        var gender: GenderDto = GenderDto.MALE,

        @NotNull
        var birthday: Long = 0,

        @NotNull
        @Size(min = 1, max = 80)
        var address: String = "",

        @ManyToOne(cascade = [CascadeType.PERSIST])
        @JoinColumn(name = "FK_TOWN_id", referencedColumnName = "id")
        var town: TownEntity = TownEntity(),

        @ManyToOne(cascade = [CascadeType.PERSIST])
        @JoinColumn(name = "FK_GROUP_name", referencedColumnName = "name")
        var group: GroupEntity = GroupEntity(),

        @ManyToOne
        @JoinColumn(name = "FK_SPORT_name", referencedColumnName = "name")
        var sport: SportEntity? = null
)
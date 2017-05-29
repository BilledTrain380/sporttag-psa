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

import java.sql.Date
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

/**
 * @author nmaerchy
 * @version 0.0.1
 */
@Entity
@Table(name = "COMPETITOR")
data class CompetitorEntity(
        
        @Id
        @NotNull
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int?,
        
        @NotNull
        @Size(min = 1, max = 30)
        val surname: String,
        
        @NotNull
        @Size(min = 1, max = 30)
        val prename: String,
        
        @NotNull
        val gender: Boolean,
        
        @NotNull
        val birthday: Date,
        
        @NotNull
        @Size(min = 1, max = 80)
        val address: String,
        
        @ManyToOne
        @JoinColumn(name = "FK_TOWN_id", referencedColumnName = "id")
        val town: TownEntity,
        
        @ManyToOne
        @JoinColumn(name = "FK_CLAZZ_id", referencedColumnName = "id")
        val clazz: ClazzEntity,
        
        @ManyToOne
        @JoinColumn(name = "FK_SPORT_id", referencedColumnName = "id")
        val sport: SportEntity?
) {
        constructor(surname: String, prename: String, gender: Boolean, birthday: Date, address: String, town: TownEntity, clazz: ClazzEntity):
                this(null, surname, prename, gender, birthday, address, town, clazz, null)
}
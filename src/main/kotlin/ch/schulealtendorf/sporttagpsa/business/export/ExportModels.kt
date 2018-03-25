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

package ch.schulealtendorf.sporttagpsa.business.export

@Deprecated("")
data class RankingExportModel(
        var disciplines: List<DisciplineRankingExportModel> = ArrayList(),
        var disciplineGroup: DisciplineGroupRankingExportModel = DisciplineGroupRankingExportModel(),
        var total: TotalRankingExportModel = TotalRankingExportModel()
)

@Deprecated("")
data class DisciplineRankingExportModel(
        var name: String = "",
        var male: Boolean = false,
        var female: Boolean = false
)

@Deprecated("")
data class DisciplineGroupRankingExportModel(
        var male: Boolean = false,
        var female: Boolean = false
)

@Deprecated("")
data class TotalRankingExportModel(
        var male: Boolean = false,
        var female: Boolean = false
)

@Deprecated("")
data class ParticipantExportModel(
        var sports: List<SportExportModel> = ArrayList()
)

@Deprecated("")
data class SportExportModel(
        var name: String = "",
        var include: Boolean = false
)

data class RankingExport(
        val disciplines: Iterable<DisciplineExport>,
        val disciplineGroup: Iterable<Boolean>,
        val total: Iterable<Boolean>
)

data class EventSheetExport(
        val disciplines: Iterable<EventSheetDisciplineExport>
)

data class ParticipantExport(
        val sports: Iterable<SimpleSport>
)

data class DisciplineExport @JvmOverloads constructor(
        val discipline: SimpleDiscipline,
        val gender: Boolean = false
)

data class EventSheetDisciplineExport @JvmOverloads constructor(
        val discipline: SimpleDiscipline,
        val clazz: SimpleClazz,
        val gender: Boolean = false
)

data class SimpleClazz(
        val id: Int,
        val name: String
)

data class SimpleDiscipline(
        val id: Int,
        val name: String
)

data class SimpleSport(
        val id: Int,
        val name: String
)
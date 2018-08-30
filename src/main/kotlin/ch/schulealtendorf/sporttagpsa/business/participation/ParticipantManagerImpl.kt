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

package ch.schulealtendorf.sporttagpsa.business.participation

import ch.schulealtendorf.sporttagpsa.entity.*
import ch.schulealtendorf.sporttagpsa.model.*
import ch.schulealtendorf.sporttagpsa.repository.AbsentParticipantRepository
import ch.schulealtendorf.sporttagpsa.repository.ParticipantRepository
import ch.schulealtendorf.sporttagpsa.repository.TownRepository
import org.springframework.stereotype.Component
import java.util.*

/**
 * Default implementation of a {@link ParticipantManager} which uses the repository classes.
 *
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.0.0
 */
@Component
class ParticipantManagerImpl(
        private val participantRepository: ParticipantRepository,
        private val absentRepository: AbsentParticipantRepository,
        private val townRepository: TownRepository
): ParticipantManager {

    /**
     * @return a list of all participants
     */
    override fun getParticipants() =  participantRepository.findAll().map { it.toParticipant() }

    /**
     * Filters all participant by the given {@code group}.
     * Participants which are not related to the group
     * are not included in the returned list.
     *
     * @param group the group where a participants belongs to
     *
     * @return the filtered participant list
     */
    override fun getParticipants(group: Group) = participantRepository.findByGroupName(group.name).map { it.toParticipant() }

    /**
     * Filters all participants by the given {@code gender}.
     * Participants which are not equal to the gender
     * are not included in the returned list.
     *
     * @param gender the gender of the participants
     *
     * @return the filtered participant list
     */
    override fun getParticipants(gender: Gender) = participantRepository.findByGender(gender.toString()).map { it.toParticipant() }

    /**
     * Filters all participants by the given {@code group}
     * AND by the given {@code gender}.
     *
     * Participants which are not related to the group AND not
     * equal to the gender are not included in the returned list.
     *
     * @param group the group where the participants belongs to
     * @param gender the gender of the participants
     *
     * @return the filtered participant list
     */
    override fun getParticipants(group: Group, gender: Gender) = participantRepository.findByGroupAndGender(group.name, gender.toString()).map { it.toParticipant() }

    /**
     * @param id the id of the participant
     *
     * @return an Optional containing the participant or empty if the participant could not be found
     */
    override fun getParticipant(id: Int): Optional<Participant> = participantRepository.findById(id).map { it.toParticipant() }

    /**
     * Saves the given {@code participant}.
     *
     * If the {@link Participant#id} < 1, it will be created.
     *
     * If the participant exists already, it will be updated.
     *
     * If the {@link Participant#town#id} < 1, it will be created.
     *
     * The {@link Participant#group} relation will be created if it does not exist yet.
     *
     * The properties {@link Participant#absent} and {@link Participant#sport} will be ignored.
     * To update those use {@link ParticipationManager#markAsAbsent}, {@link ParticipationManager#markAsPresent}
     * or {@link ParticipationManager#participate}.
     *
     * @param participant the participant to save
     *
     * @return the created participant
     */
    override fun saveParticipant(participant: Participant): Participant {

        val participantEntity: ParticipantEntity = participantRepository.findById(participant.id)
                .orElseGet { ParticipantEntity() }

        val townEntity = townRepository.findByZipAndName(participant.town.zip, participant.town.name)
                .orElseGet { TownEntity(zip = participant.town.zip, name = participant.town.name) }

        participantEntity.apply {
            surname = participant.surname
            prename = participant.prename
            gender = participant.gender.toString()
            birthday = participant.birthday.milliseconds
            address = participant.address
            town = townEntity
            group = GroupEntity().apply {
                name = participant.group.name
                coach = CoachEntity().apply {
                    id = if (participant.group.coach.id < 1) null else participant.group.coach.id
                    name = participant.group.coach.name
                }
            }
        }

        return participantRepository.save(participantEntity).toParticipant()
    }

    private fun ParticipantEntity.toParticipant(): Participant {
        return Participant(
                id!!,
                surname,
                prename,
                Gender.valueOf(gender),
                Birthday(birthday),
                absentRepository.findByParticipantId(id!!).isPresent,
                address,
                town.toTown(),
                group.toGroup(),
                sport.toSport()
        )
    }

    private fun TownEntity.toTown() = Town(zip, name)

    private fun GroupEntity.toGroup() = Group(name, Coach(coach.id!!, coach.name))

    private fun SportEntity?.toSport(): Optional<Sport> {
        return Optional.ofNullable(
                if (this == null) this else Sport(name)
        )
    }
}

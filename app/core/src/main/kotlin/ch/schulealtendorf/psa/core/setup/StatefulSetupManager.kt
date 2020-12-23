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

package ch.schulealtendorf.psa.core.setup

import ch.schulealtendorf.psa.core.user.USER_ADMIN
import ch.schulealtendorf.psa.core.user.UserManager
import ch.schulealtendorf.psa.setup.SetupRepository
import ch.schulealtendorf.psa.setup.entity.SetupEntity.Companion.DEFAULT_SETUP
import mu.KotlinLogging
import org.springframework.stereotype.Component
import java.util.Random
import javax.annotation.PostConstruct

/**
 * A {@link SetupManager} which is stateful to reduce db access.
 *
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.0.0
 */
@Component
class StatefulSetupManager(
    private val setupRepository: SetupRepository,
    private val userManager: UserManager
) : SetupManager {
    private val log = KotlinLogging.logger {}

    private var isInit = false
    private var jwtSec = ""

    override val isInitialized: Boolean get() = isInit
    override val jwtSecret: String get() = jwtSec

    @PostConstruct
    fun init() {
        val setup = this.setupRepository.findById(DEFAULT_SETUP)
            .orElseThrow { IllegalStateException("Setup \"$DEFAULT_SETUP\" does not exist. Check your database") }
        isInit = setup.initialized
        jwtSec = setup.jwtSecret
    }

    override fun initialize(setup: SetupInformation) {
        if (isInitialized) {
            throw IllegalStateException("Setup is already initialized")
        }

        log.info { "Initialize PSA" }

        // Set admin password
        val user = userManager.getOne(USER_ADMIN).get()
        userManager.changePassword(user, setup.adminPassword)

        // Mark setup as initialized and set new JWT secret
        val setupEntity = setupRepository.findById(DEFAULT_SETUP).get()
        setupEntity.apply {
            initialized = true
            jwtSecret = generateJWTSecret(32)
        }
        setupRepository.save(setupEntity)

        isInit = true
        jwtSec = setupEntity.jwtSecret

        log.info { "Successfully initialized PSA" }
    }

    override fun generateJWTSecret(length: Int): String {
        val random = Random()
        val buffer = StringBuffer()
        while (buffer.length < length) {
            buffer.append(Integer.toHexString(random.nextInt()))
        }

        return buffer.toString().substring(0, length)
    }

    override fun replaceJWTSecret(secret: String) {
        val setup = this.setupRepository.findById(DEFAULT_SETUP).get()
        this.setupRepository.save(setup.apply { jwtSecret = secret })
        jwtSec = secret
    }
}

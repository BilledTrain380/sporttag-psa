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

package ch.schulealtendorf.sporttagpsa.controller.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer
import org.springframework.security.oauth2.provider.token.DefaultTokenServices

/**
 * Configures the resource server.
 *
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.0.0
 */
@Configuration
@EnableResourceServer
class ResourceServerConfig(
        private val tokenServices: DefaultTokenServices
): ResourceServerConfigurerAdapter() {

    override fun configure(http: HttpSecurity?) {

        http
                ?.antMatcher("/api/**")
                ?.authorizeRequests()

                    ?.antMatchers(
                            "/api/rest/groups",
                            "/api/rest/participation",
                            "/api/rest/competitors",
                            "/api/rest/competitor/**",
                            "/api/rest/sports",
                            "/api/rest/disciplines"
                    )?.hasRole("USER")

                    ?.antMatchers(
                            "/api/rest/group/**",
                            "/api/rest/participant/**",
                            "/api/rest/participants",
                            "/api/rest/users",
                            "/api/rest/user/**",
                            "/api/web/group-import",
                            "/api/web/ranking",
                            "/api/web/event-sheets",
                            "/api/web/file/**",
                            "/api/web/participant-list"
                    )?.hasRole("ADMIN")

                    ?.anyRequest()?.authenticated()

                ?.and()
                ?.csrf()?.disable()
                ?.cors()
    }

    override fun configure(resources: ResourceServerSecurityConfigurer?) {
        resources?.tokenServices(tokenServices)
    }
}
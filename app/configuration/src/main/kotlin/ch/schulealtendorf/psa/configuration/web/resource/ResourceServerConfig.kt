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

package ch.schulealtendorf.psa.configuration.web.resource

import ch.schulealtendorf.psa.dto.oauth.PSAScope
import ch.schulealtendorf.psa.dto.oauth.SecurityRequirementNames
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.info.License
import io.swagger.v3.oas.annotations.security.OAuthFlow
import io.swagger.v3.oas.annotations.security.OAuthFlows
import io.swagger.v3.oas.annotations.security.OAuthScope
import io.swagger.v3.oas.annotations.security.SecurityScheme
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
@OpenAPIDefinition(
    info = Info(
        title = "PSA API",
        version = "1.0.0",
        license = License(
            name = "GNU General Public License v3.0",
            url = "https://github.com/BilledTrain380/sporttag-psa/blob/master/LICENSE.md"
        )
    )
)
@SecurityScheme(
    name = SecurityRequirementNames.OAUTH2,
    type = SecuritySchemeType.OAUTH2,
    flows = OAuthFlows(
        implicit = OAuthFlow(
            authorizationUrl = "/oauth/authorize",
            tokenUrl = "/oauth/token",
            scopes = [
                OAuthScope(name = PSAScope.GROUP_READ, description = "Allows to read groups resources"),
                OAuthScope(name = PSAScope.GROUP_WRITE, description = "Allows to modify groups resources"),
                OAuthScope(name = PSAScope.PARTICIPANT_READ, description = "Allows to read participant resources"),
                OAuthScope(name = PSAScope.PARTICIPANT_WRITE, description = "Allows to modify participant resources"),
                OAuthScope(name = PSAScope.COMPETITOR_READ, description = "Allows to read competitor resources"),
                OAuthScope(name = PSAScope.COMPETITOR_WRITE, description = "Allows to modify competitor resources"),
                OAuthScope(name = PSAScope.DISCIPLINE_READ, description = "Allows to read discipline resources"),
                OAuthScope(name = PSAScope.SPORT_READ, description = "Allows to read sport resources"),
                OAuthScope(name = PSAScope.PARTICIPATION, description = "Access participation resources"),
                OAuthScope(name = PSAScope.RANKING, description = "Access ranking"),
                OAuthScope(name = PSAScope.EVENT_SHEETS, description = "Access event sheets"),
                OAuthScope(name = PSAScope.USER, description = "Access to user management)")
            ]
        )
    )
)
class ResourceServerConfig(
    private val tokenServices: DefaultTokenServices
) : ResourceServerConfigurerAdapter() {

    override fun configure(http: HttpSecurity?) {
        http
            ?.authorizeRequests()
            ?.antMatchers(
                "/api/swagger-ui/**",
                "/v3/api-docs/**"
            )?.permitAll()
            ?.antMatchers(
                "/api/groups",
                "/api/participation",
                "/api/competitors",
                "/api/competitor/**",
                "/api/sports",
                "/api/disciplines",
                "/api/ranking/download"
            )?.hasRole("USER")
            ?.antMatchers(
                "/api/groups/overview",
                "/api/groups/import",
                "/api/group/**",
                "/api/participant/**",
                "/api/participants",
                "/api/event-sheets/**",
                "/api/users",
                "/api/user/**"
            )?.hasRole("ADMIN")

            ?.anyRequest()?.authenticated()

            ?.and()
            ?.exceptionHandling()
            ?.accessDeniedHandler(CustomAccessDeniedHandler())
            ?.and()
            ?.csrf()?.disable()
            ?.cors()
    }

    override fun configure(resources: ResourceServerSecurityConfigurer?) {
        resources?.tokenServices(tokenServices)
    }
}

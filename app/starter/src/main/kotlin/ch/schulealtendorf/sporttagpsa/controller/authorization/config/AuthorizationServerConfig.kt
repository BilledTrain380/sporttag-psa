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

package ch.schulealtendorf.sporttagpsa.controller.authorization.config

import ch.schulealtendorf.sporttagpsa.business.setup.SetupManager
import ch.schulealtendorf.sporttagpsa.controller.oauth.PSAScope
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer
import org.springframework.security.oauth2.provider.token.DefaultTokenServices
import org.springframework.security.oauth2.provider.token.TokenEnhancer
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
import java.time.Duration

/**
 * Configures OAuth 2 authorization server.
 *
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.0.0
 */
@Configuration
@EnableAuthorizationServer
class AuthorizationServerConfig(
    @Qualifier("security-config")
    private val authenticationManager: AuthenticationManager,
    private val setupManager: SetupManager,
    @Qualifier("psa")
    private val tokenEnhancer: TokenEnhancer
) : AuthorizationServerConfigurerAdapter() {
    companion object {
        @JvmField
        val TOKEN_VALIDITY_DURATION: Duration = Duration.ofHours(2)
    }

    override fun configure(security: AuthorizationServerSecurityConfigurer?) {
        security
            ?.tokenKeyAccess("permitAll()")
            ?.checkTokenAccess("isAuthenticated()")
    }

    override fun configure(clients: ClientDetailsServiceConfigurer?) {
        clients
            ?.inMemory()
            ?.withClient("psa-kitten")
            ?.secret("very-secret")
            ?.autoApprove(true)
            ?.authorities("ADMIN", "USER")
            ?.authorizedGrantTypes("implicit", "authorization_code")
            ?.accessTokenValiditySeconds(TOKEN_VALIDITY_DURATION.seconds.toInt())
            ?.scopes(
                PSAScope.USER,
                PSAScope.GROUP_READ,
                PSAScope.GROUP_WRITE,
                PSAScope.SPORT_READ,
                PSAScope.DISCIPLINE_READ,
                PSAScope.COMPETITOR_READ,
                PSAScope.COMPETITOR_WRITE,
                PSAScope.PARTICIPANT_READ,
                PSAScope.PARTICIPANT_WRITE,
                PSAScope.PARTICIPATION,
                PSAScope.FILES,
                PSAScope.RANKING,
                PSAScope.EVENT_SHEETS,
                PSAScope.PARTICIPANT_LIST
            )
    }

    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer?) {

        val tokenEnhancerChain = TokenEnhancerChain().apply {
            setTokenEnhancers(listOf(tokenEnhancer, tokenConverter()))
        }

        endpoints
            ?.tokenStore(tokenStore())
            ?.tokenEnhancer(tokenEnhancerChain)
            ?.authenticationManager(authenticationManager)
    }

    @Bean
    fun tokenStore(): TokenStore = InMemoryTokenStore()

    @Bean
    fun tokenConverter() = JwtAccessTokenConverter().apply { setSigningKey(setupManager.jwtSecret) }

    @Bean
    @Primary
    fun tokenService(): DefaultTokenServices {
        return DefaultTokenServices().apply {
            setTokenStore(tokenStore())
            setSupportRefreshToken(true)
        }
    }
}

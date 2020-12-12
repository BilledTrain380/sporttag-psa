package ch.schulealtendorf.psa.service.user

import ch.schulealtendorf.psa.dto.oauth.SecurityRequirementNames
import ch.schulealtendorf.psa.dto.oauth.TokenRevokeDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.oauth2.provider.token.DefaultTokenServices
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
@Tag(name = "Oauth", description = "Access oauth resources")
@SecurityRequirement(name = SecurityRequirementNames.OAUTH2)
class OauthController(
    private val tokenServices: DefaultTokenServices
) {

    @Operation(
        summary = "Revoke an access token",
        tags = ["Oauth"],
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Successful revoked an access token",
                content = [Content()]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content()]
            )
        ]
    )
    @PostMapping("/oauth/token/revoke")
    fun revokeAccessToken(@RequestBody tokenRevokeDto: TokenRevokeDto) {
        tokenServices.revokeToken(tokenRevokeDto.token)
    }
}

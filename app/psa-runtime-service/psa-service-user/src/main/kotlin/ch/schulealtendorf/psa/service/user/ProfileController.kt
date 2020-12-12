package ch.schulealtendorf.psa.service.user

import ch.schulealtendorf.psa.core.user.UserManager
import ch.schulealtendorf.psa.dto.oauth.PSAScope
import ch.schulealtendorf.psa.dto.oauth.SecurityRequirementNames
import ch.schulealtendorf.psa.dto.user.ProfileElement
import ch.schulealtendorf.psa.service.standard.exception.web.NotFoundException
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

/**
 * Controller to manage the profile of the authenticated user.
 */
@RestController
@RequestMapping("/api")
@Tag(name = "Profile", description = "Manage profile")
@SecurityRequirement(name = SecurityRequirementNames.OAUTH2, scopes = [PSAScope.PROFILE])
class ProfileController(
    private val userManager: UserManager
) {

    @Operation(
        summary = "Update the profile",
        tags = ["Profile"],
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Successful updated the profile",
                content = [Content()]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content()]
            )
        ]
    )
    @PreAuthorize("#oauth2.hasScope('profile')")
    @PatchMapping("/profile")
    fun changeLocale(@RequestBody profileElement: ProfileElement, principal: Principal) {
        val user = userManager.getOne(principal.name)
            .orElseThrow { NotFoundException("Could not find user: username=${principal.name}") }
            .toBuilder()
            .setLocale(profileElement.locale.value)
            .build()

        userManager.save(user)
    }
}

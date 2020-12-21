package ch.schulealtendorf.psa.service.core

import ch.schulealtendorf.psa.configuration.BuildInfo
import ch.schulealtendorf.psa.dto.about.BuildInfoDto
import ch.schulealtendorf.psa.dto.oauth.SecurityRequirementNames
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
@Tag(name = "About", description = "Access about resources")
@SecurityRequirement(name = SecurityRequirementNames.OAUTH2)
class AboutController {

    @Operation(
        summary = "Access the build info",
        tags = ["About"]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Load build info",
                content = [Content(schema = Schema(implementation = BuildInfoDto::class))]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content()]
            )
        ]
    )
    @GetMapping("/build-info")
    fun getBuildInfo(): BuildInfoDto {
        return BuildInfoDto(
            BuildInfo.VERSION,
            BuildInfo.HASH,
            BuildInfo.BUILD_TIME,
            BuildInfo.ENVIRONMENT
        )
    }
}

package ch.schulealtendorf.sporttagpsa.controller.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.info.License
import io.swagger.v3.oas.annotations.security.SecurityRequirement

@OpenAPIDefinition(
    info = Info(
        title = "PSA API",
        version = "1.0.0",
        license = License(
            name = "GNU General Public License v3.0",
            url = "https://github.com/BilledTrain380/sporttag-psa/blob/master/LICENSE.md"
        )
    ),
    security = [SecurityRequirement(name = SecurityRequirementNames.OAUTH2, scopes = [PSAScope.COMPETITOR_WRITE])]
)
class OpenApiDefinition {
}
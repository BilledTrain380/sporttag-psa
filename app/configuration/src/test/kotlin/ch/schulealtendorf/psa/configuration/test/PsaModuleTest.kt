package ch.schulealtendorf.psa.configuration.test

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication(scanBasePackages = ["ch.schulealtendorf.psa"])
@EnableJpaRepositories(basePackages = ["ch.schulealtendorf.psa"])
@EntityScan(basePackages = ["ch.schulealtendorf.psa"])
annotation class PsaModuleTest

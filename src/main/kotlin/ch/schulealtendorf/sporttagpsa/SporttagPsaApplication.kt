package ch.schulealtendorf.sporttagpsa

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication(scanBasePackages = ["ch.schulealtendorf.sporttagpsa", "ch.schulealtendorf.psa"])
class SporttagPsaApplication

fun main(args: Array<String>) {
    SpringApplication.run(SporttagPsaApplication::class.java, *args)
}

package ch.schulealtendorf.psa.configuration

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class BuildInfoTest {

    @Test
    internal fun readBuildInfoJson() {
        assertThat(BuildInfo.VERSION).isEqualTo("1.0.0")
        assertThat(BuildInfo.HASH).isEqualTo("ab24ed8")
        assertThat(BuildInfo.ENVIRONMENT).isEqualTo("dev")
        assertThat(BuildInfo.BUILD_TIME).isEqualTo("2020-01-01T00:00:00.00000Z")

        val buildInfoJson =
            BuildInfoTest::class.java.getResourceAsStream("/build-info.json").bufferedReader().use { it.readText() }
        assertThat(BuildInfo.BUILD_INFO_JSON).isEqualTo(buildInfoJson)
    }
}

package ch.schulealtendorf.psa.service.group

import ch.schulealtendorf.psa.configuration.test.PsaWebMvcTest
import ch.schulealtendorf.psa.dto.oauth.PSAScope
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

internal class GroupControllerTest : PsaWebMvcTest() {
    companion object {
        private const val GROUP_2A_ENDPOINT = "/api/group/2a"
        private const val GROUPS_ENDPOINT = "/api/groups"
        private const val OVERVIEW_GROUP_ENDPOINT = "/api/groups/overview"
        private const val GROUP_IMPORT_ENDPOINT = "/api/groups/import"

        private val CONTENT = GroupControllerTest::class.java.getResourceAsStream("/parsing/test-group-import.csv")
        private val GROUPS_CSV = MockMultipartFile("group-input", "groups.csv", "text/csv", CONTENT)
    }

    @Test
    internal fun getGroupByName() {
        mockMvc.perform(
            get(GROUP_2A_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.GROUP_READ))
        ).andExpect(status().isOk)
    }

    @Test
    internal fun getGroupByNameWhenUnauthorized() {
        mockMvc.perform(get(GROUP_2A_ENDPOINT))
            .andExpect(status().isUnauthorized)
    }

    @Test
    internal fun getGroupByNameWhenMissingScope() {
        mockMvc.perform(
            get(GROUP_2A_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.COMPETITOR_READ))
        ).andExpect(status().isNotFound)
    }

    @Test
    internal fun getGroupByNameWhenInvalidAuthority() {
        mockMvc.perform(
            get(GROUP_2A_ENDPOINT)
                .with(bearerTokenUser(PSAScope.GROUP_READ))
        ).andExpect(status().isNotFound)
    }

    @Test
    internal fun getGroups() {
        mockMvc.perform(
            get(GROUPS_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.GROUP_READ))
        ).andExpect(status().isOk)

        mockMvc.perform(
            get(GROUPS_ENDPOINT)
                .with(bearerTokenUser(PSAScope.GROUP_READ))
        ).andExpect(status().isOk)
    }

    @Test
    internal fun getGroupsWhenUnauthorized() {
        mockMvc.perform(get(GROUPS_ENDPOINT))
            .andExpect(status().isUnauthorized)
    }

    @Test
    internal fun getGroupsWhenMissingScope() {
        mockMvc.perform(
            get(GROUPS_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.COMPETITOR_READ))
        ).andExpect(status().isNotFound)
    }

    @Test
    internal fun getOverviewGroups() {
        mockMvc.perform(
            get(OVERVIEW_GROUP_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.GROUP_READ))
        ).andExpect(status().isOk)
    }

    @Test
    internal fun getOverviewGroupsWhenUnauthorized() {
        mockMvc.perform(get(OVERVIEW_GROUP_ENDPOINT))
            .andExpect(status().isUnauthorized)
    }

    @Test
    internal fun getOverviewGroupsWhenMissingScope() {
        mockMvc.perform(
            get(OVERVIEW_GROUP_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.COMPETITOR_READ))
        ).andExpect(status().isNotFound)
    }

    @Test
    internal fun getOverviewGroupsWhenInvalidAuthority() {
        mockMvc.perform(
            get(OVERVIEW_GROUP_ENDPOINT)
                .with(bearerTokenUser(PSAScope.GROUP_READ))
        ).andExpect(status().isNotFound)
    }

    @Test
    @DirtiesContext
    internal fun importGroups() {
        mockMvc.perform(
            multipart(GROUP_IMPORT_ENDPOINT)
                .file(GROUPS_CSV)
                .with(bearerTokenAdmin(PSAScope.GROUP_WRITE))
        ).andExpect(status().isOk)
    }

    @Test
    internal fun importGroupsWhenInvalidCsvContent() {
        val content =
            GroupControllerTest::class.java.getResourceAsStream("/parsing/test-group-import-invalid-gender.csv")
        val invalidCsv = MockMultipartFile("group-input", "groups.csv", "text/csv", content)

        mockMvc.perform(
            multipart(GROUP_IMPORT_ENDPOINT)
                .file(invalidCsv)
                .with(bearerTokenAdmin(PSAScope.GROUP_WRITE))
        ).andExpect(status().isBadRequest)
    }

    @Test
    internal fun importGroupsWhenInvalidFileType() {
        val content =
            GroupControllerTest::class.java.getResourceAsStream("/parsing/test-group-import-invalid-gender.csv")
        val invalidCsv = MockMultipartFile("group-input", "groups.txt", MediaType.TEXT_PLAIN_VALUE, content)

        mockMvc.perform(
            multipart(GROUP_IMPORT_ENDPOINT)
                .file(invalidCsv)
                .with(bearerTokenAdmin(PSAScope.GROUP_WRITE))
        ).andExpect(status().isBadRequest)
    }

    @Test
    internal fun importGroupsWhenUnauthorized() {
        mockMvc.perform(
            multipart(GROUP_IMPORT_ENDPOINT)
                .file(GROUPS_CSV)
        ).andExpect(status().isUnauthorized)
    }

    @Test
    internal fun importGroupsWhenMissingScope() {
        mockMvc.perform(
            multipart(GROUP_IMPORT_ENDPOINT)
                .file(GROUPS_CSV)
                .with(bearerTokenAdmin(PSAScope.RANKING))
        ).andExpect(status().isNotFound)
    }

    @Test
    internal fun importGroupsWhenMissingAuthority() {
        mockMvc.perform(
            multipart(GROUP_IMPORT_ENDPOINT)
                .file(GROUPS_CSV)
                .with(bearerTokenUser(PSAScope.GROUP_WRITE))
        ).andExpect(status().isNotFound)
    }
}

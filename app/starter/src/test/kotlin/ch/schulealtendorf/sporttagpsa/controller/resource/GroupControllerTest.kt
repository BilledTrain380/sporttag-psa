package ch.schulealtendorf.sporttagpsa.controller.resource

import ch.schulealtendorf.sporttagpsa.controller.PsaWebMvcTest
import ch.schulealtendorf.sporttagpsa.controller.oauth.PSAScope
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

internal class GroupControllerTest : PsaWebMvcTest() {
    companion object {
        private const val GROUP_2A_ENDPOINT = "/api/group/2a"
        private const val GROUPS_ENDPOINT = "/api/groups"
        private const val OVERVIEW_GROUP_ENDPOINT = "/api/groups/overview"
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
}

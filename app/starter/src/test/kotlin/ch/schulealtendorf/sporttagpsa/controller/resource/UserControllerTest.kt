package ch.schulealtendorf.sporttagpsa.controller.resource

import ch.schulealtendorf.psa.dto.user.UserElement
import ch.schulealtendorf.psa.dto.user.UserInput
import ch.schulealtendorf.psa.dto.user.UserRelation
import ch.schulealtendorf.sporttagpsa.controller.PsaWebMvcTest
import ch.schulealtendorf.sporttagpsa.controller.oauth.PSAScope
import org.hamcrest.Matchers.hasItem
import org.hamcrest.Matchers.not
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

internal class UserControllerTest : PsaWebMvcTest() {
    companion object {
        private const val USERS_ENDPOINT = "/api/users"
        private const val ADMIN_USER_ENDPOINT = "/api/user/1"
        private const val NORMAL_USER_ENDPOINT = "/api/user/2"
        private const val MMUSTER_USER_ENDPOINT = "/api/user/3"
        private const val WWIRBELWIND_USER_ENDPOINT = "/api/user/4"

        private val USER_INPUT = UserInput(
            username = "Test user",
            enabled = true,
            password = "Secret12345$"
        )

        private val USER_ELEMENT = UserElement(
            username = "wwibelwind",
            enabled = false
        )

        private val USER_RELATION = UserRelation(
            password = "newSecretPassword$12345"
        )
    }

    @Test
    internal fun getUsers() {
        mockMvc.perform(
            get(USERS_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.USER))
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$[*].username", not(hasItem(ADMIN_USER))))
    }

    @Test
    internal fun getUsersWhenUnauthorized() {
        mockMvc.perform(get(USERS_ENDPOINT))
            .andExpect(status().isUnauthorized)
    }

    @Test
    internal fun getUsersWhenMissingScope() {
        mockMvc.perform(
            get(USERS_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.SPORT_READ))
        ).andExpect(status().isNotFound)
    }

    @Test
    internal fun getUsersWhenMissingAuthority() {
        mockMvc.perform(
            get(USERS_ENDPOINT)
                .with(bearerTokenUser(PSAScope.USER))
        ).andExpect(status().isNotFound)
    }

    @Test
    @DirtiesContext
    internal fun createUser() {
        mockMvc.perform(
            post(USERS_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.USER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBodyOf(USER_INPUT))
        ).andExpect(status().isOk)
    }

    @Test
    internal fun createUserWhenUnauthorized() {
        mockMvc.perform(
            post(USERS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBodyOf(USER_INPUT))
        ).andExpect(status().isUnauthorized)
    }

    @Test
    internal fun createUserWhenMissingScope() {
        mockMvc.perform(
            post(USERS_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.COMPETITOR_WRITE))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBodyOf(USER_INPUT))
        ).andExpect(status().isNotFound)
    }

    @Test
    internal fun createUserWhenMissingAuthority() {
        mockMvc.perform(
            post(USERS_ENDPOINT)
                .with(bearerTokenUser(PSAScope.USER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBodyOf(USER_INPUT))
        ).andExpect(status().isNotFound)
    }

    @Test
    internal fun getUserByIdAsAdmin() {
        mockMvc.perform(
            get(ADMIN_USER_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.USER))
        ).andExpect(status().isOk)

        mockMvc.perform(
            get(NORMAL_USER_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.USER))
        ).andExpect(status().isOk)
    }

    @Test
    internal fun getUserWhenUnauthorized() {
        mockMvc.perform(get(ADMIN_USER_ENDPOINT))
            .andExpect(status().isUnauthorized)
    }

    @Test
    internal fun getUserWhenMissingScope() {
        mockMvc.perform(
            get(NORMAL_USER_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.COMPETITOR_WRITE))
        ).andExpect(status().isNotFound)
    }

    @Test
    internal fun getUserByIdWhenMissingAuthority() {
        mockMvc.perform(
            get(NORMAL_USER_ENDPOINT)
                .with(bearerTokenUser(PSAScope.USER))
        ).andExpect(status().isNotFound)

        mockMvc.perform(
            get(ADMIN_USER_ENDPOINT)
                .with(bearerTokenUser(PSAScope.USER))
        ).andExpect(status().isNotFound)
    }

    @Test
    @DirtiesContext
    internal fun updateUser() {
        mockMvc.perform(
            patch(WWIRBELWIND_USER_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.USER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBodyOf(USER_ELEMENT))
        ).andExpect(status().isOk)
    }

    @Test
    internal fun updateAdminUser() {
        mockMvc.perform(
            patch(ADMIN_USER_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.USER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBodyOf(USER_ELEMENT))
        ).andExpect(status().isBadRequest)
    }

    @Test
    internal fun updateUserWhenUnauthorized() {
        mockMvc.perform(
            patch(WWIRBELWIND_USER_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBodyOf(USER_ELEMENT))
        ).andExpect(status().isUnauthorized)
    }

    @Test
    internal fun updateUserWhenMissingScope() {
        mockMvc.perform(
            patch(WWIRBELWIND_USER_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.COMPETITOR_WRITE))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBodyOf(USER_ELEMENT))
        ).andExpect(status().isNotFound)
    }

    @Test
    internal fun updateUserWhenMissingAuthority() {
        mockMvc.perform(
            patch(WWIRBELWIND_USER_ENDPOINT)
                .with(bearerTokenUser(PSAScope.USER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBodyOf(USER_ELEMENT))
        ).andExpect(status().isNotFound)
    }

    @Test
    internal fun updateUserRelationWhenBadRequest() {
        val userRelation = UserRelation(
            password = "bad-password"
        )

        mockMvc.perform(
            put(WWIRBELWIND_USER_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.USER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBodyOf(userRelation))
        ).andExpect(status().isBadRequest)
    }

    @Test
    @DirtiesContext
    internal fun updateUserRelation() {
        mockMvc.perform(
            put(WWIRBELWIND_USER_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.USER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBodyOf(USER_RELATION))
        ).andExpect(status().isOk)
    }

    @Test
    internal fun updateUserRelationWhenUnauthorized() {
        mockMvc.perform(
            put(WWIRBELWIND_USER_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBodyOf(USER_RELATION))
        ).andExpect(status().isUnauthorized)
    }

    @Test
    internal fun updateUserRelationWhenMissingScope() {
        mockMvc.perform(
            put(WWIRBELWIND_USER_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.COMPETITOR_WRITE))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBodyOf(USER_RELATION))
        ).andExpect(status().isNotFound)
    }

    @Test
    internal fun updateUserRelationWhenMissingAuthority() {
        mockMvc.perform(
            put(WWIRBELWIND_USER_ENDPOINT)
                .with(bearerTokenUser(PSAScope.USER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBodyOf(USER_RELATION))
        ).andExpect(status().isNotFound)
    }

    @Test
    @DirtiesContext
    internal fun deleteUser() {
        mockMvc.perform(
            delete(MMUSTER_USER_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.USER))
        ).andExpect(status().isOk)
    }

    @Test
    internal fun deleteUserWhenUnauthorized() {
        mockMvc.perform(delete(MMUSTER_USER_ENDPOINT))
            .andExpect(status().isUnauthorized)
    }

    @Test
    internal fun deleteUserWhenMissingScope() {
        mockMvc.perform(
            delete(MMUSTER_USER_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.COMPETITOR_WRITE))
        ).andExpect(status().isNotFound)
    }

    @Test
    internal fun deleteUserWhenMissingAuthority() {
        mockMvc.perform(
            delete(MMUSTER_USER_ENDPOINT)
                .with(bearerTokenUser(PSAScope.USER))
        ).andExpect(status().isNotFound)
    }

    @Test
    internal fun deleteAdminUser() {
        mockMvc.perform(
            delete(ADMIN_USER_ENDPOINT)
                .with(bearerTokenAdmin(PSAScope.USER))
        ).andExpect(status().isBadRequest)
    }
}

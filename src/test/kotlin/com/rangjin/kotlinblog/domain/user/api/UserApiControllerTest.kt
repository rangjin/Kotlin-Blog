package com.rangjin.kotlinblog.domain.user.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.rangjin.kotlinblog.domain.user.application.UserService
import com.rangjin.kotlinblog.domain.user.dto.request.UserCreateRequestDto
import com.rangjin.kotlinblog.domain.user.dto.request.UserDeleteRequestDto
import com.rangjin.kotlinblog.global.common.DataSweepExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.transaction.annotation.Transactional

@AutoConfigureMockMvc
@SpringBootTest
@ExtendWith(DataSweepExtension::class)
class UserApiControllerTest @Autowired constructor(

    private val mvc: MockMvc,

    private val objectMapper: ObjectMapper,

    private val userService: UserService,

) {

    @Test
    @Transactional
    fun `user signup api`() {
        // given
        val request = UserCreateRequestDto("email@ursuu.com", "password", "username")

        // when
        val result = mvc.perform(
            MockMvcRequestBuilders
                .post("/api/v1/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )

        // then
        result
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.email").value("email@ursuu.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.username").value("username"))
    }

    @Test
    @Transactional
    fun `not blank validation error exception`() {
        // given
        val request = UserCreateRequestDto(null, "password", "username")

        // when
        val result = mvc.perform(
            MockMvcRequestBuilders
                .post("/api/v1/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )

        // then
        result
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.status").value("BAD_REQUEST"))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.field").value("email"))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.message").value("이메일은 빈 칸일 수 없습니다"))
    }

    @Test
    @Transactional
    fun `email validation error exception`() {
        // given
        val request = UserCreateRequestDto("email", "password", "username")

        // when
        val result = mvc.perform(
            MockMvcRequestBuilders
                .post("/api/v1/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )

        // then
        result
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.status").value("BAD_REQUEST"))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.field").value("email"))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.message").value("이메일 형식의 입력이어야 합니다"))
    }

    @Test
    @Transactional
    fun `user delete api`() {
        // given
        userService.create(UserCreateRequestDto("email@ursuu.com", "password", "username"))
        val request = UserDeleteRequestDto("email@ursuu.com", "password")

        // when
        val result = mvc.perform(
            MockMvcRequestBuilders
                .delete("/api/v1/user/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )

        // then
        result
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

}
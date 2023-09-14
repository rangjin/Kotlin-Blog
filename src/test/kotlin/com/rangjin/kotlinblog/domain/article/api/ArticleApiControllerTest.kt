package com.rangjin.kotlinblog.domain.article.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.rangjin.kotlinblog.domain.article.application.ArticleService
import com.rangjin.kotlinblog.domain.article.dto.request.ArticleCreateOrUpdateRequestDto
import com.rangjin.kotlinblog.domain.article.dto.request.ArticleDeleteRequestDto
import com.rangjin.kotlinblog.domain.user.application.UserService
import com.rangjin.kotlinblog.domain.user.dto.request.UserCreateRequestDto
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.transaction.annotation.Transactional

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class ArticleApiControllerTest @Autowired constructor(

    private val mvc: MockMvc,

    private val objectMapper: ObjectMapper,

    private val userService: UserService,

    private val articleService: ArticleService,

) {

    @Test
    fun `article create api`() {
        // given
        userService.create(UserCreateRequestDto("email@ursuu.com", "password", "username"))
        val request = ArticleCreateOrUpdateRequestDto("email@ursuu.com", "password", "title", "content")

        // when
        val result = mvc.perform(
            MockMvcRequestBuilders
                .post("/api/v1/article/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )

        // then
        result
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.email").value("email@ursuu.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.title").value("title"))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.content").value("content"))
    }

    @Test
    fun `article update api`() {
        // given
        userService.create(UserCreateRequestDto("email@ursuu.com", "password", "username"))
        val articleId = articleService.create(ArticleCreateOrUpdateRequestDto("email@ursuu.com", "password", "title", "content")).articleId
        val request = ArticleCreateOrUpdateRequestDto("email@ursuu.com", "password", "title2", "content2")

        // when
        val result = mvc.perform(
            MockMvcRequestBuilders
                .put("/api/v1/article/update/$articleId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )

        // then
        result
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.articleId").value(articleId))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.email").value("email@ursuu.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.title").value("title2"))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.content").value("content2"))
    }

    @Test
    fun `article delete api`() {
        // given
        userService.create(UserCreateRequestDto("email@ursuu.com", "password", "username"))
        val articleId = articleService.create(ArticleCreateOrUpdateRequestDto("email@ursuu.com", "password", "title", "content")).articleId
        val request = ArticleDeleteRequestDto("email@ursuu.com", "password")

        // when
        val result = mvc.perform(
            MockMvcRequestBuilders
                .delete("/api/v1/article/delete/$articleId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )

        // then
        result
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

}
package com.rangjin.kotlinblog.domain.article.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.rangjin.kotlinblog.domain.article.application.ArticleService
import com.rangjin.kotlinblog.domain.article.dto.request.ArticleCreateOrUpdateRequestDto
import com.rangjin.kotlinblog.domain.article.dto.request.ArticleDeleteRequestDto
import com.rangjin.kotlinblog.domain.comment.application.CommentService
import com.rangjin.kotlinblog.domain.comment.dto.request.CommentCreateOrUpdateRequestDto
import com.rangjin.kotlinblog.domain.user.application.UserService
import com.rangjin.kotlinblog.domain.user.dto.request.UserCreateRequestDto
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
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
class ArticleApiControllerTest @Autowired constructor(

    private val mvc: MockMvc,

    private val objectMapper: ObjectMapper,

    private val userService: UserService,

    private val articleService: ArticleService,

) {

    companion object {

        @BeforeAll
        @JvmStatic
        fun beforeAll (

            @Autowired userService: UserService,

            @Autowired articleService: ArticleService,

            ) {
            userService.create(UserCreateRequestDto("email@ursuu.com", "password", "username"))
            articleService.create(ArticleCreateOrUpdateRequestDto("email@ursuu.com", "password", "title", "content"))
        }

    }

    @Test
    @Transactional
    fun `article create api`() {
        // given
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
            .andExpect(MockMvcResultMatchers.jsonPath("\$.articleId").value("2"))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.email").value("email@ursuu.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.title").value("title"))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.content").value("content"))
    }

    @Test
    @Transactional
    fun `article update api`() {
        // given
        val request = ArticleCreateOrUpdateRequestDto("email@ursuu.com", "password", "title2", "content2")

        // when
        val result = mvc.perform(
            MockMvcRequestBuilders
                .put("/api/v1/article/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )

        // then
        result
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.articleId").value("1"))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.email").value("email@ursuu.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.title").value("title2"))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.content").value("content2"))
    }

    @Test
    @Transactional
    fun `article delete api`() {
        // given
        val request = ArticleDeleteRequestDto("email@ursuu.com", "password")

        // when
        val result = mvc.perform(
            MockMvcRequestBuilders
                .delete("/api/v1/article/delete/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )

        // then
        result
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

}
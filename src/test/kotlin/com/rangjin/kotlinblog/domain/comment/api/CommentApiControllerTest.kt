package com.rangjin.kotlinblog.domain.comment.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.rangjin.kotlinblog.domain.article.application.ArticleService
import com.rangjin.kotlinblog.domain.article.dto.request.ArticleCreateOrUpdateRequestDto
import com.rangjin.kotlinblog.domain.comment.application.CommentService
import com.rangjin.kotlinblog.domain.comment.dto.request.CommentCreateOrUpdateRequestDto
import com.rangjin.kotlinblog.domain.comment.dto.request.CommentDeleteRequestDto
import com.rangjin.kotlinblog.domain.user.application.UserService
import com.rangjin.kotlinblog.domain.user.dto.request.UserCreateRequestDto
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
class CommentApiControllerTest @Autowired constructor(

    private val mvc: MockMvc,

    private val objectMapper: ObjectMapper,

    private val commentService: CommentService,

    private val articleService: ArticleService,

    private val userService: UserService,

) {

    @Test
    @Transactional
    fun `comment create api`() {
        // given
        userService.create(UserCreateRequestDto("email@ursuu.com", "password", "username"))
        val articleId = articleService.create(ArticleCreateOrUpdateRequestDto("email@ursuu.com", "password", "title", "content")).articleId
        val request = CommentCreateOrUpdateRequestDto("email@ursuu.com", "password", "content")

        // when
        val result = mvc.perform(
            MockMvcRequestBuilders
                .post("/api/v1/comment/create/$articleId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )

        // then
        result
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.email").value("email@ursuu.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.content").value("content"))
    }

    @Test
    @Transactional
    fun `comment update api`() {
        // given
        userService.create(UserCreateRequestDto("email@ursuu.com", "password", "username"))
        val articleId = articleService.create(ArticleCreateOrUpdateRequestDto("email@ursuu.com", "password", "title", "content")).articleId
        val commentId = commentService.create(CommentCreateOrUpdateRequestDto("email@ursuu.com", "password", "content"), articleId).commentId
        val request = CommentCreateOrUpdateRequestDto("email@ursuu.com", "password", "content2")

        // when
        val result = mvc.perform(
            MockMvcRequestBuilders
                .put("/api/v1/comment/update/$articleId/$commentId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )

        // then
        result
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.commentId").value(commentId))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.email").value("email@ursuu.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.content").value("content2"))
    }

    @Test
    @Transactional
    fun `comment delete api`() {
        // given
        userService.create(UserCreateRequestDto("email@ursuu.com", "password", "username"))
        val articleId = articleService.create(ArticleCreateOrUpdateRequestDto("email@ursuu.com", "password", "title", "content")).articleId
        val commentId = commentService.create(CommentCreateOrUpdateRequestDto("email@ursuu.com", "password", "content"), articleId).commentId
        val request = CommentDeleteRequestDto("email@ursuu.com", "password")

        // when
        val result = mvc.perform(
            MockMvcRequestBuilders
                .delete("/api/v1/comment/delete/$articleId/$commentId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )

        // then
        result
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

}
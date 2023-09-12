package com.rangjin.kotlinblog.domain.article.application

import com.rangjin.kotlinblog.domain.article.dao.ArticleRepository
import com.rangjin.kotlinblog.domain.article.domain.Article
import com.rangjin.kotlinblog.domain.article.dto.request.ArticleCreateOrUpdateRequestDto
import com.rangjin.kotlinblog.domain.article.dto.request.ArticleDeleteRequestDto
import com.rangjin.kotlinblog.domain.user.dao.UserRepository
import com.rangjin.kotlinblog.domain.user.domain.User
import com.rangjin.kotlinblog.global.common.DataSweepExtension
import com.rangjin.kotlinblog.global.error.CustomException
import com.rangjin.kotlinblog.global.error.ErrorCode
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder

@SpringBootTest
@ExtendWith(DataSweepExtension::class)
class ArticleServiceTest @Autowired constructor(

    private val articleService: ArticleService,

    private val articleRepository: ArticleRepository,

    private val userRepository: UserRepository,

    private val passwordEncoder: PasswordEncoder,

){

    @Test
    fun `create article`() {
        // given
        userRepository.save(User(1L, "email@ursuu.com", passwordEncoder.encode("password"), "username", emptyList(), emptyList()))

        // when
        val response = articleService.create(ArticleCreateOrUpdateRequestDto("email@ursuu.com", "password", "title", "content"))
        val article: Article = articleRepository.findById(response.articleId).get()

        // then
        assertEquals("title", article.title)
        assertEquals("content", article.content)
        assertEquals(1L, article.user.id)
    }

    @Test
    fun `update article`() {
        // given
        val user = User(1L, "email@ursuu.com", passwordEncoder.encode("password"), "username", emptyList(), emptyList())
        userRepository.save(user)
        articleRepository.save(Article(1L, "content", "title", user, emptyList()))

        // when
        val response = articleService.update(ArticleCreateOrUpdateRequestDto("email@ursuu.com", "password", "title2", "content2"), 1L)
        val article: Article = articleRepository.findById(response.articleId).get()

        // then
        assertEquals(article.id, 1L)
        assertEquals(article.title, "title2")
        assertEquals(article.content, "content2")
        assertEquals(article.user.id, 1L)
    }

    @Test
    fun `article not found exception`() {
        // given
        userRepository.save(User(1L, "email@ursuu.com", passwordEncoder.encode("password"), "username", emptyList(), emptyList()))

        // when & then
        val exception: CustomException = assertThrows(CustomException::class.java) {
            articleService.update(ArticleCreateOrUpdateRequestDto("email@ursuu.com", "password", "title2", "content2"), 1L)
        }
        assertEquals(exception.errorCode.status, ErrorCode.ARTICLE_NOT_FOUND.status)
        assertEquals(exception.errorCode.message, ErrorCode.ARTICLE_NOT_FOUND.message)
    }

    @Test
    fun `delete article`() {
        // given
        val user = User(1L, "email@ursuu.com", passwordEncoder.encode("password"), "username", emptyList(), emptyList())
        userRepository.save(user)
        articleRepository.save(Article(1L, "content", "title", user, emptyList()))

        // when
        articleService.delete(ArticleDeleteRequestDto("email@ursuu.com", "password"), 1L)

        // then
        assertNull(articleRepository.findByIdOrNull(1L))
    }

}
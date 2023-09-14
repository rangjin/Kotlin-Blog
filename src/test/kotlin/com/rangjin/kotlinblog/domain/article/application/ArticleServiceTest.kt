package com.rangjin.kotlinblog.domain.article.application

import com.rangjin.kotlinblog.domain.article.dao.ArticleRepository
import com.rangjin.kotlinblog.domain.article.domain.Article
import com.rangjin.kotlinblog.domain.article.dto.request.ArticleCreateOrUpdateRequestDto
import com.rangjin.kotlinblog.domain.article.dto.request.ArticleDeleteRequestDto
import com.rangjin.kotlinblog.domain.user.dao.UserRepository
import com.rangjin.kotlinblog.domain.user.domain.User
import com.rangjin.kotlinblog.global.error.CustomException
import com.rangjin.kotlinblog.global.error.ErrorCode
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class ArticleServiceTest @Autowired constructor(

    private val articleService: ArticleService,

    private val articleRepository: ArticleRepository,

    private val userRepository: UserRepository,

    private val passwordEncoder: PasswordEncoder,

){

    @Test
    fun `create article`() {
        // given
        val userId = userRepository.save(User(null, "email@ursuu.com", passwordEncoder.encode("password"), "username", emptyList(), emptyList())).id!!

        // when
        val response = articleService.create(ArticleCreateOrUpdateRequestDto("email@ursuu.com", "password", "title", "content"))
        val article: Article = articleRepository.findById(response.articleId).get()

        // then
        assertEquals("content", article.content)
        assertEquals("title", article.title)
        assertEquals(userId, article.user.id)
    }

    @Test
    fun `update article`() {
        // given
        val user = userRepository.save(User(null, "email@ursuu.com", passwordEncoder.encode("password"), "username", emptyList(), emptyList()))
        val articleId = articleRepository.save(Article(null, "content", "title", user, emptyList())).id

        // when
        articleService.update(ArticleCreateOrUpdateRequestDto("email@ursuu.com", "password", "title2", "content2"), articleId!!)
        val article = articleRepository.findById(articleId).get()

        // then
        assertEquals(articleId, article.id)
        assertEquals("content2", article.content)
        assertEquals("title2", article.title)
        assertEquals(user.id, article.user.id)
    }

    @Test
    fun `article not found exception`() {
        // given
        val userId = userRepository.save(User(null, "email@ursuu.com", passwordEncoder.encode("password"), "username", emptyList(), emptyList())).id!!

        // when & then
        val exception: CustomException = assertThrows(CustomException::class.java) {
            articleService.update(ArticleCreateOrUpdateRequestDto("email@ursuu.com", "password", "title2", "content2"), userId)
        }
        assertEquals(exception.errorCode.status, ErrorCode.ARTICLE_NOT_FOUND.status)
        assertEquals(exception.errorCode.message, ErrorCode.ARTICLE_NOT_FOUND.message)
    }

    @Test
    fun `delete article`() {
        // given
        val user = userRepository.save(User(null, "email@ursuu.com", passwordEncoder.encode("password"), "username", emptyList(), emptyList()))
        val articleId = articleRepository.save(Article(null, "content", "title", user, emptyList())).id!!

        // when
        articleService.delete(ArticleDeleteRequestDto("email@ursuu.com", "password"), articleId)

        // then
        assertNull(articleRepository.findByIdOrNull(articleId))
    }

}
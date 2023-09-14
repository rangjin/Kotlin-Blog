package com.rangjin.kotlinblog.domain.comment.application

import com.rangjin.kotlinblog.domain.article.dao.ArticleRepository
import com.rangjin.kotlinblog.domain.article.domain.Article
import com.rangjin.kotlinblog.domain.comment.dao.CommentRepository
import com.rangjin.kotlinblog.domain.comment.domain.Comment
import com.rangjin.kotlinblog.domain.comment.dto.request.CommentCreateOrUpdateRequestDto
import com.rangjin.kotlinblog.domain.comment.dto.request.CommentDeleteRequestDto
import com.rangjin.kotlinblog.domain.user.dao.UserRepository
import com.rangjin.kotlinblog.domain.user.domain.User
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
class CommentServiceTest @Autowired constructor(

    private val commentService: CommentService,

    private val commentRepository: CommentRepository,

    private val articleRepository: ArticleRepository,

    private val userRepository: UserRepository,

    private val passwordEncoder: PasswordEncoder,

) {

    @Test
    fun `create comment`() {
        // given
        val user = userRepository.save(User(null, "email@ursuu.com", passwordEncoder.encode("password"), "username", emptyList(), emptyList()))
        val articleId = articleRepository.save(Article(null, "content", "title", user, emptyList())).id!!

        // when
        val response = commentService.create(CommentCreateOrUpdateRequestDto("email@ursuu.com", "password", "content"), articleId)
        val comment: Comment = commentRepository.findById(response.commentId).get()

        // then
        assertEquals("content", comment.content)
        assertEquals(articleId, comment.article.id)
        assertEquals(user.id!!, comment.user.id)
    }

    @Test
    fun `update comment`() {
        // given
        val user = userRepository.save(User(null, "email@ursuu.com", passwordEncoder.encode("password"), "username", emptyList(), emptyList()))
        val article = articleRepository.save(Article(null, "content", "title", user, emptyList()))
        val commentId = commentRepository.save(Comment(null, "content", article, user)).id!!

        // when
        commentService.update(CommentCreateOrUpdateRequestDto("email@ursuu.com", "password", "content2"), article.id!!, commentId)
        val comment = commentRepository.findById(commentId).get()

        // then
        assertEquals(commentId, comment.id)
        assertEquals("content2", comment.content)
        assertEquals(article.id, comment.article.id)
        assertEquals(user.id, comment.user.id)
    }

    @Test
    fun `delete comment`() {
        // given
        val user = userRepository.save(User(null, "email@ursuu.com", passwordEncoder.encode("password"), "username", emptyList(), emptyList()))
        val article = articleRepository.save(Article(null, "content", "title", user, emptyList()))
        val commentId = commentRepository.save(Comment(null, "content", article, user)).id!!

        // when
        commentService.delete(CommentDeleteRequestDto("email@ursuu.com", "password"), article.id!!, commentId)

        // then
        assertNull(commentRepository.findByIdOrNull(commentId))
    }

}
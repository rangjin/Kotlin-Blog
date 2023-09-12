package com.rangjin.kotlinblog.domain.comment.application

import com.rangjin.kotlinblog.domain.article.dao.ArticleRepository
import com.rangjin.kotlinblog.domain.article.domain.Article
import com.rangjin.kotlinblog.domain.comment.dao.CommentRepository
import com.rangjin.kotlinblog.domain.comment.domain.Comment
import com.rangjin.kotlinblog.domain.comment.dto.request.CommentCreateOrUpdateRequestDto
import com.rangjin.kotlinblog.domain.comment.dto.request.CommentDeleteRequestDto
import com.rangjin.kotlinblog.domain.user.dao.UserRepository
import com.rangjin.kotlinblog.domain.user.domain.User
import com.rangjin.kotlinblog.global.common.DataSweepExtension
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder

@SpringBootTest
@ExtendWith(DataSweepExtension::class)
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
        val user = User(1L, "email@ursuu.com", passwordEncoder.encode("password"), "username", emptyList(), emptyList())
        userRepository.save(user)
        articleRepository.save(Article(1L, "content", "title", user, emptyList()))

        // when
        val response = commentService.create(CommentCreateOrUpdateRequestDto("email@ursuu.com", "password", "content"), 1L)
        val comment: Comment = commentRepository.findById(response.commentId).get()

        // then
        assertEquals("content", comment.content)
        assertEquals(1L, comment.user.id)
        assertEquals(1L, comment.article.id)
    }

    @Test
    fun `update comment`() {
        // given
        val user = User(1L, "email@ursuu.com", passwordEncoder.encode("password"), "username", emptyList(), emptyList())
        userRepository.save(user)
        val article = Article(1L, "content", "title", user, emptyList())
        articleRepository.save(article)
        commentRepository.save(Comment(2L, "content", article, user))

        // when
        val response = commentService.update(CommentCreateOrUpdateRequestDto("email@ursuu.com", "password", "content2"), 1L, 2L)
        val comment: Comment = commentRepository.findById(response.commentId).get()

        // then
        assertEquals(2L, comment.id)
        assertEquals("content2", comment.content)
        assertEquals(1L, comment.user.id)
        assertEquals(1L, comment.article.id)
    }

    @Test
    fun `delete comment`() {
        // given
        val user = User(1L, "email@ursuu.com", passwordEncoder.encode("password"), "username", emptyList(), emptyList())
        userRepository.save(user)
        val article = Article(1L, "content", "title", user, emptyList())
        articleRepository.save(article)
        commentRepository.save(Comment(1L, "content", article, user))

        // when
        commentService.delete(CommentDeleteRequestDto("email@ursuu.com", "password"), 1L, 1L)

        // then
        assertNull(commentRepository.findByIdOrNull(1L))
    }

}
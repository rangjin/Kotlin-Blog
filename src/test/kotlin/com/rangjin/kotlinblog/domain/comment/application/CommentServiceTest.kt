package com.rangjin.kotlinblog.domain.comment.application

import com.rangjin.kotlinblog.domain.article.dao.ArticleRepository
import com.rangjin.kotlinblog.domain.article.domain.Article
import com.rangjin.kotlinblog.domain.comment.dao.CommentRepository
import com.rangjin.kotlinblog.domain.comment.domain.Comment
import com.rangjin.kotlinblog.domain.comment.dto.request.CommentCreateOrUpdateRequestDto
import com.rangjin.kotlinblog.domain.comment.dto.request.CommentDeleteRequestDto
import com.rangjin.kotlinblog.domain.user.dao.UserRepository
import com.rangjin.kotlinblog.domain.user.domain.User
import com.rangjin.kotlinblog.global.error.CustomException
import com.rangjin.kotlinblog.global.error.ErrorCode
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
class CommentServiceTest @Autowired constructor(

    private val commentService: CommentService,

    private val commentRepository: CommentRepository,

    private val articleRepository: ArticleRepository,

    private val userRepository: UserRepository,

    private val passwordEncoder: PasswordEncoder,

) {

    @Test
    @Transactional
    fun `create comment`() {
        // given
        val user = User(1L, "email@ursuu.com", passwordEncoder.encode("password"), "username", emptyList(), emptyList())
        userRepository.save(user)
        articleRepository.save(Article(1L, "content", "title", user, emptyList()))

        // when
        commentService.create(CommentCreateOrUpdateRequestDto("email@ursuu.com", "password", "content"), 1L)

        // then
        val comment: Comment? = commentRepository.findByIdOrNull(1L)
        assertNotNull(comment)
        assertEquals(comment!!.id, 1L)
        assertEquals(comment.content, "content")
        assertEquals(comment.user.id, 1L)
        assertEquals(comment.article.id, 1L)
    }

    @Test
    @Transactional
    fun `update comment`() {
        // given
        val user = User(1, "email@ursuu.com", passwordEncoder.encode("password"), "username", emptyList(), emptyList())
        userRepository.save(user)
        val article = Article(1, "content", "title", user, emptyList())
        articleRepository.save(article)
        commentRepository.save(Comment(1, "content", article, user))

        // when
        commentService.update(CommentCreateOrUpdateRequestDto("email@ursuu.com", "password", "content2"), 1L, 1L)

        // then
        val comment: Comment? = commentRepository.findByIdOrNull(1L)
        assertNotNull(comment)
        assertEquals(comment!!.id, 1L)
        assertEquals(comment.content, "content2")
        assertEquals(comment.user.id, 1L)
        assertEquals(comment.article.id, 1L)
    }

    @Test
    @Transactional
    fun `comment not found exception`() {
        // given
        val user = User(1, "email@ursuu.com", passwordEncoder.encode("password"), "username", emptyList(), emptyList())
        userRepository.save(user)
        articleRepository.save(Article(1, "content", "title", user, emptyList()))

        // when & then
        val exception: CustomException = assertThrows(CustomException::class.java) {
            commentService.update(CommentCreateOrUpdateRequestDto("email@ursuu.com", "password", "content2"), 1L, 1L)
        }
        assertEquals(exception.errorCode.status, ErrorCode.COMMENT_NOT_FOUND.status)
        assertEquals(exception.errorCode.message, ErrorCode.COMMENT_NOT_FOUND.message)
    }

    @Test
    @Transactional
    fun `delete comment`() {
        // given
        val user = User(1, "email@ursuu.com", passwordEncoder.encode("password"), "username", emptyList(), emptyList())
        userRepository.save(user)
        val article = Article(1, "content", "title", user, emptyList())
        articleRepository.save(article)
        commentRepository.save(Comment(1, "content", article, user))

        // when
        commentService.delete(CommentDeleteRequestDto("email@ursuu.com", "password"), 1L, 1L)

        // then
        assertNull(commentRepository.findByIdOrNull(1L))
    }

}
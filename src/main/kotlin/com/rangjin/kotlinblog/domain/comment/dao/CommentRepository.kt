package com.rangjin.kotlinblog.domain.comment.dao

import com.rangjin.kotlinblog.domain.comment.domain.Comment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository: JpaRepository<Comment, Long>
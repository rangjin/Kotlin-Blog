package com.rangjin.kotlinblog.domain.comment.domain

import com.rangjin.kotlinblog.domain.article.domain.Article
import com.rangjin.kotlinblog.domain.comment.dto.request.CommentCreateOrUpdateRequestDto
import com.rangjin.kotlinblog.domain.user.domain.User
import com.rangjin.kotlinblog.global.common.BaseTimeEntity
import javax.persistence.*

@Entity
class Comment (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    var content: String,

    @ManyToOne
    val article: Article,

    @ManyToOne
    val user: User,

): BaseTimeEntity()  {

    constructor(requestDto: CommentCreateOrUpdateRequestDto, article: Article, user: User): this(
        null, requestDto.content!!, article, user
    )

    fun update(requestDto: CommentCreateOrUpdateRequestDto) {
        this.content = requestDto.content!!
    }

}
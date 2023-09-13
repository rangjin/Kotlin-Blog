package com.rangjin.kotlinblog.domain.comment.domain

import com.rangjin.kotlinblog.domain.article.domain.Article
import com.rangjin.kotlinblog.domain.comment.dto.request.CommentCreateOrUpdateRequestDto
import com.rangjin.kotlinblog.domain.user.domain.User
import com.rangjin.kotlinblog.global.common.BaseTimeEntity
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
class Comment (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @field:NotBlank(message = "내용은 빈 칸일 수 없습니다")
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
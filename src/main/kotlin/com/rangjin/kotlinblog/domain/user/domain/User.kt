package com.rangjin.kotlinblog.domain.user.domain

import com.rangjin.kotlinblog.domain.article.domain.Article
import com.rangjin.kotlinblog.domain.comment.domain.Comment
import com.rangjin.kotlinblog.domain.user.dto.request.UserCreateRequestDto
import com.rangjin.kotlinblog.global.common.BaseTimeEntity
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
class User (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val email: String,

    val password: String,

    val username: String,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.REMOVE])
    val articleList: List<Article>,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.REMOVE])
    val commentList: List<Comment>,

): BaseTimeEntity() {

    constructor(requestDto: UserCreateRequestDto, password: String): this(
        null, requestDto.email!!, password, requestDto.username!!, emptyList(), emptyList(),
    )

}
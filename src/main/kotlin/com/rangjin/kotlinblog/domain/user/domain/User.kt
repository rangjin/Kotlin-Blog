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
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

@Entity
class User (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @field:NotBlank(message = "이메일은 빈 칸일 수 없습니다")
    @field:Email(message = "이메일 형식의 입력이어야 합니다")
    val email: String,

    @field:NotBlank(message = "비밀번호는 빈 칸일 수 없습니다")
    val password: String,

    @field:NotBlank(message = "이름은 빈 칸일 수 없습니다")
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
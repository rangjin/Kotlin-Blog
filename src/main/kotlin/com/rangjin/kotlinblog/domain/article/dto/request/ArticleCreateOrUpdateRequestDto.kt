package com.rangjin.kotlinblog.domain.article.dto.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

class ArticleCreateOrUpdateRequestDto (

    @field:NotBlank(message = "이메일은 빈 칸일 수 없습니다")
    @field:Email(message = "이메일 형식의 입력이어야 합니다")
    val email: String?,

    @field:NotBlank(message = "비밀번호는 빈 칸일 수 없습니다")
    val password: String?,

    @field:NotBlank(message = "제목은 빈 칸일 수 없습니다")
    val title: String?,

    @field:NotBlank(message = "내용은 빈 칸일 수 없습니다")
    val content: String?,

)
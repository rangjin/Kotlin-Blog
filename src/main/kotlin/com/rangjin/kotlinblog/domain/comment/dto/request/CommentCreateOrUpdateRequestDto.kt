package com.rangjin.kotlinblog.domain.comment.dto.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

class CommentCreateOrUpdateRequestDto (

    @field:NotBlank(message = "이메일은 빈 칸일 수 없습니다")
    @field:Email(message = "이메일 형식의 입력이어야 합니다")
    val email: String?,

    @field:NotBlank(message = "비밀번호는 빈 칸일 수 없습니다")
    val password: String?,

    @field:NotBlank(message = "내용은 빈 칸일 수 없습니다")
    val content: String?,

)
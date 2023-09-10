package com.rangjin.kotlinblog.domain.comment.dto.request

class CommentCreateOrUpdateRequestDto (
    val email: String,

    val password: String,

    val content: String,
) {
}
package com.rangjin.kotlinblog.domain.comment.dto.response

import com.rangjin.kotlinblog.domain.comment.domain.Comment

class CommentCreateOrUpdateResponseDto(

    val commentId: Long,

    val email: String,

    val content: String,

) {

    constructor(comment: Comment, email: String): this(comment.id!!, email, comment.content)

}
package com.rangjin.kotlinblog.domain.user.dto.response

import com.rangjin.kotlinblog.domain.user.domain.User

class UserCreateResponseDto (

    val email: String,

    val username: String,

) {

    constructor(user: User): this(user.email, user.username)

}
package com.rangjin.kotlinblog.domain.user.api

import com.rangjin.kotlinblog.domain.user.application.UserService
import com.rangjin.kotlinblog.domain.user.dto.request.UserCreateRequestDto
import com.rangjin.kotlinblog.domain.user.dto.request.UserDeleteRequestDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/user")
class UserApiController (
    private val userService: UserService,
) {

    @PostMapping("/signup")
    fun signup(@RequestBody request: UserCreateRequestDto): ResponseEntity<Any> {
        return ResponseEntity.ok().body(userService.create(request))
    }

    @DeleteMapping("/delete")
    fun delete(@RequestBody request: UserDeleteRequestDto): ResponseEntity<Any> {
        userService.delete(request)
        return ResponseEntity.ok().build()
    }

}
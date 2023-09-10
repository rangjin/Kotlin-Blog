package com.rangjin.kotlinblog.domain.user.application

import com.rangjin.kotlinblog.domain.user.dao.UserRepository
import com.rangjin.kotlinblog.domain.user.domain.User
import com.rangjin.kotlinblog.domain.user.dto.request.UserCreateRequestDto
import com.rangjin.kotlinblog.domain.user.dto.request.UserDeleteRequestDto
import com.rangjin.kotlinblog.domain.user.dto.response.UserCreateResponseDto
import org.springframework.stereotype.Service
import java.lang.Exception

@Service
class UserService (
    private val userRepository: UserRepository,
){

    fun create(requestDto: UserCreateRequestDto): UserCreateResponseDto {
        // todo: 중복 email 예외 처리
        // todo: password 암호화
        val user = User(requestDto)
        return UserCreateResponseDto(userRepository.save(user))
    }

    fun delete(requestDto: UserDeleteRequestDto) {
        // todo: email, password 확인 절차 추가
        val user = userRepository.findByEmail(requestDto.email) ?: throw Exception("error")

        userRepository.delete(user)
    }

}
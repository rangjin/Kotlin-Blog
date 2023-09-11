package com.rangjin.kotlinblog.domain.user.application

import com.rangjin.kotlinblog.domain.user.dao.UserRepository
import com.rangjin.kotlinblog.domain.user.domain.User
import com.rangjin.kotlinblog.domain.user.dto.request.UserCreateRequestDto
import com.rangjin.kotlinblog.domain.user.dto.request.UserDeleteRequestDto
import com.rangjin.kotlinblog.domain.user.dto.response.UserCreateResponseDto
import com.rangjin.kotlinblog.global.error.CustomException
import com.rangjin.kotlinblog.global.error.ErrorCode
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService (
    private val userRepository: UserRepository,

    private val passwordEncoder: PasswordEncoder,
){

    fun create(requestDto: UserCreateRequestDto): UserCreateResponseDto {
        if (userRepository.findByEmail(requestDto.email!!) != null) throw CustomException(ErrorCode.EMAIL_ALREADY_EXIST)

        // todo: password 암호화
        val encoded: String = passwordEncoder.encode(requestDto.password)
        return UserCreateResponseDto(userRepository.save(User(requestDto, encoded)))
    }

    fun delete(requestDto: UserDeleteRequestDto) {
        val user = userRepository.findByEmail(requestDto.email!!) ?: throw CustomException(ErrorCode.EMAIL_NOT_FOUND)
        if (user.password != requestDto.password) throw CustomException(ErrorCode.PASSWORD_MISMATCH)

        userRepository.delete(user)
    }

    fun validateUser(user: User, email: String, password: String) {
        if (user.email != email) {
            throw CustomException(ErrorCode.EMAIL_MISMATCH)
        } else if (!passwordEncoder.matches(password, user.password)) {
            throw CustomException(ErrorCode.PASSWORD_MISMATCH)
        }
    }

}
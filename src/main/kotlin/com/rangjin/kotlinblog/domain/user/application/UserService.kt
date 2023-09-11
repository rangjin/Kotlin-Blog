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
        // 이메일 중복 처리
        if (userRepository.findByEmail(requestDto.email!!) != null) throw CustomException(ErrorCode.EMAIL_ALREADY_EXIST)

        // 비밀번호 암호화
        val encoded: String = passwordEncoder.encode(requestDto.password)

        return UserCreateResponseDto(userRepository.save(User(requestDto, encoded)))
    }

    fun delete(requestDto: UserDeleteRequestDto) {
        // 이메일, 비밀번호 검증
        val user = userRepository.findByEmail(requestDto.email!!) ?: throw CustomException(ErrorCode.EMAIL_NOT_FOUND)
        if (!passwordEncoder.matches(requestDto.password, user.password)) throw CustomException(ErrorCode.PASSWORD_MISMATCH)

        userRepository.delete(user)
    }

    // Article, Comment에서 사용할 User 검증 로직
    fun validateUser(obj: User?, email: String, password: String): User {
        // 존재하지 않는 이메일 처리
        val user: User = obj ?: (userRepository.findByEmail(email) ?: throw CustomException(ErrorCode.EMAIL_NOT_FOUND))

        if (user.email != email) {
            // 해당 글, 댓글을 작성한 이메일이 아닐 경우
            throw CustomException(ErrorCode.EMAIL_MISMATCH)
        } else if (!passwordEncoder.matches(password, user.password)) {
            // 비밀번호가 일치하지 않는 경우
            throw CustomException(ErrorCode.PASSWORD_MISMATCH)
        }

        return user
    }

}
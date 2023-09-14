package com.rangjin.kotlinblog.domain.user.application

import com.rangjin.kotlinblog.domain.user.dao.UserRepository
import com.rangjin.kotlinblog.domain.user.domain.User
import com.rangjin.kotlinblog.domain.user.dto.request.UserCreateRequestDto
import com.rangjin.kotlinblog.domain.user.dto.request.UserDeleteRequestDto
import com.rangjin.kotlinblog.global.error.CustomException
import com.rangjin.kotlinblog.global.error.ErrorCode
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class UserServiceTest @Autowired constructor(

    private val userService: UserService,

    private val userRepository: UserRepository,

    private val passwordEncoder: PasswordEncoder,

) {

    @Test
    fun `create user`() {
        // given & when
        userService.create(UserCreateRequestDto("email@ursuu.com", "password", "username"))
        val user: User? = userRepository.findByEmail("email@ursuu.com")

        // then
        assertNotNull(user)
        assertEquals(user!!.email, "email@ursuu.com")
        assertTrue(passwordEncoder.matches("password", user.password))
        assertEquals(user.username, "username")
    }

    @Test
    fun `email already exist exception`() {
        // given
        userRepository.save(User(null, "email@ursuu.com", passwordEncoder.encode("password"), "username", emptyList(), emptyList()))

        // when & then
        val exception: CustomException = assertThrows(CustomException::class.java) {
            userService.create(UserCreateRequestDto("email@ursuu.com", "password", "username"))
        }
        assertEquals(exception.errorCode.status, ErrorCode.EMAIL_ALREADY_EXIST.status)
        assertEquals(exception.errorCode.message, ErrorCode.EMAIL_ALREADY_EXIST.message)
    }

    @Test
    fun `delete user`() {
        // given
        val user = userRepository.save(User(null, "email@ursuu.com", passwordEncoder.encode("password"), "username", emptyList(), emptyList()))

        // when
        userService.delete(UserDeleteRequestDto("email@ursuu.com", "password"))

        // then
        assertNull(userRepository.findByIdOrNull(user.id))
    }

    @Test
    fun `validate user`() {
        // given
        val user = userRepository.save(User(null, "email@ursuu.com", passwordEncoder.encode("password"), "username", emptyList(), emptyList()))

        // when
        val obj = userService.validateUser(user, "email@ursuu.com", "password")

        // then
        assertEquals(obj.id, user.id)
        assertEquals(obj.email, user.email)
        assertEquals(obj.password, user.password)
        assertEquals(obj.username, user.username)
    }

    @Test
    fun `email not found exception`() {
        // given & when & then
        val exception: CustomException = assertThrows(CustomException::class.java) {
            userService.validateUser(null, "email@ursuu.com", "password")
        }
        assertEquals(ErrorCode.EMAIL_NOT_FOUND.status, exception.errorCode.status)
        assertEquals(ErrorCode.EMAIL_NOT_FOUND.message, exception.errorCode.message)
    }

    @Test
    fun `email mismatch exception`() {
        // given
        val user = userRepository.save(User(null, "email@ursuu.com", passwordEncoder.encode("password"), "username", emptyList(), emptyList()))

        // when & then
        val exception: CustomException = assertThrows(CustomException::class.java) {
            userService.validateUser(user, "email2@ursuu.com", "password")
        }
        assertEquals(ErrorCode.EMAIL_MISMATCH.status, exception.errorCode.status)
        assertEquals(ErrorCode.EMAIL_MISMATCH.message, exception.errorCode.message)
    }

    @Test
    fun `password mismatch exception`() {
        // given
        val user = userRepository.save(User(null, "email@ursuu.com", passwordEncoder.encode("password"), "username", emptyList(), emptyList()))

        // when & then
        val exception: CustomException = assertThrows(CustomException::class.java) {
            userService.validateUser(user, "email@ursuu.com", "password2")
        }
        assertEquals(ErrorCode.PASSWORD_MISMATCH.status, exception.errorCode.status)
        assertEquals(ErrorCode.PASSWORD_MISMATCH.message, exception.errorCode.message)
    }

}
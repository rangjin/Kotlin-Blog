package com.rangjin.kotlinblog.domain.user.dao

import com.rangjin.kotlinblog.domain.user.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<User, Long> {

    fun findByEmail(email: String): User?

}
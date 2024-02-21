package com.mvp.order.domain.service.auth

import com.mvp.order.domain.model.auth.LoginAttempt
import com.mvp.order.infrastruture.repository.auth.LoginAttemptRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class LoginService @Autowired constructor(
    private val repository: LoginAttemptRepository
) {

    @Transactional
    fun addLoginAttempt(email: String, success: Boolean) {
        val loginAttempt = LoginAttempt(email, success, LocalDateTime.now())
        repository.save(loginAttempt.toEntity())
    }

    fun findRecentLoginAttempts(email: String): List<LoginAttempt> {
        val loginAttemptEntityList = repository.findRecent(email)

        return loginAttemptEntityList.flatMap { listOf(it.toDTO()) }
    }
}
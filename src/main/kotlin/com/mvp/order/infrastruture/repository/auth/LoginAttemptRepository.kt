package com.mvp.order.infrastruture.repository.auth

import com.mvp.order.infrastruture.entity.auth.LoginAttemptEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface LoginAttemptRepository : JpaRepository<LoginAttemptEntity, Long> {
    @Query(""" 
        SELECT email, success, created_at
        FROM tb_login_attempt
        WHERE email = :email 
        ORDER BY created_at DESC LIMIT 10
    """, nativeQuery = true)
    fun findRecent(email: String): List<LoginAttemptEntity>
}
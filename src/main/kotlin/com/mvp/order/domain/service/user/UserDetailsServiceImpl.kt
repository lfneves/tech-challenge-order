package com.mvp.order.domain.service.user

import com.mvp.order.domain.model.auth.AuthClientDTO
import com.mvp.order.domain.model.exception.Exceptions
import com.mvp.order.infrastruture.entity.user.UserEntity
import com.mvp.order.infrastruture.repository.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service


@Service
class UserDetailsServiceImpl @Autowired constructor(
    private val repository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val userEntity = repository.findByCpf(username)
            .orElseGet {
                repository.findByEmail(username).orElseThrow {
                    UsernameNotFoundException(
                        "User does not exist with username (CPF or email): $username"
                    )
                }
            }
        return AuthClientDTO(
            userEntity.name ?: throw IllegalStateException("Name is required"),
            userEntity.email ?: throw IllegalStateException("Email is required"),
            userEntity.cpf ?: throw IllegalStateException("Email is required"),
            userEntity.password ?: throw IllegalStateException("Password is required")
        )
    }
}
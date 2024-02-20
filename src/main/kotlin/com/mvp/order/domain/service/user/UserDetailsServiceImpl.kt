package com.mvp.order.domain.service.user

import com.mvp.order.domain.model.auth.AuthClientDTO
import com.mvp.order.domain.model.exception.Exceptions
import com.mvp.order.infrastruture.entity.user.UserEntity
import com.mvp.order.infrastruture.repository.user.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service


@Service
class UserDetailsServiceImpl(private val repository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails? {
        val user: UserEntity = repository.findByEmail(email).orElseThrow {
            Exceptions.NotFoundException(
                String.format("User does not exist, email: %s", email)
            )
        }

        return AuthClientDTO(
            user.name!!,
            user.email!!,
            user.password!!
        )
    }
}
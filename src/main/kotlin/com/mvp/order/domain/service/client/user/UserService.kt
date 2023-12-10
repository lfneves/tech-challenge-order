package com.mvp.order.domain.service.client.user

import com.mvp.order.domain.model.user.UserDTO
import com.mvp.delivery.domain.model.user.UsernameDTO
import org.springframework.security.core.Authentication
import reactor.core.publisher.Mono

interface UserService {
    fun getUserById(id: Int): Mono<UserDTO>

    fun getByUsername(usernameDTO: UsernameDTO): Mono<UserDTO>

    fun saveUser(user: UserDTO): Mono<UserDTO>

    fun signup(user: UserDTO): Mono<UserDTO>

    fun updateUser(id: Int, userDTO: UserDTO): Mono<UserDTO>

    fun deleteUserById(id: Int): Mono<Void>
}
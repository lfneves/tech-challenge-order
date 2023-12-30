package com.mvp.order.domain.service.user

import com.mvp.order.domain.model.user.UserDTO

fun interface UserService {

    fun getByUsername(username: String): UserDTO
}
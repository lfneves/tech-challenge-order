package com.mvp.order.domain.service.auth

import com.mvp.order.domain.model.auth.ResponseSignupDTO
import com.mvp.order.domain.model.user.UserDTO

fun interface AuthService {

    fun signup(user: UserDTO): ResponseSignupDTO
}
package com.mvp.order.domain.service.admin.user

import com.mvp.order.domain.model.user.UserDTO

interface UserAdminService {

    fun getUsers(): List<UserDTO>

    fun deleteById(id: Long)

    fun deleteAllUsers()
}
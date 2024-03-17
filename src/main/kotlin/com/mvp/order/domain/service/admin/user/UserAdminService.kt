package com.mvp.order.domain.service.admin.user

import com.mvp.order.domain.model.auth.RemoveUserDTO
import com.mvp.order.domain.model.user.admin.UserAdminResponseDTO

interface UserAdminService {

    fun getUsers(): List<UserAdminResponseDTO>

    fun deleteByUsername(removeUserDTO: RemoveUserDTO)

    fun deleteAllUsers()
}
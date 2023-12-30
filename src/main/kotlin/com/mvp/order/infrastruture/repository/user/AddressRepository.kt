package com.mvp.order.infrastruture.repository.user

import com.mvp.order.infrastruture.entity.user.AddressEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AddressRepository : JpaRepository<AddressEntity, Long>
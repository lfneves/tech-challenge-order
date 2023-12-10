package com.mvp.order.infrastruture.repository.user

import com.mvp.order.infrastruture.entity.user.AddressEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AddressRepository : ReactiveCrudRepository<AddressEntity?, Long?>
package com.mvp.order.domain.service.admin.order

import com.mvp.order.domain.model.order.OrderDTO
import com.mvp.order.domain.model.order.OrderFinishDTO
import com.mvp.order.domain.model.order.OrderStatusDTO
import com.mvp.order.domain.model.order.enums.OrderStatusEnum
import com.mvp.order.domain.model.exception.Exceptions
import com.mvp.order.infrastruture.repository.order.OrderRepository
import com.mvp.order.utils.constants.ErrorMsgConstants
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class OrderAdminServiceImpl(
    private val orderRepository: OrderRepository
): OrderAdminService {
    var logger: Logger = LoggerFactory.getLogger(OrderAdminServiceImpl::class.java)

    override fun updateOrderStatus(id: Long, orderStatusDTO: OrderStatusDTO, authentication: Authentication): Mono<OrderDTO> {
        return orderRepository.findByIdOrder(id)
            .switchIfEmpty(Mono.error(Exceptions.NotFoundException(ErrorMsgConstants.ERROR_ORDER_NOT_FOUND)))
            .doOnNext { setStatus ->
                setStatus.status = OrderStatusEnum.valueOf(orderStatusDTO.status).value
            }.onErrorMap { Exceptions.BadStatusException(ErrorMsgConstants.ERROR_ORDER_ERROR) }
            .flatMap(orderRepository::save)
                .map { return@map it.toDTO() }
    }

    override fun updateOrderFinishedAndStatus(orderFinishDTO: OrderFinishDTO, authentication: Authentication): Mono<OrderDTO> {
        return orderRepository.findByIdOrder(orderFinishDTO.idOrder)
            .switchIfEmpty(Mono.error(Exceptions.NotFoundException(ErrorMsgConstants.ERROR_ORDER_NOT_FOUND)))
            .doOnNext { orderFinished ->
                orderFinished.status = OrderStatusEnum.FINISHED.value
                orderFinished.isFinished = true
            }.onErrorMap { Exceptions.BadStatusException(ErrorMsgConstants.ERROR_ORDER_ERROR) }
            .flatMap(orderRepository::save)
            .map { return@map it.toDTO() }
    }

    override fun getOrders(): Flux<OrderDTO> {
        return orderRepository
            .findAllOrder()
            .map{ it?.toDTO() }
    }

    override fun deleteAllOrders(): Mono<Void> {
         return orderRepository
             .deleteAll()
    }
}
package com.mvp.order.domain.service.admin.order

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.mvp.order.domain.model.exception.Exceptions
import com.mvp.order.domain.model.order.OrderDTO
import com.mvp.order.domain.model.order.OrderFinishDTO
import com.mvp.order.domain.model.order.OrderResponseDTO
import com.mvp.order.domain.model.order.OrderStatusDTO
import com.mvp.order.domain.model.order.enums.OrderStatusEnum
import com.mvp.order.domain.service.message.SnsAndSqsService
import com.mvp.order.infrastruture.repository.order.OrderRepository
import com.mvp.order.utils.constants.ErrorMsgConstants
import com.mvp.order.domain.model.status.enums.PaymentStatusEnum
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderAdminServiceImpl(
    private val orderRepository: OrderRepository,
    private val snsAndSqsService: SnsAndSqsService,
): OrderAdminService {
    var logger: Logger = LoggerFactory.getLogger(OrderAdminServiceImpl::class.java)

    @Transactional
    override fun updateOrderStatus(id: Long, orderStatusDTO: OrderStatusDTO, authentication: Authentication): OrderDTO {
        logger.info("OrderAdminServiceImpl - updateOrderStatus...")
        val orderEntity = orderRepository.findByIdOrder(id)
        return if (orderEntity.isPresent) {
            var order = orderEntity.get()
            order.status = PaymentStatusEnum.valueOf(orderStatusDTO.status).value
            val savedOrder = orderRepository.save(order)
            snsAndSqsService.sendMessage(jacksonObjectMapper().writeValueAsString(OrderResponseDTO(savedOrder.toDTO())))
            savedOrder.toDTO()
        } else {
            throw Exceptions.BadStatusException(ErrorMsgConstants.ERROR_ORDER_NOT_FOUND)
        }
    }

    @Transactional
    override fun updateOrderFinishedAndStatus(orderFinishDTO: OrderFinishDTO, authentication: Authentication): OrderDTO {
        logger.info("OrderAdminServiceImpl - updateOrderFinishedAndStatus...")
        val orderEntity = orderRepository.findByIdOrder(orderFinishDTO.idOrder)
        return if (orderEntity.isPresent) {
            var order = orderEntity.get()
            order.status = OrderStatusEnum.FINISHED.value
            order.isFinished = true
            val savedOrder = orderRepository.save(order)
            snsAndSqsService.sendMessage(jacksonObjectMapper().writeValueAsString(OrderResponseDTO(savedOrder.toDTO())))
            savedOrder.toDTO()
        } else {
            throw Exceptions.BadStatusException(ErrorMsgConstants.ERROR_ORDER_NOT_FOUND)
        }
    }


    override fun getOrders(): List<OrderDTO?> {
        logger.info("OrderAdminServiceImpl - getOrders...")
        return orderRepository.findAll()
            .map{ it?.toDTO() }
    }

    override fun deleteAllOrders() {
        logger.info("OrderAdminServiceImpl - deleteAllOrders...")
         return orderRepository.deleteAll()
    }
}
package com.mvp.order.domain.service.order

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.mvp.order.domain.model.exception.Exceptions
import com.mvp.order.domain.model.order.*
import com.mvp.order.domain.model.order.enums.OrderStatusEnum
import com.mvp.order.domain.model.product.ProductRemoveOrderDTO
import com.mvp.order.infrastruture.repository.order.OrderProductRepository
import com.mvp.order.infrastruture.repository.order.OrderRepository
import com.mvp.order.domain.service.message.SnsService
import com.mvp.order.domain.service.product.ProductServiceImpl
import com.mvp.order.domain.service.user.UserServiceImpl
import com.mvp.order.infrastruture.entity.order.OrderProductEntity
import com.mvp.order.utils.constants.ErrorMsgConstants
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.*

@Service
class OrderServiceImpl @Autowired constructor(
    private val snsService: SnsService,
    private val orderRepository: OrderRepository,
    private val orderProductRepository: OrderProductRepository,
    private val productService: ProductServiceImpl,
    private val userService: UserServiceImpl
): OrderService {
    var logger: Logger = LoggerFactory.getLogger(OrderServiceImpl::class.java)

    private val mapper = ObjectMapper().registerModule(JavaTimeModule())

    override fun getOrderById(id: Long): OrderByIdResponseDTO {
        val orderEntity = orderRepository.findByIdOrder(id)
        
        val orderResponseDTO = orderEntity.toResponseDTO()

        val orderProducts = orderProductRepository.findAllByIdOrderInfo(orderResponseDTO.id!!)
        orderResponseDTO.products.addAll(orderProducts)

        return orderResponseDTO
    }

    override fun getOrderByExternalId(externalId: UUID): OrderByIdResponseDTO? {
        return orderRepository.findByExternalId(externalId.toString()).toResponseDTO()
    }

    override fun createOrder(orderRequestDTO: OrderRequestDTO): OrderResponseDTO {
        var total: BigDecimal
        val listIDproduct = orderRequestDTO.orderProduct.mapNotNull { it.idProduct }.toMutableList()

        val userDTO = userService.getByUsername(orderRequestDTO.username)

        val products = productService.getAllById(listIDproduct)
        if (products.isEmpty()) {
            throw Exceptions.NotFoundException(ErrorMsgConstants.ERROR_PRODUCT_NOT_FOUND)
        } else {
            total = products.sumOf { it.price }
        }

        val existingOrder = orderRepository.findByUsername(orderRequestDTO.username)
        val orderEntity = existingOrder.apply {
            this.idClient = userDTO?.id
            this.totalPrice = this.totalPrice.add(total)
        }

        val savedOrder = orderRepository.save(orderEntity)
        orderRequestDTO.orderProduct.forEach { it.idOrder = savedOrder.id }
        saveAllOrderProduct(orderRequestDTO.toEntityList())
        val orderDTO = savedOrder.toDTO().apply {
            productList.addAll(getAllOrderProductsByIdOrder(savedOrder.id!!))
        }
        snsService.sendMessage(mapper.writeValueAsString(OrderResponseDTO(orderDTO)))
        return OrderResponseDTO(orderDTO)
    }

    override fun updateOrderProduct(orderRequestDTO: OrderRequestDTO): OrderResponseDTO {
        val orderEntity = orderRepository.findByUsername(orderRequestDTO.username)
        logger.info("${orderEntity.status} ${OrderStatusEnum.PAID.value}")

        if (orderEntity.status == OrderStatusEnum.PAID.value) {
            throw Exceptions.NotFoundException(ErrorMsgConstants.ERROR_ORDER_PAID_CONSTANT)
        }
        return createOrder(orderRequestDTO)
    }

    fun saveAllOrderProduct(data: List<OrderProductEntity>): List<OrderProductEntity>? {
        return data.let {
            orderProductRepository.saveAll(it).toList()
        }
    }

    override fun getAllOrderProductsByIdOrder(id: Long): List<OrderProductResponseDTO> {
        val orderProducts = orderProductRepository.findAllByIdOrderInfo(id)
        if (orderProducts.isEmpty()) {
            throw Exceptions.NotFoundException(ErrorMsgConstants.ERROR_ORDER_PRODUCT_NOT_FOUND)
        }
        return orderProducts.map { it }
    }

    override fun deleteOrderById(id: Long) {
        val order = orderRepository.findByIdOrder(id)

        order.id?.let { orderProductRepository.deleteByIdOrder(it) }
        orderRepository.deleteById(id)
    }

    override fun deleteOrderProductById(products: ProductRemoveOrderDTO) {
        try {
            val listProductId = products.orderProductId.toList()
            orderProductRepository.deleteAllById(listProductId)
        } catch (e: Throwable) {
            throw Exceptions.NotFoundException(ErrorMsgConstants.ERROR_ORDER_NOT_FOUND)
        }
    }
}
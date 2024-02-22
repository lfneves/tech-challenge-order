package com.mvp.order.domain.service.order

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
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
import com.mvp.order.infrastruture.entity.order.OrderEntity
import com.mvp.order.infrastruture.entity.order.OrderProductEntity
import com.mvp.order.infrastruture.repository.order.OrderProductResponseRepository
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
    private val orderProductResponseRepository: OrderProductResponseRepository,
    private val productService: ProductServiceImpl,
    private val userService: UserServiceImpl
): OrderService {
    var logger: Logger = LoggerFactory.getLogger(OrderServiceImpl::class.java)

    private val mapper = ObjectMapper().registerModule(JavaTimeModule()).enable(SerializationFeature.INDENT_OUTPUT)

    override fun getOrderById(id: Long): OrderByIdResponseDTO {
        val orderEntity = orderRepository.findByIdOrder(id)
        return if(orderEntity.isPresent) {
            val orderResponseDTO = orderEntity.get().toResponseDTO()
            val orderProducts = findAllByIdOrderInfo(orderResponseDTO.id!!)
            orderResponseDTO.products.addAll(orderProducts)
            orderResponseDTO
        } else {
            throw Exceptions.RequestedElementNotFoundException(ErrorMsgConstants.ERROR_ORDER_NOT_FOUND)
        }
    }

    override fun findAllByIdOrderInfo(id: Long): List<OrderProductResponseDTO> {
        return orderProductResponseRepository.findAllByIdOrderInfo(id).map { entity ->
            try {
                OrderProductResponseDTO(
                    id = entity.id,
                    idProduct = entity.idProduct,
                    idOrder = entity.idOrder,
                    productName = entity.productName,
                    categoryName = entity.categoryName,
                    price = entity.price
                )
            } catch (e: Exception) {
                throw Exceptions.RequestedElementNotFoundException(ErrorMsgConstants.ERROR_ORDER_NOT_FOUND)
            }
        }
    }

    override fun getOrderByExternalId(externalId: String): OrderByIdResponseDTO {
        val orderEntity = orderRepository.findByExternalId(externalId)
        return if(orderEntity.isPresent) {
            val orderResponseDTO = orderEntity.get().toResponseDTO()
            val orderProducts = findAllByIdOrderInfo(orderResponseDTO.id!!)
            orderResponseDTO.products.addAll(orderProducts)
            orderResponseDTO
        } else {
            throw Exceptions.RequestedElementNotFoundException(ErrorMsgConstants.ERROR_ORDER_NOT_FOUND)
        }
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

        val existingOrder = orderRepository.findByUsernameIfExists(orderRequestDTO.username)
            .orElse(OrderEntity())
        val orderEntity = existingOrder.apply {
            if (userDTO != null) {
                this.idClient = userDTO.id
            }
            this.externalId = existingOrder.externalId ?: UUID.randomUUID()
            this.totalPrice = BigDecimal.ZERO
            this.totalPrice = this.totalPrice.add(total)
            this.status = OrderStatusEnum.PENDING.value
        }

        val savedOrder = orderRepository.save(orderEntity).toDTO()
        orderRequestDTO.orderProduct.forEach { it.idOrder = savedOrder.id }
        savedOrder.productList = orderProductRepository.saveAll(orderRequestDTO.toEntityList()).toMutableList()

//        snsService.sendMessage(mapper.writeValueAsString(OrderResponseDTO(savedOrder)))
        return OrderResponseDTO(savedOrder)
    }

    override fun updateOrderProduct(orderRequestDTO: OrderRequestDTO): OrderResponseDTO {
        val orderEntityOptional = orderRepository.findByUsernameIfExists(orderRequestDTO.username)
        return if(orderEntityOptional.isPresent) {
            val orderEntity = orderEntityOptional.get()
            logger.info("${orderEntity.status} ${OrderStatusEnum.PAID.value}")
            if (orderEntity.status == OrderStatusEnum.PAID.value) {
                throw Exceptions.NotFoundException(ErrorMsgConstants.ERROR_ORDER_PAID_CONSTANT)
            }
            createOrder(orderRequestDTO)
        } else {
            throw Exceptions.NotFoundException(ErrorMsgConstants.ERROR_ORDER_NOT_FOUND)
        }
    }

    override fun saveAllOrderProduct(data: List<OrderProductEntity>): List<OrderProductEntity>? {
        return data.let {
            orderProductRepository.saveAll(it).toList()
        }
    }

    override fun getAllOrderProductsByIdOrder(id: Long): List<OrderProductResponseDTO> {
        val orderEntity = orderRepository.findByIdOrder(id)
        return if(orderEntity.isPresent) {
            findAllByIdOrderInfo(orderEntity.get().id!!)
        } else {
            throw Exceptions.NotFoundException(ErrorMsgConstants.ERROR_ORDER_PRODUCT_NOT_FOUND)
        }
    }

    override fun deleteOrderById(id: Long) {
        try {
            orderProductRepository.deleteByIdOrder(id)
            orderRepository.deleteById(id)
        } catch (e: Throwable) {
            logger.error(ErrorMsgConstants.ERROR_ORDER_PRODUCT_NOT_FOUND, e.printStackTrace())
            throw Exceptions.NotFoundException(ErrorMsgConstants.ERROR_ORDER_PRODUCT_NOT_FOUND)
        }
    }

    override fun deleteOrderProductById(products: ProductRemoveOrderDTO) {
        try {
            val listProductId = products.orderProductId.toList()
            orderProductRepository.deleteAllProductsByIdOrder(products.idOrder, listProductId)
        } catch (e: Throwable) {
            logger.error(ErrorMsgConstants.ERROR_ORDER_PRODUCT_NOT_FOUND, e.printStackTrace())
            throw Exceptions.NotFoundException(ErrorMsgConstants.ERROR_ORDER_PRODUCT_NOT_FOUND)
        }
    }
}
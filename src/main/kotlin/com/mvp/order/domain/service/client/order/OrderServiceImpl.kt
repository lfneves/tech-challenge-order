package com.mvp.order.domain.service.client.order

import com.mvp.order.domain.service.client.user.UserServiceImpl
import com.mvp.order.domain.model.exception.Exceptions
import com.mvp.order.domain.model.order.*
import com.mvp.order.domain.model.order.enums.OrderStatusEnum
import com.mvp.order.domain.model.product.ProductDTO
import com.mvp.order.domain.model.product.ProductRemoveOrderDTO
import com.mvp.order.domain.service.client.product.ProductServiceImpl
import com.mvp.order.infrastruture.entity.order.OrderEntity
import com.mvp.order.infrastruture.entity.order.OrderProductEntity
import com.mvp.order.infrastruture.repository.order.OrderProductRepository
import com.mvp.order.infrastruture.repository.order.OrderRepository
import com.mvp.order.utils.constants.ErrorMsgConstants
import kotlinx.coroutines.reactor.awaitSingle
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono
import java.math.BigDecimal
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

@Service
class OrderServiceImpl(
    private val orderRepository: OrderRepository,
    private val userService: UserServiceImpl,
    private val orderProductRepository: OrderProductRepository,
    private val productService: ProductServiceImpl
): OrderService {
    var logger: Logger = LoggerFactory.getLogger(OrderServiceImpl::class.java)

    override fun getOrderById(id: Long): Mono<OrderByIdResponseDTO> {
        return orderRepository.findByIdOrder(id)
            .switchIfEmpty(Mono.error(Exceptions.NotFoundException(ErrorMsgConstants.ERROR_ORDER_NOT_FOUND)))
            .flatMap { it?.toResponseDTO().toMono() }
            .flatMap { order ->
                orderProductRepository.findAllByIdOrderInfo(order.id!!)
                    .collectList()
                    .flatMap {
                        order.products.addAll(it)
                        order.toMono()
                    }.then(Mono.just(order))
            }
    }

    override suspend fun getOrderByExternalId(externalId: UUID): OrderByIdResponseDTO? {
        return orderRepository.findByExternalId(externalId).awaitSingle().toResponseDTO()
    }

    override fun createOrder(orderRequestDTO: OrderRequestDTO): Mono<OrderResponseDTO> {

        var total: BigDecimal = BigDecimal.ZERO
        var listIDproduct = mutableListOf<Long>()
        orderRequestDTO.orderProduct.forEach {
            it.idProduct?.let { it1 -> listIDproduct.add(it1) }
        }

        return userService.getByUsername(orderRequestDTO.username)
            .switchIfEmpty(Mono.error(Exceptions.NotFoundException(ErrorMsgConstants.ERROR_USER_NOT_FOUND)))
            .flatMap {
                productService.getAllById(listIDproduct).toFlux()
                    .collectList()
                    .flatMap { product ->
                        if(product.isEmpty()) {
                            Mono.error(Exceptions.NotFoundException(ErrorMsgConstants.ERROR_PRODUCT_NOT_FOUND))
                        } else {
                            total = product.reduce { acc, productDTO -> ProductDTO(price = acc.price.add(productDTO.price)) }.price
                            Mono.just(it)
                        }
                    }
            }.flatMap { userDTO ->
                orderRepository.findByUsername(orderRequestDTO.username)
                    .flatMap {orderEntity ->
                        orderEntity.idClient = userDTO.id
                        orderEntity.totalPrice.add(total)
                        orderEntity.toMono()
                    }.switchIfEmpty {
                        return@switchIfEmpty Mono.just(
                            OrderEntity(
                                idClient = userDTO.id,
                                status = OrderStatusEnum.PENDING.value,
                                totalPrice = total
                            )
                        ).flatMap {
                            orderRepository.save(it)
                        }
                    }
            }.map {orderEntity ->
                orderRequestDTO.orderProduct.forEach {
                    it.idOrder = orderEntity.id
                }
                saveAllOrderProduct(orderRequestDTO.toEntityList().toList()).subscribe()
                OrderResponseDTO(orderEntity.toDTO())
            }
    }

    override fun updateOrderProduct(orderRequestDTO: OrderRequestDTO): Mono<OrderResponseDTO> {
        return orderRepository.findByUsername(orderRequestDTO.username)
            .switchIfEmpty(Mono.error(Exceptions.NotFoundException(ErrorMsgConstants.ERROR_ORDER_NOT_FOUND)))
            .flatMap {
                logger.info(it.status + " " + OrderStatusEnum.PAID.value)
                if(it.status == OrderStatusEnum.PAID.value) {
                    return@flatMap Mono.error(Exceptions.NotFoundException(ErrorMsgConstants.ERROR_ORDER_PAID_CONSTANT))
                }
                return@flatMap createOrder(orderRequestDTO)
            }
    }

    fun saveAllOrderProduct(data: List<OrderProductEntity>?): Flux<OrderProductEntity> {
        return Flux.fromIterable(data!!).flatMap(orderProductRepository::save)
    }

    override fun getAllOrderProductsByIdOrder(id: Long): Flux<OrderProductResponseDTO> {
       return orderProductRepository.findAllByIdOrderInfo(id)
           .switchIfEmpty(Flux.error(Exceptions.NotFoundException(ErrorMsgConstants.ERROR_ORDER_PRODUCT_NOT_FOUND)))
           .map { it.toDTO() }
    }

    override fun deleteOrderById(id: Long): Mono<Void> {
        return orderRepository.findByIdOrder(id).flatMap {order ->
            orderProductRepository.deleteByIdOrder(order?.id!!)
                .then(orderRepository.deleteById(id))
        }
    }

    override fun deleteOrderProductById(products: ProductRemoveOrderDTO): Mono<Void> {
        val listProductId = products.orderProductId.map { it }.toList()
        return orderProductRepository.deleteById(listProductId)
            .switchIfEmpty(Mono.error(Exceptions.NotFoundException(ErrorMsgConstants.ERROR_ORDER_NOT_FOUND)))
    }

    override fun fakeCheckoutOrder(orderCheckoutDTO: OrderCheckoutDTO): Mono<Boolean> {
        return orderRepository.findByIdOrder(orderCheckoutDTO.idOrder)
            .switchIfEmpty(Mono.error(Exceptions.NotFoundException(ErrorMsgConstants.ERROR_ORDER_NOT_FOUND)))
            .doOnNext { setStatus ->
                setStatus.status = OrderStatusEnum.PAID.value
                val randomMinutes = (20..75).random().toLong()
                val z = ZoneId.of( "America/Sao_Paulo")
                setStatus.waitingTime = ZonedDateTime.now(z).plusMinutes(randomMinutes).toLocalDateTime()
            }.flatMap(orderRepository::save)
            .thenReturn(true)
    }
}
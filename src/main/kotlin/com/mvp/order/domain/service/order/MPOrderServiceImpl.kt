package com.mvp.order.domain.service.order

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.mvp.order.domain.configuration.OrderEndpointPropertyConfiguration
import com.mvp.order.domain.configuration.OrderPropertyConfiguration
import com.mvp.order.domain.model.exception.Exceptions
import com.mvp.order.domain.model.order.OrderByIdResponseDTO
import com.mvp.order.domain.model.order.enums.OrderStatusEnum
import com.mvp.order.domain.model.order.store.*
import com.mvp.order.domain.model.order.store.webhook.MerchantOrderDTO
import com.mvp.order.domain.model.order.store.webhook.MerchantOrderResponseDTO
import com.mvp.order.infrastruture.repository.order.OrderRepository
import com.mvp.order.utils.constants.ErrorMsgConstants
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class MPOrderServiceImpl(
    private val orderRepository: OrderRepository,
    private val orderService: OrderService,
    private val orderPropertyConfiguration: OrderPropertyConfiguration,
    private val orderEndpointPropertyConfiguration: OrderEndpointPropertyConfiguration
): MPOrderService {

    private val restTemplate = RestTemplate()

    override fun checkoutOrder(username: String): QrDataDTO {
        val orderEntityOptional = orderRepository.findByUsernameIfExists(username)
        return if(orderEntityOptional.isPresent) {
            val orderEntity = orderEntityOptional.get()
            val orderByIdResponse = orderService.getOrderById(orderEntity.id!!)
            val jsonRequest = orderCheckoutGenerateQrs(orderByIdResponse)

            generateOrderQrs(jsonRequest)
        } else {
            throw Exceptions.NotFoundException(ErrorMsgConstants.ERROR_ORDER_NOT_FOUND)
        }
    }

    override fun saveCheckoutOrderExternalStoreID(merchantOrderDTO: MerchantOrderDTO) {
        val  test = getMerchantOrderByID(merchantOrderDTO.resource)
        val order = orderService.getOrderByExternalId(UUID.fromString(test.externalReference))
        order?.status = if (test.orderStatus == "payment_required") OrderStatusEnum.PAYMENT_REQUIRED.value
            else OrderStatusEnum.PENDING.value
        orderRepository.updateStatus(order?.id!!, order.status)
    }

    override fun getMerchantOrderByID(requestUrl: String): MerchantOrderResponseDTO {
        val headers = HttpHeaders()
        headers.set("Authorization", orderPropertyConfiguration.token)

        val entity = HttpEntity<String>(headers)

        return restTemplate.exchange(
            requestUrl,
            HttpMethod.GET,
            entity,
            MerchantOrderResponseDTO::class.java
        ).body ?: throw RuntimeException("Response body is null")
    }

    override fun generateOrderQrs(requestBody: String): QrDataDTO {
        val endpoint = orderEndpointPropertyConfiguration.qrs.replace("?", orderPropertyConfiguration.userId)
        val requestUrl = orderPropertyConfiguration.url + endpoint

        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            accept = listOf(MediaType.APPLICATION_JSON)
            set("Authorization", orderPropertyConfiguration.token)
        }

        val entity = HttpEntity<String>(requestBody, headers)
        return restTemplate.exchange(
            requestUrl, HttpMethod.PUT, entity, QrDataDTO::class.java
        ).body ?: throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No response from QR service")
    }

    override fun orderCheckoutGenerateQrs(order: OrderByIdResponseDTO): String {
        val mapper = jacksonObjectMapper()
        val products = mutableListOf<ItemDTO>()
        val orderQrsDTO = OrderQrsDTO()

        orderQrsDTO.externalReference = order.externalId.toString()
        orderQrsDTO.title = "${order.id}"
        orderQrsDTO.notificationUrl = orderPropertyConfiguration.notificationUrl
        orderQrsDTO.description = order.status

        order.products.forEach {
            val product = ItemDTO(
                category = it.categoryName!!,
                description = "Test",
                quantity = 1,
                sku_number = "${order.idClient}_${order.id}",
                title = it.productName!!,
                total_amount = 1,
                unit_measure = "unit",
                unit_price = it.price.toInt()
            )
            products.add(product)
        }
        val sponsor = SponsorDTO(id = 57174696)
        orderQrsDTO.totalAmount = products.sumOf { it.total_amount + it.unit_price }
        val cashOut = CashOutDTO(amount = products.sumOf { it.unit_price })
        orderQrsDTO.itemDTOS = products
        orderQrsDTO.sponsorDTO = sponsor
        orderQrsDTO.cashOut = cashOut

        return mapper.writeValueAsString(orderQrsDTO)
    }
}
package com.mvp.order.domain.service.client.product


import com.mvp.order.domain.model.product.CategoryDTO
import com.mvp.order.domain.model.product.ProductDTO
import com.mvp.order.domain.model.exception.Exceptions
import com.mvp.order.infrastruture.repository.product.CategoryRepository
import com.mvp.order.infrastruture.repository.product.ProductRepository
import com.mvp.order.utils.constants.ErrorMsgConstants
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.math.BigDecimal

@Service
class ProductServiceImpl(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository
) : ProductService {


    override fun getProductById(id: Int): Mono<ProductDTO> {
        return productRepository.findById(id)
            .switchIfEmpty(Mono.error(Exceptions.NotFoundException(ErrorMsgConstants.ERROR_PRODUCT_NOT_FOUND)))
            .flatMap{ product ->
                categoryRepository.findById(product?.idCategory!!)
                    .map { category ->
                        return@map product.toDTO(category?.toDTO())
                    }
            }
    }

    override fun getProducts(): Flux<ProductDTO> {
        return productRepository
            .findAll()
            .flatMap{ product ->
                categoryRepository.findById(product?.idCategory!!)
                    .map { category ->
                    return@map product.toDTO(category?.toDTO())
                }
            }
    }

    override fun getAllById(id: List<Long>): Flux<ProductDTO> {
        return productRepository
            .findAllProductById(id)
            .flatMap{ product ->
                categoryRepository.findById(product?.idCategory!!)
                    .map { category ->
                        return@map product.toDTO(category?.toDTO())
                    }
            }.switchIfEmpty(Mono.error(Exceptions.NotFoundException(ErrorMsgConstants.ERROR_PRODUCT_NOT_FOUND)))
    }

    override fun getByIdTotalPrice(ids: List<Long?>): Mono<BigDecimal> {
        return productRepository.findByIdTotalPrice(ids)
            .flatMap { Mono.just(it.price) }
            .map { it }
    }

//    @Cacheable("productsCache")
    override fun getProductsByCategoryByName(name: String): Flux<ProductDTO> {
        return categoryRepository.findByName(name)
            .switchIfEmpty(Mono.error(Exceptions.NotFoundException(ErrorMsgConstants.ERROR_CATEGORY_NOT_FOUD)))
            .flatMap {category ->
                productRepository.findByIdCategory(category.id)
                    .map { product ->
                        product.toDTO(category.toDTO())
                    }
            }
    }
}
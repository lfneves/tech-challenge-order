package com.mvp.order.domain.service.product

import com.mvp.order.domain.model.exception.Exceptions
import com.mvp.order.domain.model.product.ProductDTO
import com.mvp.order.infrastruture.repository.product.CategoryRepository
import com.mvp.order.infrastruture.repository.product.ProductRepository
import com.mvp.order.utils.constants.ErrorMsgConstants
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class ProductServiceImpl(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository
): ProductService {

//    @Autowired
//    private lateinit var productRepository: ProductRepository
//    @Autowired
//    private lateinit var categoryRepository: CategoryRepository

    override fun getProductById(id: Int): ProductDTO {
        val product = productRepository.findById(id.toLong())
            .orElseThrow { Exceptions.NotFoundException(ErrorMsgConstants.ERROR_PRODUCT_NOT_FOUND) }

        val category = categoryRepository.findById(product?.idCategory!!)
            .orElseThrow { Exceptions.NotFoundException(ErrorMsgConstants.ERROR_CATEGORY_NOT_FOUND) }

        return product.toDTO(category.toDTO())
    }

    override fun getProducts(): List<ProductDTO> {
        val products = productRepository.findAll()

        return products.map { product ->
            val category = categoryRepository.findById(product?.idCategory!!)
                .orElseThrow { Exceptions.NotFoundException(ErrorMsgConstants.ERROR_CATEGORY_NOT_FOUND) }
            product.toDTO(category.toDTO())
        }
    }

    override fun getAllById(ids: List<Long>): List<ProductDTO> {
        if (ids.isEmpty()) {
            throw Exceptions.NotFoundException(ErrorMsgConstants.ERROR_PRODUCT_NOT_FOUND)
        }

        val products = productRepository.findAllProductById(ids)
        if (products.isEmpty()) {
            throw Exceptions.NotFoundException(ErrorMsgConstants.ERROR_PRODUCT_NOT_FOUND)
        }

        return products.mapNotNull { product ->
            val categoryId = product.idCategory ?: return@mapNotNull null
            val category = categoryRepository.findById(categoryId).orElse(null)
            product.toDTO(category?.toDTO())
        }
    }

    override fun getByIdTotalPrice(ids: List<Long?>): BigDecimal {
        return productRepository.findByIdTotalPrice(ids).price
    }

    override fun getProductsByCategoryByName(name: String): List<ProductDTO> {
        val category = categoryRepository.findByName(name)

        val products = productRepository.findByIdCategory(category.id)
        return products.map { product ->
            product.toDTO(category.toDTO())
        }
    }
}
package com.mvp.order.domain.service.admin.product


import com.mvp.order.domain.model.exception.Exceptions
import com.mvp.order.domain.model.product.CategoryDTO
import com.mvp.order.domain.model.product.ProductDTO
import com.mvp.order.domain.model.product.ProductRequestDTO
import com.mvp.order.infrastruture.entity.product.ProductEntity
import com.mvp.order.infrastruture.repository.product.CategoryRepository
import com.mvp.order.infrastruture.repository.product.ProductRepository
import com.mvp.order.utils.constants.ErrorMsgConstants
import org.springframework.dao.DataAccessException
import org.springframework.stereotype.Service

@Service
class ProductAdminServiceImpl(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository
): ProductAdminService {

    override fun saveProduct(productDTO: ProductDTO): ProductDTO {
        if (productDTO.idCategory == 0L) {
            throw Exceptions.NotFoundException(ErrorMsgConstants.ERROR_PRODUCT_NOT_FOUND)
        }

        try {
            val categoryEntity = categoryRepository.findById(productDTO.idCategory)
                .orElseThrow { IllegalStateException("Category ID ${productDTO.idCategory} not found") }

            productDTO.idCategory = categoryEntity.id ?: throw IllegalStateException("Category ID must not be null")
            productDTO.category = categoryEntity.toDTO()

            val savedProductEntity = productRepository.save(productDTO.toEntity())
            return savedProductEntity.toDTO(categoryEntity.toDTO())
        } catch (e: Exception) {
            throw Exceptions.NotFoundException(ErrorMsgConstants.ERROR_PRODUCT_NOT_FOUND)
        }
    }

    override fun updateProduct(id: Long, productRequestDTO: ProductRequestDTO): ProductDTO {
        var productEntity = productRepository.findById(id)
        return if (productEntity.isPresent) {
            var product = productEntity.get()
            product.updateUserEntity(product, productRequestDTO.toEntity())

            val updatedProduct = productRepository.save(product)

            val categoryEntity = updatedProduct.idCategory?.let {
                categoryRepository.findById(it)
                    .orElseThrow { IllegalStateException("Category ID ${updatedProduct.idCategory} not found") }
            }
            if (categoryEntity != null) {
                updatedProduct.idCategory = categoryEntity.id
            }
            updatedProduct.toDTO(categoryEntity?.toDTO())
        } else {
            throw Exceptions.NotFoundException(ErrorMsgConstants.ERROR_PRODUCT_NOT_FOUND)
        }
    }

    override fun deleteProductById(id: Long) {
        var productEntity = productRepository.findById(id)
        if(productEntity.isPresent) {
            productRepository.deleteById(id)
        } else {
            throw Exceptions.NotFoundException(ErrorMsgConstants.ERROR_PRODUCT_NOT_FOUND)
        }
    }

    override fun getProducts(): List<ProductDTO> {
        val products = productRepository.findAll()
        return products.map { product ->
            val category = categoryRepository.findById(product.idCategory!!).get()
            product.toDTO(category.toDTO())
        }
    }

    override fun getAllCategory(): List<CategoryDTO> {
        return categoryRepository
            .findAll()
            .map { it.toDTO() }
    }

    override fun deleteAllProducts() {
         return productRepository
             .deleteAll()
    }

    private fun getCategoryByName(categoryName: String): CategoryDTO {
        return categoryRepository.findByName(categoryName).toDTO()
    }
}
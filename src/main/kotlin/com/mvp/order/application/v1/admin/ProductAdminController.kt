package com.mvp.order.application.v1.admin

import com.mvp.order.domain.service.admin.product.ProductAdminService
import com.mvp.order.domain.model.product.CategoryDTO
import com.mvp.order.domain.model.product.ProductDTO
import com.mvp.order.domain.model.product.ProductRequestDTO
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/admin/product")
class ProductAdminController(private val productAdminService: ProductAdminService) {

    @PostMapping("/create")
    @Operation(
        summary = "Cria produtos",
        description = "Cadastro de produtos para utilização visualização de clientes",
        tags = ["Administrador Produtos"]
    )
    @ResponseStatus(HttpStatus.CREATED)
    fun createProduct(@RequestBody productDTO: ProductDTO): ResponseEntity<ProductDTO> {
        return ResponseEntity.ok(productAdminService.saveProduct(productDTO))
    }

    @PutMapping("/update/{id}")
    @Operation(
        summary = "Atualiza produto pelo id",
        description = "Atualiza produto cadastrados é preciso informa a categoria",
        tags = ["Administrador Produtos"]
    )
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun updateProduct(@PathVariable id: Long, @RequestBody productRequestDTO: ProductRequestDTO): ResponseEntity<ProductDTO> {
        return ResponseEntity.ok(productAdminService.updateProduct(id, productRequestDTO))
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Remove produto pelo id",
        description = "remove produto cadastrado pelo id informado",
        tags = ["Administrador Produtos"]
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteProduct(@PathVariable id: Long): ResponseEntity<Unit> {
        return ResponseEntity.ok(productAdminService.deleteProductById(id))
    }

    @GetMapping("/get-all-category")
    @Operation(
        summary = "Busca todas categorias",
        description = "Busca todas categorias cadastradas",
        tags = ["Produtos"]
    )
    fun getAllCategory(): ResponseEntity<List<CategoryDTO>> {
        return ResponseEntity.ok(productAdminService.getAllCategory())
    }
}
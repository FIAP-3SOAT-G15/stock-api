package com.fiap.stock.application.adapter.controller

import com.fiap.stock.application.domain.entities.Product
import com.fiap.stock.application.domain.valueobjects.ProductCategory
import com.fiap.stock.application.driver.web.ProductAPI
import com.fiap.stock.application.driver.web.request.ProductComposeRequest
import com.fiap.stock.application.driver.web.request.ProductRequest
import com.fiap.stock.application.driver.web.request.ProductStockBatchChangeRequest
import com.fiap.stock.application.driver.web.response.ProductResponse
import com.fiap.stock.application.usecases.AdjustStockUseCase
import com.fiap.stock.application.usecases.AssembleProductsUseCase
import com.fiap.stock.application.usecases.LoadProductUseCase
import com.fiap.stock.application.usecases.SearchProductUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class ProductController(
    private val assembleProductsUseCase: AssembleProductsUseCase,
    private val loadProductUseCase: LoadProductUseCase,
    private val searchProductUseCase: SearchProductUseCase,
    private val adjustStockUseCase: AdjustStockUseCase,
) : ProductAPI {
    override fun getByProductNumber(productNumber: Long): ResponseEntity<ProductResponse> {
        return loadProductUseCase.getByProductNumber(productNumber).let(::createResponse)
    }

    override fun findAll(): ResponseEntity<List<ProductResponse>> =
        loadProductUseCase.findAll().let(::respond)

    override fun findAllByProductNumber(productNumbers: List<Long>): ResponseEntity<List<ProductResponse>> =
        loadProductUseCase.findAllByProductNumber(productNumbers).let(::respond)

    override fun incrementStockOfProducts(productStockBatchChangeRequest: ProductStockBatchChangeRequest): ResponseEntity<String> {
        adjustStockUseCase.incrementStockOfProducts(productStockBatchChangeRequest.productNumberQuantityMap)
        return ResponseEntity.ok().build()
    }

    override fun decrementStockOfProducts(productStockBatchChangeRequest: ProductStockBatchChangeRequest): ResponseEntity<String> {
        adjustStockUseCase.decrementStockOfProducts(productStockBatchChangeRequest.productNumberQuantityMap)
        return ResponseEntity.ok().build()
    }

    override fun findByCategory(category: String): ResponseEntity<List<ProductResponse>> =
        loadProductUseCase.findByCategory(ProductCategory.fromString(category)).let(::respond)

    override fun searchByName(name: String): ResponseEntity<List<ProductResponse>> =
        searchProductUseCase.searchByName(name).let(::respond)

    override fun create(productRequest: ProductRequest): ResponseEntity<ProductResponse> =
        assembleProductsUseCase.create(productRequest.toDomain(), productRequest.components).let(::createResponse)

    override fun update(
        productNumber: Long,
        productRequest: ProductRequest,
    ): ResponseEntity<ProductResponse> {
        val product = productRequest.toDomain().copy(number = productNumber)
        return assembleProductsUseCase.update(product, productRequest.components).let(::createResponse)
    }

    override fun delete(productNumber: Long): ResponseEntity<ProductResponse> =
        assembleProductsUseCase.delete(productNumber).let(::createResponse)

    override fun compose(productComposeRequest: ProductComposeRequest): ResponseEntity<ProductResponse> =
        assembleProductsUseCase.compose(
            productComposeRequest.productNumber,
            productComposeRequest.subItemsNumbers,
        ).let(::createResponse)

    private fun createResponse(product: Product?): ResponseEntity<ProductResponse> =
        ResponseEntity.ok(product?.let { ProductResponse.fromDomain(product) })

    private fun respond(products: List<Product>): ResponseEntity<List<ProductResponse>> =
        ResponseEntity.ok(products.map { ProductResponse.fromDomain(it) })
}

package com.fiap.stock.application.services

import com.fiap.stock.application.adapter.gateway.ProductGateway
import com.fiap.stock.application.domain.entities.Product
import com.fiap.stock.application.domain.errors.ErrorType
import com.fiap.stock.application.domain.errors.SelfOrderManagementException
import com.fiap.stock.application.domain.valueobjects.ProductCategory
import com.fiap.stock.application.usecases.AssembleProductsUseCase
import com.fiap.stock.application.usecases.LoadComponentUseCase
import com.fiap.stock.application.usecases.LoadProductUseCase
import com.fiap.stock.application.usecases.RemoveProductUseCase
import com.fiap.stock.application.usecases.SearchProductUseCase
import org.slf4j.LoggerFactory

class ProductService(
    private val productRepository: ProductGateway,
    private val loadComponentUseCase: LoadComponentUseCase,
) :
    LoadProductUseCase,
        SearchProductUseCase,
        AssembleProductsUseCase,
        RemoveProductUseCase {
    private val log = LoggerFactory.getLogger(javaClass)
    
    override fun getByProductNumber(productNumber: Long): Product =
        productRepository.findByProductNumber(productNumber)
            ?: throw SelfOrderManagementException(
                errorType = ErrorType.PRODUCT_NOT_FOUND,
                message = "Product [$productNumber] not found",
            )

    override fun findAll(): List<Product> =
        productRepository.findAll()

    override fun findAllByProductNumber(productNumbers: List<Long>): List<Product> =
        productRepository.findAllByProductNumber(productNumbers)

    override fun findByCategory(category: ProductCategory): List<Product> =
        productRepository.findByCategory(category)

    override fun searchByName(productName: String): List<Product> =
        productRepository.searchByName(productName.trim())

    override fun create(
        product: Product,
        components: List<Long>,
    ): Product {
        log.info("Creating product $product with components: $components")
        return productRepository.create(
            product.copy(components = components.map(loadComponentUseCase::getByComponentNumber))
        )
    }

    override fun update(
        product: Product,
        components: List<Long>,
    ): Product {
        log.info("Updating product $product with components: $components")
        return productRepository.update(
            product.copy(components = components.map(loadComponentUseCase::getByComponentNumber))
        )
    }

    override fun delete(productNumber: Long): Product {
        log.info("Removing product [$productNumber]")
        return productRepository.delete(productNumber)
    }

    override fun compose(
        productNumber: Long,
        subitemsNumbers: List<Long>,
    ): Product {
        log.info("Composing product [$productNumber] with sub items numbers: $subitemsNumbers")
        val product = getByProductNumber(productNumber)
        val subItems = subitemsNumbers.map(::getByProductNumber)
        val newProduct = product.copy(subItems = subItems)
        return productRepository.update(newProduct)
    }
}

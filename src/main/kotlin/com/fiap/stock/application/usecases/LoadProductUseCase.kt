package com.fiap.stock.application.usecases

import com.fiap.stock.application.domain.entities.Product
import com.fiap.stock.application.domain.valueobjects.ProductCategory

interface LoadProductUseCase {
    fun getByProductNumber(productNumber: Long): Product

    fun findAll(): List<Product>

    fun findAllByProductNumber(productNumbers: List<Long>): List<Product>

    fun findByCategory(category: ProductCategory): List<Product>
}

package com.fiap.stock.application.adapter.gateway

import com.fiap.stock.application.domain.entities.Product
import com.fiap.stock.application.domain.valueobjects.ProductCategory

interface ProductGateway {
    fun findAll(): List<Product>
    
    fun findAllByProductNumber(productNumbers: List<Long>): List<Product>

    fun findByProductNumber(productNumber: Long): Product?

    fun findByCategory(category: ProductCategory): List<Product>

    fun searchByName(name: String): List<Product>

    fun create(product: Product): Product

    fun update(product: Product): Product

    fun delete(productNumber: Long): Product
}

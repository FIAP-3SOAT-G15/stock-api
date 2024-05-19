package com.fiap.stock.application.usecases

import com.fiap.stock.application.domain.entities.Product

interface AssembleProductsUseCase {
    fun compose(productNumber: Long, subitemsNumbers: List<Long>): Product?

    fun create(product: Product, components: List<Long>): Product

    fun update(product: Product, components: List<Long>): Product

    fun delete(productNumber: Long): Product
}

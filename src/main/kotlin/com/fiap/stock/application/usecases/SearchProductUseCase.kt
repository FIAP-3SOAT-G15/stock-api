package com.fiap.stock.application.usecases

import com.fiap.stock.application.domain.entities.Product

interface SearchProductUseCase {
    fun searchByName(productName: String): List<Product>
}

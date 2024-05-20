package com.fiap.stock.application.usecases

import com.fiap.stock.application.domain.entities.Product

interface RemoveProductUseCase {
    fun delete(productNumber: Long): Product
}

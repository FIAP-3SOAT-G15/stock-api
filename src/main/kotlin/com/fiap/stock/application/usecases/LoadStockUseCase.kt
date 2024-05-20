package com.fiap.stock.application.usecases

import com.fiap.stock.application.domain.entities.Stock

interface LoadStockUseCase {
    fun getByComponentNumber(componentNumber: Long): Stock
}

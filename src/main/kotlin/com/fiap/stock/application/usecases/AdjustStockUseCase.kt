package com.fiap.stock.application.usecases

import com.fiap.stock.application.domain.entities.Stock

interface AdjustStockUseCase {
    fun increment(componentNumber: Long, quantity: Long): Stock

    fun decrement(componentNumber: Long, quantity: Long): Stock
}

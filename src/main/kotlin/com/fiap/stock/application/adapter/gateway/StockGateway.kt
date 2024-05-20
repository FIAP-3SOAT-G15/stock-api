package com.fiap.stock.application.adapter.gateway

import com.fiap.stock.application.domain.entities.Stock

interface StockGateway {
    fun findByComponentNumber(componentNumber: Long): Stock?

    fun create(stock: Stock): Stock

    fun update(stock: Stock): Stock
}

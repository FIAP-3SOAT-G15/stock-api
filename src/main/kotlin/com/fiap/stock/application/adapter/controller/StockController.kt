package com.fiap.stock.application.adapter.controller

import com.fiap.stock.application.domain.entities.Stock
import com.fiap.stock.application.driver.web.StockAPI
import com.fiap.stock.application.driver.web.request.QuantityRequest
import com.fiap.stock.application.usecases.AdjustStockUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class StockController(
    private val adjustStockUseCase: AdjustStockUseCase,
) : StockAPI {
    override fun increment(componentNumber: Long, quantityRequest: QuantityRequest): ResponseEntity<Stock> {
        return ResponseEntity.ok(adjustStockUseCase.increment(componentNumber, quantityRequest.quantity))
    }

    override fun decrement(componentNumber: Long, quantityRequest: QuantityRequest): ResponseEntity<Stock> {
        return ResponseEntity.ok(adjustStockUseCase.decrement(componentNumber, quantityRequest.quantity))
    }
}

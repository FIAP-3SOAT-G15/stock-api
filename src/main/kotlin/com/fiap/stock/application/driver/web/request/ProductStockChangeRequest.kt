package com.fiap.stock.application.driver.web.request

data class ProductStockBatchChangeRequest(
    val productNumberQuantityMap: Map<Long, Long>
)

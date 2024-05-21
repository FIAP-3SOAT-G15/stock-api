package com.fiap.stock.application.it

import com.fiap.stock.application.domain.valueobjects.ProductCategory
import com.fiap.stock.application.driver.web.request.ComponentRequest
import com.fiap.stock.application.driver.web.request.ProductRequest
import java.math.BigDecimal

fun createComponentRequest(
    name: String = "Hamburger",
    initialQuantity: Long = 100L,
) = ComponentRequest(
    name = name,
    initialQuantity = initialQuantity,
)

fun createProductRequest(
    name: String = "Big Mac",
    category: String = ProductCategory.MAIN.name,
    price: BigDecimal = BigDecimal("10.00"),
    description: String = "Dois hambúrgueres, alface, queijo, molho especial, cebola, picles, num pão com gergelim",
    minSub: Int = 3,
    maxSub: Int = 3,
    components: List<Long> = listOf(1, 2, 3, 4, 5, 6, 7),
) = ProductRequest(
    name = name,
    category = category,
    price = price,
    description = description,
    minSub = minSub,
    maxSub = maxSub,
    components = components,
)

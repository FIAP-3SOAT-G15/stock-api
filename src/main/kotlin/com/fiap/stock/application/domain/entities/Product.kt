package com.fiap.stock.application.domain.entities

import com.fiap.stock.application.domain.valueobjects.ProductCategory
import java.math.BigDecimal

data class Product(
    val number: Long? = null,
    val name: String,
    val price: BigDecimal,
    val description: String,
    val category: ProductCategory,
    val minSub: Int,
    val maxSub: Int,
    val subItems: List<Product>,
    val components: List<Component>,
) {
    fun update(newProduct: Product): Product =
        copy(
            name = newProduct.name,
            price = newProduct.price,
            description = newProduct.description,
            category = newProduct.category,
            subItems = newProduct.subItems,
            maxSub = newProduct.maxSub,
            minSub = newProduct.minSub,
            components = newProduct.components,
        )

}

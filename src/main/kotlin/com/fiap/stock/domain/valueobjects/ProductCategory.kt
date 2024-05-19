package com.fiap.stock.domain.valueobjects

import com.fiap.stock.domain.errors.ErrorType
import com.fiap.stock.domain.errors.SelfOrderManagementException

enum class ProductCategory {
    DRINK,
    MAIN,
    SIDE,
    DESSERT,
    ;

    companion object {
        fun fromString(category: String): ProductCategory {
            return ProductCategory.values().firstOrNull { it.name.equals(category.trim(), ignoreCase = true) }
                ?: throw SelfOrderManagementException(
                    errorType = ErrorType.INVALID_PRODUCT_CATEGORY,
                    message = "Product category [$category] is not valid",
                )
        }
    }
}

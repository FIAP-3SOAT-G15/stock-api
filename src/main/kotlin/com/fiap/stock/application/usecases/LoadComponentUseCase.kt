package com.fiap.stock.application.usecases

import com.fiap.stock.application.domain.entities.Component

interface LoadComponentUseCase {
    fun getByComponentNumber(componentNumber: Long): Component

    fun findByProductNumber(productNumber: Long): List<Component>

    fun findAll(): List<Component>
}

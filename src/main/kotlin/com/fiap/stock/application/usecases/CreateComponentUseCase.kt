package com.fiap.stock.application.usecases

import com.fiap.stock.application.domain.entities.Component

interface CreateComponentUseCase {
    fun create(component: Component, initialQuantity: Long): Component
}

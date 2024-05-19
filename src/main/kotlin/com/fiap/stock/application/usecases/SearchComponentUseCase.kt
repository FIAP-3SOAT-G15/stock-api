package com.fiap.stock.application.usecases

import com.fiap.stock.application.domain.entities.Component

interface SearchComponentUseCase {
    fun searchByName(componentName: String): List<Component>
}

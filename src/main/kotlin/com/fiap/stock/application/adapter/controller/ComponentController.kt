package com.fiap.stock.application.adapter.controller

import com.fiap.stock.application.domain.entities.Component
import com.fiap.stock.application.driver.web.ComponentAPI
import com.fiap.stock.application.driver.web.request.ComponentRequest
import com.fiap.stock.application.usecases.CreateComponentUseCase
import com.fiap.stock.application.usecases.LoadComponentUseCase
import com.fiap.stock.application.usecases.SearchComponentUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class ComponentController(
    private val loadComponentUseCase: LoadComponentUseCase,
    private val createComponentUseCase: CreateComponentUseCase,
    private val searchComponentUseCase: SearchComponentUseCase,
) : ComponentAPI {
    override fun findAll(): ResponseEntity<List<Component>> {
        return ResponseEntity.ok(loadComponentUseCase.findAll())
    }

    override fun findByProductNumber(productNumber: Long): ResponseEntity<List<Component>> {
        return ResponseEntity.ok(loadComponentUseCase.findByProductNumber(productNumber))
    }

    override fun create(componentRequest: ComponentRequest): ResponseEntity<Component> {
        return ResponseEntity.ok(
            createComponentUseCase.create(
                componentRequest.toComponent(),
                componentRequest.initialQuantity,
            ),
        )
    }

    override fun searchByName(name: String): ResponseEntity<List<Component>> {
        return ResponseEntity.ok(searchComponentUseCase.searchByName(name.trim()))
    }
}

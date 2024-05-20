package com.fiap.stock.application.adapter.controller

import com.fiap.stock.application.domain.entities.Product
import com.fiap.stock.application.domain.valueobjects.ProductCategory
import com.fiap.stock.application.driver.web.MenuAPI
import com.fiap.stock.application.usecases.LoadProductUseCase
import com.fiap.stock.application.usecases.SearchProductUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class MenuController(
    private val loadProductUseCase: LoadProductUseCase,
    private val searchProductUseCase: SearchProductUseCase,
) : MenuAPI {
    override fun findAll(): ResponseEntity<List<Product>> {
        return ResponseEntity.ok(loadProductUseCase.findAll())
    }

    override fun findByCategory(category: String): ResponseEntity<List<Product>> {
        return ResponseEntity.ok(loadProductUseCase.findByCategory(ProductCategory.fromString(category)))
    }

    override fun searchByName(name: String): ResponseEntity<List<Product>> {
        return ResponseEntity.ok(searchProductUseCase.searchByName(name))
    }
}

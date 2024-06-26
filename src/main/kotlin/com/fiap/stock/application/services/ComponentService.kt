package com.fiap.stock.application.services

import com.fiap.stock.application.adapter.gateway.ComponentGateway
import com.fiap.stock.application.adapter.gateway.ProductGateway
import com.fiap.stock.application.adapter.gateway.StockGateway
import com.fiap.stock.application.domain.entities.Component
import com.fiap.stock.application.domain.entities.Stock
import com.fiap.stock.application.domain.errors.ErrorType
import com.fiap.stock.application.domain.errors.SelfOrderManagementException
import com.fiap.stock.application.usecases.CreateComponentUseCase
import com.fiap.stock.application.usecases.LoadComponentUseCase
import com.fiap.stock.application.usecases.SearchComponentUseCase
import org.slf4j.LoggerFactory

class ComponentService(
    private val componentRepository: ComponentGateway,
    private val stockRepository: StockGateway,
    private val productRepository: ProductGateway,
) : LoadComponentUseCase,
    SearchComponentUseCase,
    CreateComponentUseCase {
    private val log = LoggerFactory.getLogger(javaClass)
    
    override fun getByComponentNumber(componentNumber: Long): Component {
        return componentRepository.findByComponentNumber(componentNumber)
            ?: throw SelfOrderManagementException(
                errorType = ErrorType.COMPONENT_NOT_FOUND,
                message = "Component [$componentNumber] not found",
            )
    }

    override fun findByProductNumber(productNumber: Long): List<Component> {
        return productRepository.findByProductNumber(productNumber)
            ?.components
            ?: throw SelfOrderManagementException(
                errorType = ErrorType.PRODUCT_NOT_FOUND,
                message = "Product [$productNumber] not found",
            )
    }

    override fun searchByName(componentName: String): List<Component> {
        return componentRepository.searchByName(componentName)
    }

    override fun findAll(): List<Component> {
        return componentRepository.findAll()
    }

    override fun create(
        component: Component,
        initialQuantity: Long,
    ): Component {
        log.info("Creating component $component with initial quantity of $initialQuantity")
        val savedComponent = componentRepository.create(component)
        val stock = Stock(componentNumber = savedComponent.number!!, quantity = initialQuantity)
        stockRepository.create(stock)
        return savedComponent
    }
}

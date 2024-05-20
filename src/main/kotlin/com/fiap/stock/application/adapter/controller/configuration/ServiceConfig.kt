package com.fiap.stock.application.adapter.controller.configuration

import com.fiap.stock.application.StockApiApp
import com.fiap.stock.application.adapter.gateway.ComponentGateway
import com.fiap.stock.application.adapter.gateway.ProductGateway
import com.fiap.stock.application.adapter.gateway.StockGateway
import com.fiap.stock.application.usecases.LoadComponentUseCase
import com.fiap.stock.application.services.ComponentService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import com.fiap.stock.application.services.ProductService
import com.fiap.stock.application.services.StockService

@Configuration
@ComponentScan(basePackageClasses = [StockApiApp::class])
class ServiceConfig {


    @Bean
    fun createProductService(
        productRepository: ProductGateway,
        loadComponentUseCase: LoadComponentUseCase,
    ): ProductService {
        return ProductService(
            productRepository,
            loadComponentUseCase,
        )
    }

    @Bean
    fun createComponentService(
        componentRepository: ComponentGateway,
        stockRepository: StockGateway,
        productRepository: ProductGateway,
    ): ComponentService {
        return ComponentService(
            componentRepository,
            stockRepository,
            productRepository,
        )
    }

    @Bean
    fun createStockService(stockRepository: StockGateway): StockService {
        return StockService(stockRepository)
    }


}
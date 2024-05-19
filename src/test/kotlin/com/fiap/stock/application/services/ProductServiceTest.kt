package com.fiap.stock.application.services

import com.fiap.stock.application.adapter.gateway.ProductGateway
import com.fiap.stock.application.domain.errors.ErrorType
import com.fiap.stock.application.domain.errors.SelfOrderManagementException
import com.fiap.stock.application.usecases.LoadComponentUseCase
import createProduct
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ProductServiceTest {
    private val productRepository = mockk<ProductGateway>()
    private val loadInputUseCase = mockk<LoadComponentUseCase>()

    private val productService =
        ProductService(
            productRepository,
            loadInputUseCase,
        )

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Nested
    inner class GetByProductNumberTest {
        @Test
        fun `getByProductNumber should return a Product when it exists`() {
            val product = createProduct()

            every { productRepository.findByProductNumber(product.number!!) } returns product

            val result = productService.getByProductNumber(product.number!!)

            assertThat(result).isEqualTo(product)
        }

        @Test
        fun `getByProductNumber should throw an exception when the product is not found`() {
            val productNumber = 123L

            every { productRepository.findByProductNumber(productNumber) } returns null

            assertThatThrownBy { productService.getByProductNumber(productNumber) }
                .isInstanceOf(SelfOrderManagementException::class.java)
                .hasFieldOrPropertyWithValue("errorType", ErrorType.PRODUCT_NOT_FOUND)
        }
    }
}

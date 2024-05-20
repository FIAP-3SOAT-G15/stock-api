package com.fiap.stock.application.services

import com.fiap.stock.application.adapter.gateway.ProductGateway
import com.fiap.stock.application.domain.errors.ErrorType
import com.fiap.stock.application.domain.errors.SelfOrderManagementException
import com.fiap.stock.application.usecases.LoadComponentUseCase
import createComponent
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

    @Nested
    inner class GetByProduct {

        @Test
        fun `findAll should return a All products`() {
            val products = listOf(createProduct())

            every { productRepository.findAll() } returns products

            val response = productService.findAll()

            assertThat(response).isEqualTo(products)

        }

        @Test
        fun `findByCategory should return a All products by category`() {
            val products = listOf(createProduct())

            every { productRepository.findByCategory(products.first().category!!) } returns products

            val response = productService.findByCategory(products.first().category!!)

            assertThat(response).isEqualTo(products)
        }

        @Test
        fun `searchByName should return a All products by name`() {
            val products = listOf(createProduct())

            every { productRepository.searchByName(products.first().name!!) } returns products

            val response = productService.searchByName(products.first().name!!)

            assertThat(response).isEqualTo(products)

        }

    }


    @Nested
    inner class CrudProduct {

        @Test
        fun `create should return a a products created`() {

            val product = createProduct(number = 1)
            val component = createComponent(componentNumber = 1)

            every { productRepository.create(any()) } returns product.copy(components = arrayListOf(component))
            every { loadInputUseCase.getByComponentNumber(1) } returns component

            val response = productService.create(product, arrayListOf(1))

            assertThat(response.components).isNotEmpty()

        }

        @Test
        fun `update should return a a products updated`() {

            val product = createProduct(number = 1)
            val component = createComponent(componentNumber = 1)

            every { productRepository.update(any()) } returns product.copy(components = arrayListOf(component))
            every { loadInputUseCase.getByComponentNumber(1) } returns component

            val response = productService.update(product, arrayListOf(1))

            assertThat(response.components).isNotEmpty()

        }

        @Test
        fun `delete should return a a products deleted`() {
            val product = createProduct(number = 1)

            every { productRepository.delete(1) } returns product

            val response = productService.delete(1)

            assertThat(response).isEqualTo(product)
        }

        @Test
        fun `compose should return a a products composed`() {
            val product = createProduct(number = 1)
            val product2 = createProduct(number = 2)
            val product3 = createProduct(number = 3)


            every { productRepository.findByProductNumber(1) } returns product
            every { productRepository.findByProductNumber(2) } returns product2
            every { productRepository.findByProductNumber(3) } returns product3

            every { productRepository.update(any()) } returns product.copy(subItems = listOf(product2, product3))

            val response = productService.compose(1, arrayListOf(2,3))

            assertThat(response.subItems).isNotEmpty()
        }

    }
}

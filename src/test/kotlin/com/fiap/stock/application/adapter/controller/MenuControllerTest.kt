package com.fiap.stock.application.adapter.controller

import com.fiap.stock.application.domain.valueobjects.ProductCategory
import com.fiap.stock.application.usecases.LoadProductUseCase
import com.fiap.stock.application.usecases.SearchProductUseCase
import com.fiap.stock.application.createProduct
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class MenuControllerTest {
    private val loadProductUseCase: LoadProductUseCase = mockk<LoadProductUseCase>()
    private val searchProductUseCase: SearchProductUseCase = mockk<SearchProductUseCase>()

    private val controller =
        MenuController(
            loadProductUseCase = loadProductUseCase,
            searchProductUseCase = searchProductUseCase,
        )

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Nested
    inner class FindFromMenu {

        @Test
        fun `findAll should return of all products`() {
            val product1 = createProduct(number = 1)
            val product2 = createProduct(number = 2)
            val product3 = createProduct(number = 3)


            every { loadProductUseCase.findAll() } returns arrayListOf(product1, product2, product3)

            val products = controller.findAll()

            assertThat(products.statusCode.value())
                .isEqualTo(200)

            assertThat(products.body)
                .isNotNull()
                .containsExactly(product1, product2, product3)

        }
    }

    @Test
    fun `findByCategory should return all products belonging to this category`() {

        val product1 = createProduct(number = 1, category = ProductCategory.MAIN)
        val product2 = createProduct(number = 2, category = ProductCategory.MAIN)
        val product3 = createProduct(number = 3, category = ProductCategory.SIDE)

        every { loadProductUseCase.findByCategory(ProductCategory.MAIN) } returns arrayListOf(product1, product2)
        every { loadProductUseCase.findByCategory(ProductCategory.SIDE) } returns arrayListOf(product3)

        val response1 = controller.findByCategory("MAIN")
        val response2 = controller.findByCategory("SIDE")

        assertThat(response1.statusCode.value())
            .isEqualTo(200)

        assertThat(response2.statusCode.value())
            .isEqualTo(200)

        assertThat(response1.body)
            .isNotNull()
            .let { it.allMatch { p -> p.category == ProductCategory.MAIN } }

        assertThat(response2.body)
            .isNotNull()
            .let { it.allMatch { p -> p.category == ProductCategory.SIDE } }

    }

    @Nested
    inner class SearchInMenu {

        @Test
        fun `findByCategory should return all products belonging to this category` () {

            val productName = "BATATA"

            val product = createProduct(number = 1, name = productName)

            every {  searchProductUseCase.searchByName(productName) } returns arrayListOf(product)

            val response = controller.searchByName(productName)

            assertThat(response.statusCode.value())
                .isEqualTo(200)

            assertThat(response.body)
                .isNotNull()
                .containsExactly(product)
        }


    }


}

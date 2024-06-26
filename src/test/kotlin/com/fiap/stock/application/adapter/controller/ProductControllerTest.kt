package com.fiap.stock.application.adapter.controller

import com.fiap.stock.application.domain.valueobjects.ProductCategory
import com.fiap.stock.application.driver.web.request.ProductComposeRequest
import com.fiap.stock.application.driver.web.response.ProductResponse
import com.fiap.stock.application.usecases.AdjustStockUseCase
import com.fiap.stock.application.usecases.AssembleProductsUseCase
import com.fiap.stock.application.usecases.LoadProductUseCase
import com.fiap.stock.application.usecases.SearchProductUseCase
import com.fiap.stock.application.createProduct
import com.fiap.stock.application.it.createProductRequest
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ProductControllerTest {

    private val assembleProductsUseCase = mockk<AssembleProductsUseCase>()
    private val loadProductUseCase = mockk<LoadProductUseCase>()
    private val searchProductUseCase = mockk<SearchProductUseCase>()
    private val adjustStockUseCase = mockk<AdjustStockUseCase>()

    private val controller =
        ProductController(
            assembleProductsUseCase = assembleProductsUseCase,
            loadProductUseCase = loadProductUseCase,
            searchProductUseCase = searchProductUseCase,
            adjustStockUseCase = adjustStockUseCase,
        )

    @Nested
    inner class CrudProduct {

        @Test
        fun `create should return product created`() {

            val request = createProductRequest()
            val product = request.toDomain()
            val completeProduct = product.copy(number = 1).update(product.copy(name = "batata"))

            every { assembleProductsUseCase.create(product, request.components) } returns completeProduct

            val response = controller.create(request)

            assertThat(response.statusCode.value())
                .isEqualTo(200)

            assertThat(response.body)
                .isNotNull()
                .isInstanceOf(ProductResponse::class.java)
        }

        @Test
        fun `updated should return product updated`() {

            val request = createProductRequest()
            val product = request.toDomain()

            every { assembleProductsUseCase.update(product.copy(number = 1), request.components) } returns product.copy(number = 1)

            val response = controller.update(1, request)

            assertThat(response.statusCode.value())
                .isEqualTo(200)

        }

        @Test
        fun `delete should return product deleted`() {
            val request = createProductRequest()
            val product = request.toDomain()

            every { assembleProductsUseCase.delete(1) } returns product.copy(number = 1)

            val response = controller.delete(1)

            assertThat(response.statusCode.value())
                .isEqualTo(200)
        }

        @Test
        fun `compose should return product composed`() {
            val request = ProductComposeRequest(1, arrayListOf(2,3))
            val p2 = createProduct(number = 2)
            val p3 = createProduct(number = 3)

            val p1 = createProduct(subitems = arrayListOf(p2, p3))


            every { assembleProductsUseCase.compose(
                request.productNumber,
                request.subItemsNumbers,
            ) } returns p1

            val response = controller.compose(request)

            assertThat(response.statusCode.value())
                .isEqualTo(200)
        }
    }

    @Nested
    inner class GetProduct {

        @Test
        fun `getByProductNumber should return product by number`() {
            val product = createProductRequest().toDomain()

            every { loadProductUseCase.getByProductNumber(1) } returns product.copy(number = 1)

            val response = controller.getByProductNumber(1)

            assertThat(response.statusCode.value())
                .isEqualTo(200)
        }


        @Test
        fun `findAll should return all products`() {
            val product = createProductRequest().toDomain()

            every { loadProductUseCase.findAll() } returns arrayListOf(product.copy(number = 1))

            val response = controller.findAll()

            assertThat(response.statusCode.value())
                .isEqualTo(200)

            assertThat(response.body)
                .isNotNull()

        }

        @Test
        fun `findByCategory should return all from a category`() {
            val product = createProductRequest().toDomain()

            every { loadProductUseCase.findByCategory(ProductCategory.DRINK) } returns arrayListOf(product.copy(number = 1))

            val response = controller.findByCategory("DRINK")

            assertThat(response.statusCode.value())
                .isEqualTo(200)

            assertThat(response.body)
                .isNotNull()
        }

        @Test
        fun `searchByName should return all products by name`() {
            val product = createProductRequest().toDomain()

            every { searchProductUseCase.searchByName("coca") } returns arrayListOf(product.copy(number = 1, name = "coca"))

            val response = controller.searchByName("coca")

            assertThat(response.statusCode.value())
                .isEqualTo(200)

            assertThat(response.body)
                .isNotNull()
        }
    }
}

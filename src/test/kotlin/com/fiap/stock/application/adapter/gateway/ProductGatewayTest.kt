package com.fiap.stock.application.adapter.gateway

import com.fiap.stock.application.adapter.gateway.impl.ProductGatewayImpl
import com.fiap.stock.application.domain.errors.SelfOrderManagementException
import com.fiap.stock.application.domain.valueobjects.ProductCategory
import com.fiap.stock.application.driver.database.persistence.jpa.ProductJpaRepository
import com.fiap.stock.application.driver.database.persistence.mapper.ProductMapper
import com.fiap.stock.application.createProduct
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mapstruct.factory.Mappers
import java.util.*

class ProductGatewayTest {

    private val productJpaRepository: ProductJpaRepository = mockk<ProductJpaRepository>()

    private val gateway : ProductGateway = ProductGatewayImpl(productJpaRepository)

    private val mapper = Mappers.getMapper(ProductMapper::class.java)


    @Nested
    inner class CrudProduct {

        @Test
        fun `create should return product created`() {

            val product = createProduct()

            every { productJpaRepository.save(any()) } returns mapper.toEntity(product.copy(number = 1))

            val result = gateway.create(product)

            assertThat(result)
                .isNotNull()

        }

        @Test
        fun `update should return product created`() {

            val product = createProduct().copy(number = 1)

            every { productJpaRepository.save(any()) } returns mapper.toEntity(product)
            every { productJpaRepository.findById(1) } returns Optional.of(mapper.toEntity(product))


            val result = gateway.update(product)

            assertThat(result)
                .isNotNull()
        }

        @Test
        fun `update should return exception when product not found`() {

            val product = createProduct().copy(number = 1)

            every { productJpaRepository.save(any()) } returns mapper.toEntity(product)
            every { productJpaRepository.findById(1) } returns Optional.empty()


            assertThrows<SelfOrderManagementException> {
                gateway.update(product)
            }

            assertThrows<SelfOrderManagementException> {
                gateway.update(product.copy(number = null))
            }

        }

        @Test
        fun `update should return deleted product`() {

            val product = createProduct().copy(number = 1)

            every { productJpaRepository.save(any()) } returns mapper.toEntity(product)
            every { productJpaRepository.findById(1) } returns Optional.empty()


            assertThrows<SelfOrderManagementException> {
                gateway.update(product)
            }

            assertThrows<SelfOrderManagementException> {
                gateway.update(product.copy(number = null))
            }

        }

        @Test
        fun `delete should exception if not exist`() {

            every { productJpaRepository.deleteById(1) } returns Unit
            every { productJpaRepository.findById(1) } returns Optional.empty()


            assertThrows<SelfOrderManagementException> {
                gateway.delete(1)
            }

        }

        @Test
        fun `delete should product deleted`() {
            val product = createProduct().copy(number = 1)

            every { productJpaRepository.deleteById(1) } returns Unit
            every { productJpaRepository.findById(1) } returns Optional.of(mapper.toEntity(product))

            val response = gateway.delete(1)

            assertThat(response).isNotNull()
        }
    }

    @Nested
    inner class GetProduct {

        @Test
        fun `findAll should return all products`() {
            val product = createProduct().copy(number = 1)

            every { productJpaRepository.findAll() } returns listOf(product).map { mapper.toEntity(it) }
            val result = gateway.findAll()

            assertThat(result).isNotEmpty()
        }

        @Test
        fun `findByProductNumber should return a product by number`() {

            val product = createProduct().copy(number = 1)

            every { productJpaRepository.findById(1) } returns Optional.of(mapper.toEntity(product))

            val result = gateway.findByProductNumber(1)

            assertThat(result).isNotNull()

        }

        @Test
        fun `findByComponentNumber should return null if not exist`() {

            every { productJpaRepository.findById(1) } returns Optional.empty()
            val result = gateway.findByProductNumber(1)

            assertThat(result).isNull()
        }

        @Test
        fun `searchByName should return a product by name`() {
            val product = createProduct().copy(number = 1)

            every { productJpaRepository.findByNameContainingIgnoreCase("batata") } returns listOf(product).map { mapper.toEntity(it) }

            val result = gateway.searchByName("batata")

            assertThat(result).isNotNull()

        }

        @Test
        fun `findByCategory should return a product by name`() {
            val product = createProduct().copy(number = 1)

            every { productJpaRepository.findByCategoryIgnoreCase("MAIN") } returns listOf(product).map { mapper.toEntity(it) }

            val result = gateway.findByCategory(ProductCategory.MAIN)

            assertThat(result).isNotNull()

        }

    }


}

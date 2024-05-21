package com.fiap.stock.application.adapter.gateway

import com.fiap.stock.application.adapter.gateway.impl.StockGatewayImpl
import com.fiap.stock.application.domain.errors.SelfOrderManagementException
import com.fiap.stock.application.driver.database.persistence.jpa.StockJpaRepository
import com.fiap.stock.application.driver.database.persistence.mapper.StockMapper
import com.fiap.stock.application.createStock
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mapstruct.factory.Mappers
import java.util.*

class StockGatewayTest {

    private val stockJpaRepository: StockJpaRepository = mockk<StockJpaRepository>()

    private val gateway : StockGateway = StockGatewayImpl(stockJpaRepository)

    private val mapper = Mappers.getMapper(StockMapper::class.java)

    @Nested
    inner class CrudStock {


        @Test
        fun `create should return product created`() {
            val stock = createStock().copy(componentNumber = 1, quantity = 2)
            val newStock = stock.update(stock)

            every { stockJpaRepository.save(any()) } returns mapper.toEntity(newStock)
            every { stockJpaRepository.findById(1) } returns Optional.empty()

            val result = gateway.create(newStock)

            assertThat(result).isNotNull()
        }

        @Test
        fun `create should return exception if stock already exists`() {
            val stock = createStock().copy(componentNumber = 1)

            every { stockJpaRepository.save(any()) } returns mapper.toEntity(stock)
            every { stockJpaRepository.findById(1) } returns Optional.of(mapper.toEntity(stock))


            assertThrows<SelfOrderManagementException> {
                gateway.create(stock)
            }

        }

        @Test
        fun `updated should return product updated`() {
            val stock = createStock().copy(componentNumber = 1)

            every { stockJpaRepository.save(any()) } returns mapper.toEntity(stock)
            every { stockJpaRepository.findById(1) } returns Optional.of(mapper.toEntity(stock))

            val result = gateway.update(stock)

            assertThat(result).isNotNull()
        }

        @Test
        fun `updated should return exception if stock not exists`() {
            val stock = createStock().copy(componentNumber = 1)

            every { stockJpaRepository.findById(1) } returns Optional.empty()

            assertThrows<SelfOrderManagementException> {
                gateway.update(stock)
            }

        }

    }

    @Nested
    inner class GetStock {

        @Test
        fun `findByComponentNumber should return null if stock not exists`() {

            every { stockJpaRepository.findById(1) } returns Optional.empty()

            val result  = gateway.findByComponentNumber(1)

            assertThat(result).isNull()
        }

        @Test
        fun `findByComponentNumber should return stock by number`() {
            val stock = createStock().copy(componentNumber = 1)

            every { stockJpaRepository.findById(1) } returns Optional.of(mapper.toEntity(stock))

            val result  = gateway.findByComponentNumber(1)

            assertThat(result).isNotNull()
        }

    }


}

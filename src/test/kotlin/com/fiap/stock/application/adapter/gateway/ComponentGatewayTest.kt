package com.fiap.stock.application.adapter.gateway

import com.fiap.stock.application.adapter.gateway.impl.ComponentGatewayImpl
import com.fiap.stock.application.domain.errors.SelfOrderManagementException
import com.fiap.stock.application.driver.database.persistence.jpa.ComponentJpaRepository
import com.fiap.stock.application.driver.database.persistence.mapper.ComponentMapper
import createComponent
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mapstruct.factory.Mappers
import java.util.*

class ComponentGatewayTest {

    private val componentJpaRepository: ComponentJpaRepository = mockk<ComponentJpaRepository>()

    private val gateway : ComponentGateway = ComponentGatewayImpl(componentJpaRepository)

    private val mapper = Mappers.getMapper(ComponentMapper::class.java)


    @Nested
    inner class CrudComponent {

        @Test
        fun `create should return component created`() {

            val component = createComponent()

            every { componentJpaRepository.save(any()) } returns mapper.toEntity(component)

            val response = gateway.create(component)

            assertThat(response)
                .isNotNull()
                .isEqualTo(component)

        }

        @Test
        fun `update should return component updated`() {

            val component = createComponent().copy(number = 1)

            every { componentJpaRepository.save(any()) } returns mapper.toEntity(component)
            every { componentJpaRepository.findById(1) } returns Optional.of(mapper.toEntity(component))


            val response = gateway.update(component)

            assertThat(response)
                .isNotNull()
                .isEqualTo(component)
        }

        @Test
        fun `update if not exist should return exception`() {

            val component = createComponent().copy(number = 1)

            every { componentJpaRepository.save(any()) } returns mapper.toEntity(component)
            every { componentJpaRepository.findById(1) } returns Optional.empty()


            assertThrows<SelfOrderManagementException> {
                gateway.update(component)
            }

            val componentNullable = createComponent().copy(number = null)

            assertThrows<SelfOrderManagementException> {
                gateway.update(componentNullable)
            }

        }

        @Test
        fun `delete should exception if not exist`() {

            val component = createComponent().copy(number = 1)

            every { componentJpaRepository.deleteById(1) } returns Unit
            every { componentJpaRepository.findById(1) } returns Optional.empty()


            assertThrows<SelfOrderManagementException> {
                gateway.delete(component)
            }

        }

        @Test
        fun `delete should return component deleted`() {

            val component = createComponent().copy(number = 1)

            every { componentJpaRepository.deleteById(1) } returns Unit
            every { componentJpaRepository.findById(1) } returns Optional.of(mapper.toEntity(component))


            val result = gateway.delete(component)

            assertThat(result).isNotNull()
        }

        @Test
        fun `deleteAll should return nothing`() {

            every { componentJpaRepository.deleteAll() } returns Unit

            gateway.deleteAll()
        }



    }

    @Nested
    inner class FindComponent {

        @Test
        fun `findAll should return all components`() {
            val component1 = createComponent(componentNumber = 1)

            every { componentJpaRepository.findAll() } returns listOf(component1).map { mapper.toEntity(it) }
            val result = gateway.findAll()

            assertThat(result).isNotEmpty()

        }

        @Test
        fun `findByComponentNumber should return a component by number`() {

            val component1 = createComponent(componentNumber = 1)

            every { componentJpaRepository.findById(1) } returns Optional.of(mapper.toEntity(component1))
            val result = gateway.findByComponentNumber(1)

            assertThat(result).isNotNull()

        }

        @Test
        fun `findByComponentNumber should return null if not exist`() {

            every { componentJpaRepository.findById(1) } returns Optional.empty()
            val result = gateway.findByComponentNumber(1)

            assertThat(result).isNull()

        }

        @Test
        fun `searchByName should return a component by name`() {
            val component1 = createComponent(componentNumber = 1, name = "batata")

            every { componentJpaRepository.findByNameContainingIgnoreCase("batata") } returns listOf(component1).map { mapper.toEntity(it) }

            val result = gateway.searchByName("batata")

            assertThat(result).isNotNull()

        }

    }


}
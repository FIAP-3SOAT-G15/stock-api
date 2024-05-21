package com.fiap.stock.application.adapter.controller

import com.fiap.stock.application.driver.web.request.ComponentRequest
import com.fiap.stock.application.usecases.CreateComponentUseCase
import com.fiap.stock.application.usecases.LoadComponentUseCase
import com.fiap.stock.application.usecases.SearchComponentUseCase
import com.fiap.stock.application.createComponent
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ComponentControllerTest {

    private val loadComponentUseCase: LoadComponentUseCase = mockk<LoadComponentUseCase>()
    private val createComponentUseCase: CreateComponentUseCase = mockk<CreateComponentUseCase>()
    private val searchComponentUseCase: SearchComponentUseCase = mockk<SearchComponentUseCase>()

    private val controller =
        ComponentController(
            loadComponentUseCase = loadComponentUseCase,
            searchComponentUseCase = searchComponentUseCase,
            createComponentUseCase = createComponentUseCase,
        )

    @Nested
    inner class FindFromComponent {

        @Test
        fun `findAll should return of all components`() {
            val component1 = createComponent(componentNumber = 1)
            val component2 = createComponent(componentNumber = 2).update(
                component1
            )

            every { loadComponentUseCase.findAll() } returns listOf(component1, component2)

            val result = controller.findAll()

            assertThat(result.body)
                .isNotNull()
                .containsExactly(component1, component2)
        }

        @Test
        fun `findByProductNumber should return of all components from a product`() {
            val component1 = createComponent(componentNumber = 1)

            every { loadComponentUseCase.findByProductNumber(1) } returns listOf(component1)

            val result = controller.findByProductNumber(1)

            assertThat(result.body)
                .isNotNull()
                .containsExactly(component1)
        }

    }

    @Nested
    inner class SearchFromComponent {

        @Test
        fun `searchByName should return of all components with the name`() {
            val component = createComponent(componentNumber = 2)
            val term = ComponentRequest("Hambúrguer", 10).name

            every { searchComponentUseCase.searchByName(term) } returns listOf(component)

            val result = controller.searchByName(term)

            assertThat(result.body)
                .isNotNull()
                .containsExactly(component)

        }

    }

    @Nested
    inner class CreateComponent {

        @Test
        fun `create should return a component created`() {
            val request = ComponentRequest("Hambúrguer", 10)
            val component = request.toComponent()

            every { createComponentUseCase.create(component, 10) } returns component

            var response = controller.create(componentRequest = request)

            assertThat(response.body)
                .isNotNull()
                .isEqualTo(component)

        }

    }




}

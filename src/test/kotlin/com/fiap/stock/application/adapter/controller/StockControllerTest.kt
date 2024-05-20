package com.fiap.stock.application.adapter.controller

import com.fiap.stock.application.domain.entities.Stock
import com.fiap.stock.application.driver.web.request.QuantityRequest
import com.fiap.stock.application.usecases.AdjustStockUseCase
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class StockControllerTest {

    private val adjustStockUseCase: AdjustStockUseCase = mockk<AdjustStockUseCase>()

    private val controller =
        StockController(
            adjustStockUseCase = adjustStockUseCase
        )

    @Test
    fun `increment should increment stock`() {

        every { adjustStockUseCase.increment(any(), any()) } returns Stock(1, 2)

        val result = controller.increment(1, QuantityRequest(1));

        assertThat(result.body).isEqualTo(Stock(1, 2))
    }

    @Test
    fun `decrement should decrement stock`() {

        every { adjustStockUseCase.decrement(any(), any()) } returns Stock(1, 1)

        val result = controller.decrement(1, QuantityRequest(1));

        assertThat(result.body).isEqualTo(Stock(1, 1))
    }

}
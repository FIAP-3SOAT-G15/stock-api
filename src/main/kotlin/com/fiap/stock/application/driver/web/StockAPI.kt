package com.fiap.stock.application.driver.web

import com.fiap.stock.application.domain.entities.Stock
import com.fiap.stock.application.driver.web.request.QuantityRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Tag(name = "estoque", description = "Estoque de componentes")
@RequestMapping("/admin/stock")
@SuppressWarnings
interface StockAPI {
    @Operation(
        summary = "Acrescenta quantidade do componente em estoque",
        parameters = [
            Parameter(
                name = "x-admin-token",
                required = true,
                `in` = ParameterIn.HEADER,
                schema = Schema(type = "string", defaultValue = "token"),
            ),
        ],
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Operação bem-sucedida"),
            ApiResponse(responseCode = "404", description = "Item de estoque não encontrado"),
            ApiResponse(responseCode = "422", description = "Quantidade inválida"),
        ],
    )
    @PostMapping("/{componentNumber}/increment")
    fun increment(
        @Parameter(description = "Número do item em estoque") @PathVariable componentNumber: Long,
        @Parameter(description = "Quantidade a acrescentar") @RequestBody quantityRequest: QuantityRequest,
    ): ResponseEntity<Stock>

    @Operation(
        summary = "Reduz quantidade do componente em estoque",
        parameters = [
            Parameter(
                name = "x-admin-token",
                required = true,
                `in` = ParameterIn.HEADER,
                schema = Schema(type = "string", defaultValue = "token"),
            ),
        ],
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Operação bem-sucedida"),
            ApiResponse(responseCode = "404", description = "Item de estoque não encontrado"),
            ApiResponse(responseCode = "422", description = "Quantidade inválida"),
        ],
    )
    @PostMapping("/{componentNumber}/decrement")
    fun decrement(
        @Parameter(description = "Número do componente") @PathVariable componentNumber: Long,
        @Parameter(description = "Quantidade a reduzir") @RequestBody quantityRequest: QuantityRequest,
    ): ResponseEntity<Stock>
}

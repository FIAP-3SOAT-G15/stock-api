package com.fiap.stock.application.driver.web

import com.fiap.stock.application.driver.web.request.ProductComposeRequest
import com.fiap.stock.application.driver.web.request.ProductRequest
import com.fiap.stock.application.driver.web.request.ProductStockBatchChangeRequest
import com.fiap.stock.application.driver.web.response.ProductResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.websocket.server.PathParam
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Tag(name = "produto", description = "Produtos")
@RequestMapping("/admin/products")
@SuppressWarnings
interface ProductAPI {
    @Operation(
        summary = "Retorna todos os produtos",
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
        ],
    )
    @GetMapping
    fun findAll(): ResponseEntity<List<ProductResponse>>

    @Operation(
        summary = "Retorna todos os produtos identificados por número",
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
        ],
    )
    @GetMapping("/batch")
    fun findAllByProductNumber(
        @Parameter(description = "IDs de produtos") @RequestParam("numbers") productNumbers: List<Long>,
    ): ResponseEntity<List<ProductResponse>>
    
    @Operation(
        summary = "Incrementa estoque disponível para os produtos identificados",
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
        ],
    )
    @PostMapping("/batch/increment")
    fun incrementStockOfProducts(
        @Parameter(description = "Relações de produto e quantidade a incrementar")
        @RequestBody productStockBatchChangeRequest: ProductStockBatchChangeRequest,
    ): ResponseEntity<String>

    @Operation(
        summary = "Decrementa estoque disponível para os produtos identificados",
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
        ],
    )
    @PostMapping("/batch/decrement")
    fun decrementStockOfProducts(
        @Parameter(description = "Relações de produto e quantidade a decrementar")
        @RequestBody productStockBatchChangeRequest: ProductStockBatchChangeRequest,
    ): ResponseEntity<String>
    
    @Operation(
        summary = "Retorna produtos por categoria",
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
            ApiResponse(responseCode = "400", description = "Categoria inválida"),
        ],
    )
    @GetMapping("/category/{category}")
    fun findByCategory(
        @Parameter(description = "Categoria") @PathVariable category: String,
    ): ResponseEntity<List<ProductResponse>>

    @Operation(
        description = "Api de produtos, Retorna produto pelo número",
        summary = "Retorna produto pelo número",
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
            ApiResponse(responseCode = "404", description = "Produto não encontrado"),
        ],
    )
    @GetMapping("/{productNumber}")
    fun getByProductNumber(
        @Parameter(description = "Número do produto") @PathVariable("productNumber") productNumber: Long,
    ): ResponseEntity<ProductResponse>

    @Operation(
        summary = "Pesquisa produto por nome",
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
        ],
    )
    @GetMapping("/search")
    fun searchByName(
        @Parameter(description = "Nome do produto") @PathParam("name") name: String,
    ): ResponseEntity<List<ProductResponse>>

    @Operation(
        summary = "Cadastra um novo produto",
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
            ApiResponse(responseCode = "422", description = "Produto inválido"),
            ApiResponse(responseCode = "500", description = "Erro não esperado"),
        ],
    )
    @PostMapping
    fun create(
        @Parameter(description = "Cadastro do produto") @RequestBody productRequest: ProductRequest,
    ): ResponseEntity<ProductResponse>

    @Operation(
        summary = "Atualiza produto",
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
            ApiResponse(responseCode = "404", description = "Produto não encontrado"),
            ApiResponse(responseCode = "422", description = "Produto inválido"),
        ],
    )
    @PutMapping("/{productNumber}")
    fun update(
        @Parameter(description = "Número do produto") @PathVariable productNumber: Long,
        @Parameter(description = "Cadastro do produto") @RequestBody productRequest: ProductRequest,
    ): ResponseEntity<ProductResponse>

    @Operation(
        summary = "Remove produto",
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
            ApiResponse(responseCode = "404", description = "Produto não encontrado"),
        ],
    )
    @DeleteMapping("/{productNumber}")
    fun delete(
        @Parameter(description = "Número do produto") @PathVariable("productNumber") productNumber: Long,
    ): ResponseEntity<ProductResponse>

    @Operation(
        summary = "Atribui subitems ao produto",
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
            ApiResponse(responseCode = "404", description = "Produto não encontrado"),
        ],
    )
    @PostMapping("/compose")
    fun compose(
        @Parameter(description = "Montagem do produto") @RequestBody productComposeRequest: ProductComposeRequest,
    ): ResponseEntity<ProductResponse>
}

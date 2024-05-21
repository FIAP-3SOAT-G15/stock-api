package com.fiap.stock.application.it

import com.fiap.stock.application.adapter.gateway.ComponentGateway
import com.fiap.stock.application.adapter.gateway.ProductGateway
import com.fiap.stock.application.adapter.gateway.StockGateway
import com.fiap.stock.application.domain.entities.Component
import com.fiap.stock.application.domain.entities.Product
import com.fiap.stock.application.domain.entities.Stock
import com.fiap.stock.application.domain.valueobjects.ProductCategory
import com.fiap.stock.application.driver.web.request.ComponentRequest
import com.fiap.stock.application.driver.web.request.ProductRequest
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.response.Response
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.Matchers.hasSize
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class ProductCreation: IntegrationTest() {

    @Autowired
    lateinit var productRepository: ProductGateway

    @Autowired
    lateinit var componentRepository: ComponentGateway

    @Autowired
    lateinit var stockRepository: StockGateway

    private lateinit var productRequest: ProductRequest
    private lateinit var response: Response
    private lateinit var componentRequests: List<ComponentRequest>
    
    @Given("valid data for product")
    fun validDataForProduct() {
        componentRequests = listOf(
            ComponentRequest("Hambúrguer", 100),
            ComponentRequest("Alface", 100),
            ComponentRequest("Queijo", 100),
            ComponentRequest("Molho especial", 100),
            ComponentRequest("Cebola", 100),
            ComponentRequest("Picles", 100),
            ComponentRequest("Pão com gergelim", 100),
        )
        
        productRequest = createProductRequest(
            components = persistComponents()
        )
    }

    @When("request to create product")
    fun requestToCreateProduct() {
        response = given()
            .contentType(ContentType.JSON)
            .body(productRequest)
            .`when`()
            .post("/admin/products")
    }

    @Then("product should be created")
    fun productShouldBeCreated() {
        val persistedProduct = productRepository.findAll()[0]

        assertThat(persistedProduct).isEqualTo(Product(
            number = 1,
            name = productRequest.name,
            price = productRequest.price,
            description = productRequest.description,
            category = ProductCategory.fromString(productRequest.category),
            minSub = productRequest.minSub,
            maxSub = productRequest.maxSub,
            subItems = emptyList(),
            components = componentRequests.mapIndexed { index, request ->
                Component(number = index + 1L, name = request.name)
            },
        ))
        
        response
            .then()
            .statusCode(HttpStatus.OK.value())
            .body(
                "number", equalTo(productRepository.findAll()[0].number!!.toInt()),
                "name", equalTo(productRequest.name),
                "price", equalTo(productRequest.price.toString()),
                "description", equalTo(productRequest.description),
                "category", equalTo(productRequest.category),
                "minSub", equalTo(productRequest.minSub),
                "maxSub", equalTo(productRequest.maxSub),
                "subItems", hasSize<Int>(0),
                "components", hasSize<Int>(7),
            )
    }

    private fun persistComponents(): List<Long> {
        return componentRequests.map { componentRequest ->
            val savedComponent = componentRepository.create(Component(name = componentRequest.name))
            stockRepository.create(
                Stock(
                    componentNumber = savedComponent.number!!,
                    quantity = componentRequest.initialQuantity,
                ),
            )
            savedComponent
        }.map { it.number!! }
    }
}

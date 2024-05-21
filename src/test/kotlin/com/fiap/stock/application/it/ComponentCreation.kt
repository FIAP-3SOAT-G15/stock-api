package com.fiap.stock.application.it

import com.fiap.stock.application.adapter.gateway.ComponentGateway
import com.fiap.stock.application.adapter.gateway.StockGateway
import com.fiap.stock.application.domain.entities.Component
import com.fiap.stock.application.domain.entities.Stock
import com.fiap.stock.application.driver.web.request.ComponentRequest
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.response.Response
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.equalTo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class ComponentCreation: IntegrationTest() {
    
    @Autowired
    lateinit var componentRepository: ComponentGateway

    @Autowired
    lateinit var stockRepository: StockGateway

    private lateinit var componentRequest: ComponentRequest
    private lateinit var response: Response
    
    @Given("valid data for product component")
    fun validDataForProductComponent() {
        componentRequest = createComponentRequest()
    }
    
    @When("request to create product component")
    fun requestToCreateProductComponent() {
        response = given()
            .contentType(ContentType.JSON)
            .body(componentRequest)
            .`when`()
            .post("/admin/components")
    }

    @Then("product component should be created")
    fun productComponentShouldBeCreated() {
        val persistedProduct = componentRepository.findAll()[0]
        
        assertThat(persistedProduct).isEqualTo(Component(
            number = 1,
            name = componentRequest.name,
        ))
        
        response.then()
            .statusCode(HttpStatus.OK.value())
            .body(
                "number", equalTo(persistedProduct.number!!.toInt()),
                "name", equalTo(componentRequest.name),
            )
        
        val stock = stockRepository.findByComponentNumber(persistedProduct.number as Long)

        assertThat(stock).isEqualTo(Stock(
            componentNumber = persistedProduct.number as Long,
            quantity = componentRequest.initialQuantity,
        ))
    }
}

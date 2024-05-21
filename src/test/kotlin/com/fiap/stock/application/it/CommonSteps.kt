package com.fiap.stock.application.it

import io.cucumber.java.Before
import io.restassured.RestAssured
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.jdbc.core.JdbcTemplate

class CommonSteps: IntegrationTest() {
    
    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    @LocalServerPort
    private val port: Int? = null
    
    @Before
    fun setup() {
        RestAssured.baseURI = "http://localhost:$port"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

    @Before("@database")
    fun setupDatabase() {
        jdbcTemplate.execute("""
            TRUNCATE TABLE product RESTART IDENTITY CASCADE;
            TRUNCATE TABLE stock RESTART IDENTITY;
            TRUNCATE TABLE component RESTART IDENTITY CASCADE;
        """.trimIndent()
        )
    }
}

package com.fiap.stock.application

import com.fiap.stock.application.it.PostgreSQLContainerInitializer
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(initializers = [PostgreSQLContainerInitializer::class])
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
annotation class WithPostgreSQL

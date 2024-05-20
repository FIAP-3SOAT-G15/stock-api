package com.fiap.stock.application.driver.database.persistence.jpa

import com.fiap.stock.application.driver.database.persistence.entities.StockEntity
import org.springframework.data.repository.CrudRepository

interface StockJpaRepository : CrudRepository<StockEntity, Long>

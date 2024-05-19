package com.fiap.stock.application.driver.database.persistence.mapper

import com.fiap.stock.application.domain.entities.Component
import com.fiap.stock.application.driver.database.persistence.entities.ComponentEntity
import org.mapstruct.Mapper

@Mapper
interface ComponentMapper {
    fun toDomain(entity: ComponentEntity): Component

    fun toEntity(domain: Component): ComponentEntity
}

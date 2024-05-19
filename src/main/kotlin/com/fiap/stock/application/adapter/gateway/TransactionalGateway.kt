package com.fiap.stock.application.adapter.gateway

interface TransactionalGateway {
    fun <T> transaction(code: () -> T): T
}

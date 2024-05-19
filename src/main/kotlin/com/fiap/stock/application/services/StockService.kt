package services

import com.fiap.stock.application.adapter.gateway.StockGateway
import com.fiap.stock.domain.entities.Stock
import com.fiap.stock.domain.errors.ErrorType
import com.fiap.stock.domain.errors.SelfOrderManagementException
import com.fiap.stock.usecases.AdjustStockUseCase
import com.fiap.stock.usecases.LoadStockUseCase
import org.slf4j.LoggerFactory

class StockService(
    private val stockRepository: StockGateway,
) : LoadStockUseCase,
    AdjustStockUseCase {
    private val log = LoggerFactory.getLogger(javaClass)
    
    override fun getByComponentNumber(componentNumber: Long): Stock {
        return stockRepository.findByComponentNumber(componentNumber)
            ?: throw SelfOrderManagementException(
                errorType = ErrorType.STOCK_NOT_FOUND,
                message = "Stock not found for component [$componentNumber]",
            )
    }

    override fun increment(
        componentNumber: Long,
        quantity: Long,
    ): Stock {
        log.info("Incrementing component [$componentNumber] by +$quantity")
        val stock = getByComponentNumber(componentNumber)
        return stockRepository.update(stock.copy(quantity = stock.quantity + quantity))
    }

    override fun decrement(
        componentNumber: Long,
        quantity: Long,
    ): Stock {
        log.info("Decrementing component [$componentNumber] by -$quantity")
        val stock = getByComponentNumber(componentNumber)
        if (stock.hasSufficientInventory(quantity)) {
            throw SelfOrderManagementException(
                errorType = ErrorType.INSUFFICIENT_STOCK,
                message = "Insufficient stock for component $componentNumber",
            )
        }
        return stockRepository.update(stock.copy(quantity = stock.quantity - quantity))
    }
}

package domain.model

import java.math.BigDecimal
import java.time.LocalDate


data class Operation(val type: Type, val amount: BigDecimal, val date: LocalDate, val currency: String = "EUR") {
    enum class Type {
        DEPOSIT, WITHDRAWAL
    }
}


package domain.model

import java.math.BigDecimal
import java.time.LocalDateTime


data class Operation(
    val type: Type,
    var amount: BigDecimal,
    val date: LocalDateTime = LocalDateTime.now(),
    val balance: BigDecimal = BigDecimal.valueOf(0),
    val currency: String = "EUR"
) {
    enum class Type {
        DEPOSIT, WITHDRAWAL
    }
}


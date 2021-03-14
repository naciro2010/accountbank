package domain.model

import java.math.BigDecimal

data class CostumerAccount(
    val identify: String,
    val firstName: String,
    val lastName: String,
    var balance: BigDecimal,
    val currency: String = "EUR"
)
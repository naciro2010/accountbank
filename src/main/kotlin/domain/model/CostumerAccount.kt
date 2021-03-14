package domain.model

import java.math.BigDecimal

data class CostumerAccount(
    val identify: String,
    val firstName: String,
    val lastName: String,
    var balance: BigDecimal = BigDecimal.valueOf(0) ,
    val currency: String = "EUR"
)
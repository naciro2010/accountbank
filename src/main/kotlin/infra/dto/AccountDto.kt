package infra.dto

import domain.model.Costumer
import java.math.BigDecimal

data class AccountDto(val identify: String, val firstName: String, val lastName: String, val balance: BigDecimal)

fun AccountDto.toDomain() = Costumer(identify, firstName, lastName, balance)
fun Costumer.toInfra (balance: BigDecimal) = AccountDto(identify, firstName, lastName, balance)



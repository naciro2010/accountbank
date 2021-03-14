package infra.dto

import domain.model.CostumerAccount
import java.math.BigDecimal

data class AccountDto(val identify: String, val firstName: String, val lastName: String, val balance: BigDecimal)

fun AccountDto.toDomain() = CostumerAccount(identify, firstName, lastName, balance)
fun CostumerAccount.toInfra (balance: BigDecimal) = AccountDto(identify, firstName, lastName, balance)



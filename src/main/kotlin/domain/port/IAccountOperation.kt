package domain.port

import domain.model.CostumerAccount
import domain.model.Operation
import infra.dto.OperationDto
import java.math.BigDecimal

interface IAccountOperation {
    fun getOperationsByAccountId(identify: String): List<Operation>
    fun getAccountByAccountId(accountId: String): CostumerAccount
    fun addOperationToHistory(account: CostumerAccount, operation: Operation) : List<OperationDto>
    fun updateAccount(account: CostumerAccount, balance: BigDecimal) : CostumerAccount

}
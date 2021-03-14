package domain.port

import domain.model.CostumerAccount
import domain.model.Operation
import java.math.BigDecimal

interface IAccountOperation {
    fun getOperationsByAccountId(identify: String): List<Operation>
    fun getAccount(accountId: String): CostumerAccount
    fun addOperationToHistory(accountId: String, operation: Operation)
    fun updateAccount(account: CostumerAccount, balance: BigDecimal) : CostumerAccount

}
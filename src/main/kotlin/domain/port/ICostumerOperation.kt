package domain.port

import domain.model.Costumer
import domain.model.Operation
import infra.dto.OperationDto
import java.math.BigDecimal

interface ICostumerOperation {
    fun getOperationsByAccountIdentify(accountIdentify: String): List<Operation>
    fun getCostumerByAccountIdentify(accountIdentify: String): Costumer
    fun addOperationToHistory(account: Costumer, operation: Operation) : List<Operation>
    fun updateAccount(account: Costumer, balance: BigDecimal) : Costumer
}
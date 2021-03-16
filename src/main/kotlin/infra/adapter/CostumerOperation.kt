package infra.adapter

import domain.model.Costumer
import domain.model.Operation
import domain.port.ICostumerOperation
import infra.adapter.datasource.AccountDataSource
import infra.dto.toDomain
import infra.dto.toInfra
import infra.exception.NoAccountFoundException
import infra.exception.NoHistoryException
import infra.exception.NoOperationFoundException
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class CostumerOperation(var accountDataSource: AccountDataSource = AccountDataSource()) : ICostumerOperation {


    override fun getOperationsByAccountIdentify(acountIdentify: String): List<Operation> {
        return accountDataSource.operations[acountIdentify]
            ?.toDomain()
            ?: throw NoOperationFoundException(
                accountId = acountIdentify
            )
    }

    override fun getCostumerByAccountIdentify(accountIdentify: String): Costumer {
        return accountDataSource.accounts[accountIdentify]
            ?.let { it.toDomain() }
            ?: throw NoAccountFoundException(
                accountId = accountIdentify
            )
    }

    override fun addOperationToHistory(costumer: Costumer, operation: Operation): List<Operation> {
         if (Operation.Type.WITHDRAWAL == operation.type) {
              operation.amount = operation.amount.negate()
          }
        val operationDto = operation.toInfra(operation.type.toString(), costumer.balance)

        accountDataSource.operations[costumer.identify] =
            accountDataSource.operations[costumer.identify]
                ?.let { it + listOf(operationDto) } ?: listOf(operationDto)

        return accountDataSource.operations[costumer.identify]
            ?.toDomain()
            ?: throw NoHistoryException(accountId = costumer.identify)
    }

    override fun updateAccount(costumer: Costumer, balance: BigDecimal): Costumer {
        accountDataSource.accounts[costumer.identify] = costumer.toInfra(balance)
        return accountDataSource.accounts[costumer.identify]
            ?.toDomain()
            ?: throw NoAccountFoundException(accountId = costumer.identify)
    }


}



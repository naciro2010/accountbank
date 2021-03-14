package infra.adapter

import domain.model.CostumerAccount
import domain.model.Operation
import domain.port.IAccountOperation
import infra.adapter.datasource.AccountDataSource
import infra.dto.OperationDto
import infra.dto.toDomain
import infra.dto.toInfra
import infra.exception.NoAccountFoundException
import infra.exception.NoHistoryException
import infra.exception.NoOperationFoundException
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class AccountOperation(var accountDataSource: AccountDataSource = AccountDataSource()) : IAccountOperation {


    override fun getOperationsByAccountId(accountId: String): List<Operation> {
        return accountDataSource.operations[accountId]?.map { it.toDomain() } ?: throw NoOperationFoundException(
            accountId = accountId
        )
    }

    override fun getAccountByAccountId(accountId: String): CostumerAccount {
        return accountDataSource.accounts[accountId]?.let { it.toDomain() } ?: throw NoAccountFoundException(
            accountId = accountId
        )
    }

    override fun addOperationToHistory(accountId: String, operation: Operation): List<OperationDto> {
        if (Operation.Type.WITHDRAWAL == operation.type)
            operation.amount = operation.amount.negate()

        val operationDto = operation.toInfra(operation.type.toString())

        accountDataSource.operations[accountId] =
            accountDataSource.operations[accountId]?.let { it + listOf(operationDto) } ?: listOf(operationDto)

        return accountDataSource.operations[accountId] ?: throw NoHistoryException(accountId = accountId)
    }

    override fun updateAccount(account: CostumerAccount, balance: BigDecimal): CostumerAccount {
        accountDataSource.accounts[account.identify] = account.toInfra(balance)
        return accountDataSource.accounts[account.identify]?.toDomain() ?: throw NoAccountFoundException(
            accountId = account.identify
        )
    }


}



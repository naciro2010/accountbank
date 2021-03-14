package infra.adapter

import domain.model.CostumerAccount
import domain.model.Operation
import domain.port.IAccountOperation
import infra.adapter.datasource.AccountDataSource
import infra.dto.toDomain
import infra.dto.toInfra
import infra.exception.NoAccountFoundException
import infra.exception.NoOperationFoundException
import java.math.BigDecimal

class AccountOperation : IAccountOperation {
    private val accountDataSource: AccountDataSource = AccountDataSource()

    override fun getOperationsByAccountId(accountId: String): List<Operation> {
        return accountDataSource.operations[accountId]?.map { it.toDomain() } ?: throw NoOperationFoundException(
            accountId = accountId
        )
    }

    override fun getAccount(accountId: String): CostumerAccount {
        return accountDataSource.accounts[accountId]?.let { it.toDomain() } ?: throw NoAccountFoundException(
            accountId = accountId
        )
    }

    override fun addOperationToHistory(accountId: String, operation: Operation) {
        accountDataSource.operations[accountId] = listOf(operation.toInfra(operation.type.toString()))
    }

    override fun updateAccount(account: CostumerAccount, balance: BigDecimal): CostumerAccount {
        accountDataSource.accounts[account.identify] = account.toInfra(balance)
        return accountDataSource.accounts[account.identify]?.toDomain() ?: throw NoAccountFoundException(
            accountId = account.identify
        )
    }


}
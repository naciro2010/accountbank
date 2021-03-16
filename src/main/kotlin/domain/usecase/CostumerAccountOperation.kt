package domain.usecase

import domain.exception.Operation_no_authorization
import domain.model.CostumerAccount
import domain.model.Operation
import domain.port.IAccountOperation
import domain.port.ICostumerAccount
import org.springframework.stereotype.Component

@Component
class CostumerAccountOperation(private val accountInfra: IAccountOperation) : ICostumerAccount {

    override fun deposit(operation: Operation, costumerAccount: CostumerAccount): CostumerAccount {
        return accountInfra.getAccountByAccountId(costumerAccount.identify).let {
            accountInfra.updateAccount(it, it.balance.add(operation.amount))
        }.also {
            accountInfra.addOperationToHistory(it, operation)
        }.let { it }
    }

    override fun withdraw(operation: Operation, costumerAccount: CostumerAccount): CostumerAccount {

        return accountInfra.getAccountByAccountId(costumerAccount.identify).also {
            isOperationAuthorized(it, operation)
        }
            .let {
                accountInfra.updateAccount(it, it.balance.subtract(operation.amount))
            }.also {
                accountInfra.addOperationToHistory(it, operation)
            }.let { it }
    }


    override fun showHistory(costumerAccount: CostumerAccount) =
        accountInfra.getOperationsByAccountId(costumerAccount.identify)


    private fun isOperationAuthorized(
        costumerAccount: CostumerAccount,
        operation: Operation
    ) {
        if (costumerAccount.balance < operation.amount || Operation.Type.WITHDRAWAL != operation.type)
            throw Operation_no_authorization(
                costumerAccount.identify,
                operation.type.toString(),
                operation.amount
            )
    }


}
package domain.usecase

import domain.exception.Operation_no_authorization
import domain.model.BankAccount
import domain.model.Costumer
import domain.model.Operation
import domain.port.IBankAccount
import domain.port.ICostumerOperation
import org.springframework.stereotype.Component

@Component
class BankAccountUseCase(private val costumerInfra: ICostumerOperation) : IBankAccount {

    override fun deposit(operation: Operation, costumer: Costumer): BankAccount {
        return costumerInfra.getCostumerByAccountIdentify(costumer.identify)
            .let {
                costumerInfra.updateAccount(it, it.balance.add(operation.amount))
            }.let {
                BankAccount(it, costumerInfra.addOperationToHistory(it, operation))
            }
    }

    override fun withdraw(operation: Operation, costumer: Costumer): BankAccount {

        return costumerInfra.getCostumerByAccountIdentify(costumer.identify)
            .also {
                isOperationAuthorized(it, operation)
            }.let {
                costumerInfra.updateAccount(it, it.balance.subtract(operation.amount))
            }.let {
                BankAccount(it, costumerInfra.addOperationToHistory(it, operation))
            }
    }

    override fun showHistory(costumer: Costumer) =
        costumerInfra.getOperationsByAccountIdentify(costumer.identify)


    private fun isOperationAuthorized(
        costumer: Costumer,
        operation: Operation
    ) {
        if (costumer.balance < operation.amount || Operation.Type.WITHDRAWAL != operation.type)
            throw Operation_no_authorization(
                costumer.identify,
                operation.type.toString(),
                operation.amount
            )
    }


}
package domain.port

import domain.model.CostumerAccount
import domain.model.Operation

interface ICostumerAccount {
    fun deposit(operation: Operation, costumerAccount: CostumerAccount): CostumerAccount
    fun withdraw(operation: Operation, costumerAccount: CostumerAccount): CostumerAccount
    fun showHistory(costumerAccount: CostumerAccount): List<Operation>
}



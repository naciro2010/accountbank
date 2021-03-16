package domain.port

import domain.model.BankAccount
import domain.model.Costumer
import domain.model.Operation

interface IBankAccount {
    fun deposit(operation: Operation, costumer: Costumer): BankAccount
    fun withdraw(operation: Operation, costumer: Costumer): BankAccount
    fun showHistory(costumer: Costumer): List<Operation>
}



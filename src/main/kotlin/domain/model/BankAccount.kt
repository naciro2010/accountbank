package domain.model

data class BankAccount(
    val costumer: Costumer,
    val operations: List<Operation>
)
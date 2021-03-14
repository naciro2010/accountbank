package domain.exception

open class AccountBankException(
    override val message: String,
    override val cause: Throwable? = null
) : RuntimeException(message, cause)

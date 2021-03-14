package infra.exception

import domain.exception.AccountBankException

class NoAccountFoundException(
    accountId: String,
    cause: Throwable? = null
) : AccountBankException("Account with id '$accountId' not found", cause)

class NoOperationFoundException(
    accountId: String,
    cause: Throwable? = null
) : AccountBankException("no Operation found for Account id '$accountId'", cause)

class NoHistoryException(
    accountId: String,
    cause: Throwable? = null
) : AccountBankException("history operation  not executed for Account id '$accountId'", cause)


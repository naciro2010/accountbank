package domain.exception

import java.math.BigDecimal

class Operation_no_authorization(
    accountId: String,
    operationType: String,
    amount: BigDecimal
) : AccountBankException("unauthorized operation for the client '$accountId' for type '$operationType' for amount '$amount' ")

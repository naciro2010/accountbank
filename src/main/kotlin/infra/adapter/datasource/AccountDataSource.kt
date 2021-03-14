package infra.adapter.datasource

import infra.dto.AccountDto
import infra.dto.OperationDto
import java.math.BigDecimal

class AccountDataSource {

    var accounts: HashMap<String, AccountDto> = HashMap()
    var operations: HashMap<String, List<OperationDto>> = HashMap()

    init {
        accounts["936a0787-7a00-46b3-af4d-6ab55d22cd6a"] =
            AccountDto("936a0787-7a00-46b3-af4d-6ab55d22cd6a", "firstName", "LastName", BigDecimal.valueOf(0))
    }
}


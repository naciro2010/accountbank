package infra.adapter.datasource

import infra.dto.AccountDto
import infra.dto.OperationDto

class AccountDataSource {

    val accounts : HashMap<String, AccountDto> = HashMap()
    val operations : HashMap<String, List<OperationDto>> = HashMap()

}
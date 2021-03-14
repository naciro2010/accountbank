package infra.dto

import domain.model.Operation
import java.math.BigDecimal
import java.time.LocalDate

class OperationDto(val type: String, val amount: BigDecimal, val date: LocalDate) {
}
fun OperationDto.toDomain() = Operation(Operation.Type.valueOf(type), amount, date)
fun Operation.toInfra(type : String) = OperationDto(type, amount, date)
package infra.dto

import domain.model.Operation
import java.math.BigDecimal
import java.time.LocalDateTime

data class OperationDto(val type: String, val amount: BigDecimal, val balance: BigDecimal, val date: LocalDateTime)

fun OperationDto.toDomain() = Operation(Operation.Type.valueOf(type), amount, balance = balance, date = date)
fun Operation.toInfra(type: String, balance: BigDecimal) = OperationDto(type, amount, balance, date)
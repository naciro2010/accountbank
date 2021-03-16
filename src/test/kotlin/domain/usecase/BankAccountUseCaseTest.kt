package domain.usecase

import domain.exception.Operation_no_authorization
import domain.model.BankAccount
import domain.model.Costumer
import domain.model.Operation
import infra.adapter.CostumerOperation
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Month
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class BankAccountUseCaseTest {

    @Mock
    private lateinit var accountInfra: CostumerOperation

    @InjectMocks
    private lateinit var useCase: BankAccountUseCase


    private val now = LocalDateTime.of(
        LocalDate.of(2021, Month.FEBRUARY, 2),
        LocalTime.of(17, 9)
    )

    private val identifyAccount = UUID.randomUUID().toString()


    private fun costumerAccount(
        identify: String = identifyAccount,
        balance: BigDecimal? = null
    ) = Costumer(
        identify, "firstName",
        " lastName", balance?.let { balance } ?: BigDecimal.valueOf(100)
    )

    @Test
    fun add_a_deposit_operation() {

        given(accountInfra.getCostumerByAccountIdentify(identifyAccount)).willReturn(
            costumerAccount()
        )
        given(accountInfra.updateAccount(costumerAccount(), BigDecimal.valueOf(200))).willReturn(
            Costumer(
                identifyAccount,
                "firstName",
                " lastName",
                BigDecimal.valueOf(200)
            )
        )

        val operationExpected = Operation(
            Operation.Type.DEPOSIT,
            BigDecimal.valueOf(100),
            now
        )
        assertEquals(
            useCase.deposit(
                operationExpected, costumerAccount()
            ), BankAccount(
                costumerAccount(balance = BigDecimal.valueOf(200)), listOf(operationExpected)
            )
        )
        // THEN
    }


    @Test
    fun add_a_withdraw_operation() {

        given(accountInfra.getCostumerByAccountIdentify(identifyAccount)).willReturn(
            costumerAccount()
        )
        given(accountInfra.updateAccount(costumerAccount(), BigDecimal.valueOf(0))).willReturn(
            Costumer(
                identifyAccount,
                "firstName",
                " lastName",
                BigDecimal.valueOf(0)
            )
        )
        given(
            accountInfra.addOperationToHistory(
                costumerAccount(), Operation(
                    Operation.Type.WITHDRAWAL,
                    BigDecimal.valueOf(100),
                    now
                )
            )
        ).willReturn(
            listOf(
                Operation(
                    type = Operation.Type.WITHDRAWAL,
                    amount = BigDecimal.valueOf(100).negate(),
                    balance = BigDecimal.valueOf(0),
                    date = now
                )
            )
        )

        val operation = Operation(
            Operation.Type.WITHDRAWAL,
            BigDecimal.valueOf(100),
            now
        )
        assertEquals(
            useCase.withdraw(
                operation, costumerAccount()
            ),
            BankAccount(costumerAccount(balance = BigDecimal.valueOf(0)), listOf(operation))
        )
    }

    @Test
    fun add_a_withdraw_operation_throw_exception_Operation_no_authorization() {

        given(accountInfra.getCostumerByAccountIdentify(identifyAccount)).willReturn(
            costumerAccount()
        )

        assertThatThrownBy {
            useCase.withdraw(
                Operation(
                    Operation.Type.WITHDRAWAL,
                    BigDecimal.valueOf(200),
                    now
                ), costumerAccount()
            )
        }.isInstanceOf(Operation_no_authorization::class.java)
    }

    @Test
    fun print_statement_operations() {

        given(accountInfra.getOperationsByAccountIdentify(identifyAccount)).willReturn(
            listOf(
                Operation(
                    Operation.Type.WITHDRAWAL,
                    BigDecimal.valueOf(100).negate(),
                    now
                ),
                Operation(
                    Operation.Type.DEPOSIT,
                    BigDecimal.valueOf(100),
                    now
                )
            )
        )

        assertEquals(
            useCase.showHistory(costumerAccount()), listOf(
                Operation(
                    Operation.Type.WITHDRAWAL,
                    BigDecimal.valueOf(100).negate(),
                    now
                ),
                Operation(
                    Operation.Type.DEPOSIT,
                    BigDecimal.valueOf(100),
                    now
                )
            )
        )
    }
}
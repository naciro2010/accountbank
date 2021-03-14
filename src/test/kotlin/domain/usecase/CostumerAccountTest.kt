package domain.usecase

import domain.exception.Operation_no_authorization
import domain.model.CostumerAccount
import domain.model.Operation
import infra.adapter.AccountOperation
import infra.dto.OperationDto
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner;
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Month
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class CostumerAccountTest {

    @Mock
    private lateinit var accountInfra: AccountOperation

    @InjectMocks
    private lateinit var useCase: CostumerAccountOperation


    private val now = LocalDateTime.of(
        LocalDate.of(2021, Month.FEBRUARY, 2),
        LocalTime.of(17, 9)
    )

    private val identifyAccount = UUID.randomUUID().toString()

    private fun initCostumerAccount(balance: BigDecimal? = null) = CostumerAccount(
        identifyAccount,
        "firstName",
        " lastName",
        balance?.let { balance } ?: BigDecimal.valueOf(100)
    )


    @Test
    fun add_a_deposit_operation() {

        given(accountInfra.getAccountByAccountId(identifyAccount)).willReturn(
            initCostumerAccount()
        )
        given(accountInfra.updateAccount(initCostumerAccount(), BigDecimal.valueOf(200))).willReturn(
            CostumerAccount(
                identifyAccount,
                "firstName",
                " lastName",
                BigDecimal.valueOf(200)
            )
        )

        assertEquals(
            useCase.deposit(
                Operation(
                    Operation.Type.DEPOSIT,
                    BigDecimal.valueOf(100),
                    now
                ), initCostumerAccount()
            ), initCostumerAccount(BigDecimal.valueOf(200))
        )


        // THEN
    }


    @Test
    fun add_a_withdraw_operation() {

        given(accountInfra.getAccountByAccountId(identifyAccount)).willReturn(
            initCostumerAccount()
        )
        given(accountInfra.updateAccount(initCostumerAccount(), BigDecimal.valueOf(0))).willReturn(
            CostumerAccount(
                identifyAccount,
                "firstName",
                " lastName",
                BigDecimal.valueOf(0)
            )
        )

        given(
            accountInfra.addOperationToHistory(
                identifyAccount, Operation(
                    Operation.Type.WITHDRAWAL,
                    BigDecimal.valueOf(100),
                    now
                )
            )
        ).willReturn(
            listOf(
                OperationDto(
                    Operation.Type.WITHDRAWAL.toString(),
                    BigDecimal.valueOf(100).negate(),
                    now
                )
            )
        )

        assertEquals(
            useCase.withdraw(
                Operation(
                    Operation.Type.WITHDRAWAL,
                    BigDecimal.valueOf(100),
                    now
                ), initCostumerAccount()
            ), initCostumerAccount(BigDecimal.valueOf(0))
        )
    }

    @Test
    fun add_a_withdraw_operation_throw_exception_Operation_no_authorization() {

        given(accountInfra.getAccountByAccountId(identifyAccount)).willReturn(
            initCostumerAccount()
        )

        assertThatThrownBy {
            useCase.withdraw(
                Operation(
                    Operation.Type.WITHDRAWAL,
                    BigDecimal.valueOf(200),
                    now
                ), initCostumerAccount()
            )
        }.isInstanceOf(Operation_no_authorization::class.java)
    }

    @Test
    fun print_statement_operations() {

        given(accountInfra.getOperationsByAccountId(identifyAccount)).willReturn(
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
            useCase.showHistory(initCostumerAccount()), listOf(
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
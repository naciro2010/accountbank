package infra.adapter

import domain.model.CostumerAccount
import domain.model.Operation
import infra.dto.AccountDto
import infra.dto.OperationDto
import infra.exception.NoAccountFoundException
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.junit.MockitoJUnitRunner
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Month
import java.util.*
import kotlin.test.assertEquals

@RunWith(MockitoJUnitRunner::class)

class AccountOperationTest {
    @InjectMocks
    private lateinit var adapter: AccountOperation


    private val now = LocalDateTime.of(
        LocalDate.of(2021, Month.FEBRUARY, 2),
        LocalTime.of(17, 9)
    )

    private val identifyAccount = UUID.randomUUID().toString()

    @Test
    fun testGetOperationsByAccountId() {


        adapter.addOperationToHistory(
            UUID.randomUUID().toString(), Operation(
                Operation.Type.DEPOSIT,
                BigDecimal.valueOf(405),
                now
            )
        )

        adapter.addOperationToHistory(
            UUID.randomUUID().toString(), Operation(
                Operation.Type.DEPOSIT,
                BigDecimal.valueOf(100),
                now
            )
        )
        adapter.addOperationToHistory(
            identifyAccount, Operation(
                Operation.Type.DEPOSIT,
                BigDecimal.valueOf(200),
                now
            )
        )
        adapter.addOperationToHistory(
            identifyAccount, Operation(
                Operation.Type.DEPOSIT,
                BigDecimal.valueOf(300),
                now
            )
        )
        adapter.addOperationToHistory(
            identifyAccount, Operation(
                Operation.Type.WITHDRAWAL,
                BigDecimal.valueOf(200),
                now
            )
        )
        assertThat(adapter.getOperationsByAccountId(identifyAccount)).usingRecursiveComparison().isEqualTo(
            listOf(
                Operation(
                    Operation.Type.DEPOSIT,
                    BigDecimal.valueOf(200),
                    now
                ),
                Operation(
                    Operation.Type.DEPOSIT,
                    BigDecimal.valueOf(300),
                    now
                ),
                Operation(
                    Operation.Type.WITHDRAWAL,
                    BigDecimal.valueOf(200).negate(),
                    now
                )
            )
        )
    }

    @Test
    fun get_a_account_by_identify() {
        val accountDto = AccountDto(identifyAccount, "firstName", "lastName", BigDecimal.valueOf(100))
        adapter.accountDataSource.accounts[identifyAccount] = accountDto

        assertEquals(
            adapter.getAccountByAccountId(identifyAccount),
            CostumerAccount(identifyAccount, "firstName", "lastName", BigDecimal.valueOf(100))
        )

    }

    @Test
    fun get_a_account_by_not_exist_identify_should_throw_exception() {
        val accountDto = AccountDto(identifyAccount, "firstName", "lastName", BigDecimal.valueOf(100))
        adapter.accountDataSource.accounts[identifyAccount] = accountDto


        Assertions.assertThatThrownBy {
            adapter.getAccountByAccountId(UUID.randomUUID().toString())
        }.isInstanceOf(NoAccountFoundException::class.java)

    }

    @Test
    fun add_a_withdraw_operation_to_history() {

        assertThat(
            adapter.addOperationToHistory(
                identifyAccount, Operation(
                    Operation.Type.WITHDRAWAL,
                    BigDecimal.valueOf(200),
                    now
                )
            )
        ).usingRecursiveComparison().isEqualTo(
            listOf(
                OperationDto(
                    Operation.Type.WITHDRAWAL.toString(),
                    BigDecimal.valueOf(200).negate(),
                    now
                )
            )
        )


    }

    @Test
    fun add_a_deposit_operation_to_history() {

        assertThat(
            adapter.addOperationToHistory(
                identifyAccount, Operation(
                    Operation.Type.DEPOSIT,
                    BigDecimal.valueOf(200),
                    now
                )
            )
        ).usingRecursiveComparison().isEqualTo(
            listOf(
                OperationDto(
                    Operation.Type.DEPOSIT.toString(),
                    BigDecimal.valueOf(200),
                    now
                )
            )
        )

    }


    @Test
    fun add_a_deposit_withdrawal_operation_to_history() {
        adapter.addOperationToHistory(
            identifyAccount, Operation(
                Operation.Type.DEPOSIT,
                BigDecimal.valueOf(200),
                now
            )
        )
        adapter.addOperationToHistory(
            identifyAccount, Operation(
                Operation.Type.DEPOSIT,
                BigDecimal.valueOf(300),
                now
            )
        )
        adapter.addOperationToHistory(
            identifyAccount, Operation(
                Operation.Type.WITHDRAWAL,
                BigDecimal.valueOf(200),
                now
            )
        )
        assertThat(
            adapter.accountDataSource.operations[identifyAccount]
        ).usingRecursiveComparison().isEqualTo(
            listOf(
                OperationDto(
                    Operation.Type.DEPOSIT.toString(),
                    BigDecimal.valueOf(200),
                    now
                ),
                OperationDto(
                    Operation.Type.DEPOSIT.toString(),
                    BigDecimal.valueOf(300),
                    now
                ),
                OperationDto(
                    Operation.Type.WITHDRAWAL.toString(),
                    BigDecimal.valueOf(200).negate(),
                    now
                )
            )
        )

    }
}
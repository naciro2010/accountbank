package infra.adapter

import domain.model.Costumer
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
    private lateinit var adapter: CostumerOperation


    private val now = LocalDateTime.of(
        LocalDate.of(2021, Month.FEBRUARY, 2),
        LocalTime.of(17, 9)
    )

    private val identifyAccount = UUID.randomUUID().toString()

    private fun costumerAccount(
        identify: String,
        balance: BigDecimal = BigDecimal.valueOf(0)
    ) = Costumer(identify, "", "", balance)

    @Test
    fun testGetOperationsByAccountId() {

        adapter.addOperationToHistory(
            costumerAccount(UUID.randomUUID().toString(), BigDecimal.valueOf(405)), Operation(
                Operation.Type.DEPOSIT,
                BigDecimal.valueOf(405),
                now
            )
        )

        adapter.addOperationToHistory(
            costumerAccount(UUID.randomUUID().toString(), BigDecimal.valueOf(100)), Operation(
                Operation.Type.DEPOSIT,
                BigDecimal.valueOf(100),
                now
            )
        )
        adapter.addOperationToHistory(
            costumerAccount(identifyAccount, BigDecimal.valueOf(200)), Operation(
                Operation.Type.DEPOSIT,
                BigDecimal.valueOf(200),
                now
            )
        )
        adapter.addOperationToHistory(
            costumerAccount(identifyAccount, BigDecimal.valueOf(500)), Operation(
                Operation.Type.DEPOSIT,
                BigDecimal.valueOf(300),
                now
            )
        )
        adapter.addOperationToHistory(
            costumerAccount(identifyAccount, BigDecimal.valueOf(300)), Operation(
                Operation.Type.WITHDRAWAL,
                BigDecimal.valueOf(200),
                now
            )
        )
        assertThat(adapter.getOperationsByAccountIdentify(identifyAccount)).usingRecursiveComparison().isEqualTo(
            listOf(
                Operation(
                    Operation.Type.DEPOSIT,
                    BigDecimal.valueOf(200),
                    now,
                    BigDecimal.valueOf(200)
                ),
                Operation(
                    Operation.Type.DEPOSIT,
                    BigDecimal.valueOf(300),
                    now,
                    BigDecimal.valueOf(500)
                ),
                Operation(
                    Operation.Type.WITHDRAWAL,
                    BigDecimal.valueOf(200).negate(),
                    now,
                    BigDecimal.valueOf(300)
                )
            )
        )
    }

    @Test
    fun get_a_account_by_identify() {
        val accountDto = AccountDto(identifyAccount, "firstName", "lastName", BigDecimal.valueOf(100))
        adapter.accountDataSource.accounts[identifyAccount] = accountDto

        assertEquals(
            adapter.getCostumerByAccountIdentify(identifyAccount),
            Costumer(identifyAccount, "firstName", "lastName", BigDecimal.valueOf(100))
        )

    }

    @Test
    fun get_a_account_by_not_exist_identify_should_throw_exception() {
        val accountDto = AccountDto(identifyAccount, "firstName", "lastName", BigDecimal.valueOf(100))
        adapter.accountDataSource.accounts[identifyAccount] = accountDto


        Assertions.assertThatThrownBy {
            adapter.getCostumerByAccountIdentify(UUID.randomUUID().toString())
        }.isInstanceOf(NoAccountFoundException::class.java)

    }

    @Test
    fun add_a_withdraw_operation_to_history() {

        assertThat(
            adapter.addOperationToHistory(
                costumerAccount(identifyAccount, BigDecimal.valueOf(0)), Operation(
                    Operation.Type.WITHDRAWAL,
                    BigDecimal.valueOf(200),
                    now
                )
            )
        ).usingRecursiveComparison().isEqualTo(
            listOf(
                Operation(
                    type = Operation.Type.WITHDRAWAL,
                    amount = BigDecimal.valueOf(200).negate(),
                    balance = BigDecimal.valueOf(0),
                    date = now
                )
            )
        )


    }

    @Test
    fun add_a_deposit_operation_to_history() {

        assertThat(
            adapter.addOperationToHistory(
                Costumer(identifyAccount, "", "", BigDecimal.valueOf(200)), Operation(
                    Operation.Type.DEPOSIT,
                    BigDecimal.valueOf(200),
                    now
                )
            )
        ).usingRecursiveComparison().isEqualTo(
            listOf(
                Operation(
                    type = Operation.Type.DEPOSIT,
                    amount = BigDecimal.valueOf(200),
                    balance = BigDecimal.valueOf(200),
                    date = now
                )
            )
        )

    }


    @Test
    fun add_a_deposit_withdrawal_operation_to_history() {
        adapter.addOperationToHistory(
            costumerAccount(identifyAccount, BigDecimal.valueOf(200)), Operation(
                Operation.Type.DEPOSIT,
                BigDecimal.valueOf(200),
                now
            )
        )
        adapter.addOperationToHistory(
            costumerAccount(identifyAccount, BigDecimal.valueOf(500)), Operation(
                Operation.Type.DEPOSIT,
                BigDecimal.valueOf(300),
                now
            )
        )
        adapter.addOperationToHistory(
            costumerAccount(identifyAccount, BigDecimal.valueOf(350)), Operation(
                Operation.Type.WITHDRAWAL,
                BigDecimal.valueOf(150),
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
                    BigDecimal.valueOf(200),
                    now
                ),
                OperationDto(
                    Operation.Type.DEPOSIT.toString(),
                    BigDecimal.valueOf(300),
                    BigDecimal.valueOf(500),
                    now
                ),
                OperationDto(
                    Operation.Type.WITHDRAWAL.toString(),
                    BigDecimal.valueOf(150).negate(),
                    BigDecimal.valueOf(350),
                    now
                )
            )
        )

    }
}
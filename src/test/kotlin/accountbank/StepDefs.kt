package accountbank

import domain.model.Costumer
import domain.model.Operation
import domain.usecase.BankAccountUseCase
import infra.adapter.CostumerOperation
import io.cucumber.datatable.DataTable
import io.cucumber.java.Before
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import junit.framework.Assert.assertEquals
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Month

class StepDefs {

    private lateinit var costumerAccount: BankAccountUseCase

    private val identifyAccount = "936a0787-7a00-46b3-af4d-6ab55d22cd6a"
    private lateinit var balanceAnswer: BigDecimal
    private lateinit var listOperation: List<Operation>

    private val now = LocalDateTime.of(
        LocalDate.of(2021, Month.FEBRUARY, 2),
        LocalTime.of(17, 9)
    )


    @Before
    fun setUp() {
        val accountOperation = CostumerOperation()
        costumerAccount = BankAccountUseCase(accountOperation)
    }

    @Given("I have an empty account")
    @Throws(Exception::class)
    fun i_have_an_empty_account() {
        costumerAccount.deposit(
            Operation(Operation.Type.DEPOSIT, BigDecimal.valueOf(0), now),
            Costumer(identifyAccount, "firstName", "LastName")
        )
    }


    @Given("I deposit (\\d+) euros")
    @Throws(Exception::class)
    fun i_deposit_amount_euros(amount: BigDecimal) {
        balanceAnswer = costumerAccount.deposit(
            Operation(Operation.Type.DEPOSIT, amount, now),
            Costumer(identifyAccount, "firstName", "LastName")
        ).costumer.balance
    }

    @Given("I withdraw (\\d+) euros")
    @Throws(Exception::class)
    fun i_withdraw_amount_euros(amount: BigDecimal) {
        balanceAnswer = costumerAccount.withdraw(
            Operation(Operation.Type.WITHDRAWAL,amount, now),
            Costumer(identifyAccount, "firstName", "LastName")
        ).costumer.balance
    }

    @When("I ask for my balance")
    @Throws(Exception::class)
    fun i_ask_for_my_balance(): BigDecimal {
        return balanceAnswer
    }

    @Then("My balance should be (\\d+) Euros$")
    @Throws(Exception::class)
    fun my_balance_should_be_amount_euros(expectedAnswer: BigDecimal) {
        assertEquals(expectedAnswer, balanceAnswer)
    }

    @When("I show my history")
    fun i_show_my_history() {
        listOperation = costumerAccount.showHistory(Costumer(identifyAccount, "firstName", "LastName"))
    }

    @Then("I should see")
    fun i_should_see(dataTable: DataTable?) {

    }
}
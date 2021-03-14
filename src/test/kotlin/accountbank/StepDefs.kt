package accountbank

import io.cucumber.java.PendingException
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import java.math.BigDecimal

class StepDefs {


    @Given("I have an empty account")
    @Throws(Exception::class)
    fun i_have_an_empty_account() {
        throw PendingException()
    }


    @Given("I deposit (\\d+) Euros")
    @Throws(Exception::class)
    fun i_deposit_amount_euros(amount: BigDecimal) {
        throw PendingException()
    }

    @Given("I withdraw (\\d+) Euros")
    @Throws(Exception::class)
    fun i_withdraw_amount_euros(amount: BigDecimal) {
        throw PendingException()
    }

    @When("I ask for the statement")
    @Throws(Exception::class)
    fun i_ask_for_the_statement() {
        throw PendingException()
    }

    @Then("My balance should be (\\d+) Euros$")
    @Throws(Exception::class)
    fun my_balance_should_be_amount_euros(expectedAnswer: BigDecimal) {
        throw PendingException()
    }
}
import io.cucumber.java.PendingException
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When

class ProductAndPriceInputStepsDefinition {

    @Given("I am at the price input screen")
    fun iAmAtThePriceInputScreen() {
        // Write code here that turns the phrase above into concrete actions
        throw PendingException()
    }

    @When("I enter a new Product called {string} with Unit {string}")
    fun iEnterANewProductCalledWithUnit(productName: String, unitFormat: String) {
        // Write code here that turns the phrase above into concrete actions
        throw PendingException()
    }

    @And("I enter a price of {string}")
    fun iEnterAPriceOf(price: Long) {
        // Write code here that turns the phrase above into concrete actions
        throw PendingException()
    }

    @And("I enter the store as {string}")
    fun iEnterTheStoreAs(storeName: String) {
        // Write code here that turns the phrase above into concrete actions
        throw PendingException()
    }

    @And("I press on the save button")
    fun iPressOnTheSaveButton() {
        // Write code here that turns the phrase above into concrete actions
        throw PendingException()
    }

    @Then("the fields go blank and I see a success message")
    fun theFieldsGoBlankAndISeeASuccessMessage() {
        // Write code here that turns the phrase above into concrete actions
        throw PendingException()
    }
}
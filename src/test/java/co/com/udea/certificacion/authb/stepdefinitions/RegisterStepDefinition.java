package co.com.udea.certificacion.authb.stepdefinitions;

import co.com.udea.certificacion.authb.tasks.ConnectTo;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.serenitybdd.screenplay.rest.interactions.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static net.serenitybdd.rest.SerenityRest.lastResponse;
import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.equalTo;

import java.util.List;
import java.util.Map;

public class RegisterStepDefinition {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterStepDefinition.class);
    private Actor user;

    @Before
    public void config() {
        OnStage.setTheStage(new OnlineCast());
        user = OnStage.theActorCalled("User");
    }

    @Given("I am connected to capacities of the service")
    public void iAmConnectedToCapacitiesOfTheService() {
        user.attemptsTo(ConnectTo.theService());
        LOGGER.info("Connected to service successfully");
    }

    @When("I enter the correct user data:")
    public void iEnterTheCorrectUserData(DataTable dataTable) {
        List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
        Map<String, String> userData = data.get(0);

        LOGGER.info("Attempting to register user with data: {}", userData);

        user.attemptsTo(
                Post.to("/users/v1/register")
                        .with(request -> request
                                .header("Content-Type", "application/json")
                                .body(userData)
                                .relaxedHTTPSValidation()
                        )
        );

        // Log the response for debugging
        LOGGER.info("Response status code: {}", SerenityRest.lastResponse().statusCode());
        LOGGER.info("Response body: {}", SerenityRest.lastResponse().asString());
    }

    @Then("I can see a 200 status code with successful registration message")
    public void iCanSeeA200StatusCodeWithSuccessfulRegistrationMessage() {
        int statusCode = lastResponse().statusCode();
        String responseBody = lastResponse().asString();

        LOGGER.info("Verifying response. Status Code: {}, Body: {}", statusCode, responseBody);

        user.should(
                seeThatResponse("Registration response verification",
                        response -> response
                                .statusCode(anyOf(
                                        equalTo(200),
                                        equalTo(201)
                                ))
                )
        );
    }
}
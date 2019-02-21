package cucumber.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.junit.Assert;
import utils.Zapis;


public class MySteps extends BaseStep {

    @Given("^I open website (.*)$")
    public void openSite(String site) throws Throwable {
        try {
            // Write code here that turns the phrase above into concrete actions

            // Check if JIRA has to be updated
            if(System.getProperty("updateJira").equalsIgnoreCase("TRUE")) {
                // Update Execution Status
                Zapis zapi = new Zapis();
                zapi.updateTestExecution("WIP", "Test Started");
            }

            driver.get(site);

        } catch (Throwable e){
        }
    }

    @Then("^I see the title (.*)$")
    public void verifyTitle(String title) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        Assert.assertEquals(title, driver.getTitle());


    }
}

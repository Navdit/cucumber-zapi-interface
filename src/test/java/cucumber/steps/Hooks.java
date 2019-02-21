package cucumber.steps;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import utils.CommonFunctions;
import utils.Zapis;
import static utils.CommonFunctions.logError;


/**
 * @author Thanh Le
 */
public class Hooks extends BaseStep {
    @Before
    public void setUp(Scenario scenario) throws Exception {
        System.out.println("=============================================START=======================================");
        System.out.println("|| Scenario: " + scenario.getName());
        System.out.println("=========================================================================================");

        // Check if Jira has to be updated
        if (System.getProperty("updateJira").equalsIgnoreCase("TRUE")) {

            // Get Test Case (IssueId) which will be added to Cycle
            String testCaseId = CommonFunctions.findSubstring(scenario.getName(), "-", " ");

            // Create Cycle
            Zapis.createCycle();

            // Add issueId to the Test Cycle
            Zapis zapi = new Zapis();
            zapi.addTestCaseToCycle(testCaseId);
        }

//        startWeb();
    }


    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
//            captureScreenShot(scenario);

            // Check if JIRA has to be updated
            if(System.getProperty("updateJira").equalsIgnoreCase("TRUE")) {
                String errorMessage = logError(scenario);

                // Updating Test Execution wit FAIL Status
                Zapis zapi = new Zapis();
                zapi.updateTestExecution("FAIL", errorMessage);
            }
        }
        else
        {
            // Check if JIRA has to be updated
            if(System.getProperty("updateJira").equalsIgnoreCase("TRUE")) {
                // Updating Test Execution with PASS Status
                Zapis zapi = new Zapis();
                zapi.updateTestExecution("PASS", "Test is Successful");
            }
        }
        stopWeb();
        System.out.println("=============================================END==============================================");
    }
}
package utils;

import data.Constants;
import org.json.JSONObject;

/**
 * @Name           : Zapis
 * @Description    : Implements Methods, which will use ZAPIs to update JIRA.
 *
 * @version 1.0 20th Feb 2019
 * @author Navdit Sharma
 */
public class Zapis {

    // Declare Global Variables
    public static String executionId = null;
    public static String cycleId = null;
    public static String zapiIssueId = null;

    /**
     * @Name           : createCycle
     * @Description    : Creates a Test Cycle in Jira
     *
     * @return cycleId : Return Cycle Id of the new Cycle, which got created.
     *
     * @version 1.0 20th Feb 2019
     * @author Navdit Sharma
     */
    public static void createCycle(){
        try {
            // Declare Classes
            CommonFunctions commFunctions = new CommonFunctions();

            // Send Request
            JSONObject response = commFunctions.sendRequest("POST", Constants.CREATE_CYCLE_URI,
                    Constants.CREATE_CYCLE_PAYLOAD);

            // Capture Cycle Id from Response
            cycleId = response.getString("id");

        }catch (Exception e){
        }
    }

    /**
     * @Name               : addTestCaseToCycle
     * @Description        : Add a Test Case to given Test Cycle and Project in Jira
     * @param issueId      : Issue Id or Test Case Id of the Test Case, which has to be added
     *
     * @return executionId : Returns Execution Id of the test case, which has been added.
     *
     * @version 1.0 20th Feb 2019
     * @author Navdit Sharma
     */
    public void addTestCaseToCycle(String issueId){
        // Payload
        String payload = "{\n" +
                "  \"cycleId\": \"" + Zapis.cycleId + "\",\n" +
                "  \"issueId\": \"" + issueId + "\",\n" +
                "  \"projectId\": \"" + Constants.PROJECT_ID + "\",\n" +
                "  \"versionId\": \"" + Constants.VERSION_ID + "\"\n" +
                "}";

        try {
            // Declare Classes
            CommonFunctions commFunctions = new CommonFunctions();

            // Send Request
            JSONObject response = commFunctions.sendRequest("POST", Constants.ADD_TEST_CASE_URI, payload);

            // As Key is Dynamic, so retrieving from String.
            String responseString = response.toString();
            executionId = CommonFunctions.findSubstring(responseString, "\"id\":", ",\"");

            zapiIssueId = issueId;

        }catch (Exception e){
        }
    }

    /**
     * @Name                 : updateTestExecution
     * @Description          : Update the given Execution Id.
     * @param executionStatus: Execution Status to be updated with. Values can be - PASS, FAIL, WIP, BLOCKED, UNEXECUTED
     * @param testComment    : Comment to be updated in Test Execution Field
     *
     * @version 1.0 20th Feb 2019
     * @author Navdit Sharma
     */
    public void updateTestExecution(String executionStatus, String testComment){
        // Add execution id to the uri
        String updateExecutionUri = Constants.EXECUTION_URI + Zapis.executionId + "/execute";

        // Remove Illegal Characters from testComment
        String legalTestComment = CommonFunctions.removeIllegalUnquotedCharacters(testComment);

        // Getting Status Code
        String executionStatusCode = CommonFunctions.getExecutionStatusCode(executionStatus);

        String payload = "{\n" +
                "  \"status\": \"" + executionStatusCode + "\",\n" +
                "  \"comment\": \"" + legalTestComment + "\"\n" +
                "}";

        try {
            // Declare Classes
            CommonFunctions commFunctions = new CommonFunctions();

            // Send Request
            JSONObject response = commFunctions.sendRequest("PUT", updateExecutionUri, payload);

        }catch (Exception e){
        }
    }


    /* Note: This Function has been added for future functionality */
    /**
     * @Name                 : bulkUpdateTestExecutionStatus
     * @Description          : Bulk/Single Update the given Execution Ids.
     * @param executionStatus: Execution Status to be updated with. Values can be - PASS, FAIL, WIP, BLOCKED, UNEXECUTED
     *
     * @version 1.0 20th Feb 2019
     * @author Navdit Sharma
     */
    public void bulkUpdateTestExecutionStatus(String executionStatus){
        // Getting Status Code
        String executionStatusCode = CommonFunctions.getExecutionStatusCode(executionStatus);

        //Payload
        String payload = "{\n" +
                "  \"executions\": [\n" +
                "    \"" + Zapis.executionId + "\"\n" +
                "  ],\n" +
                "  \"status\": \"" + executionStatusCode + "\",\n" +
                "  \"comment\": \"Test\",\n" +
                "  \"stepStatus\": \"1\"\n" +
                "}";

        try {
            // Declare Classes
            CommonFunctions commFunctions = new CommonFunctions();

            // Send Request
            JSONObject response = commFunctions.sendRequest("PUT", Constants.BULK_UPDATE_EXECUTION_URI, payload);

        }catch (Exception e){
        }
    }
}

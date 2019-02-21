package data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class Constants {

    // Project Related Information
    public static String PROJECT_ID = "12801";
    public static String VERSION_ID = "-1";
    public static String JIRA_SERVER = "https://<jira_server>";
    public static String CYCLE_NAME = "TavaAutomationTest-" +
                                        new SimpleDateFormat("yyyy-MM-dd-HH:mm").format(new Date());
    public static String CYCLE_DESC = "Cycle Created by Automation Test Framework on - " +
                                        new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());


    // ZAPI URIs
    public static String CREATE_CYCLE_URI = "/rest/zapi/latest/cycle";
    public static String ADD_TEST_CASE_URI = "/rest/zapi/latest/execution/";
    public static String EXECUTION_URI = "/rest/zapi/latest/execution/";
    public static String BULK_UPDATE_EXECUTION_URI = "/rest/zapi/latest/execution/updateBulkStatus";


    // ZAPI Payloads
    // Create Cycle Payload
    public static String CREATE_CYCLE_PAYLOAD = "{\n" +
            "  \"clonedCycleId\": \"\",\n" +
            "  \"name\": \"" + Constants.CYCLE_NAME + "\",\n" +
            "  \"build\": \"\",\n" +
            "  \"environment\": \"\",\n" +
            "  \"description\": \"" + Constants.CYCLE_DESC + "\",\n" +
            "  \"startDate\": \"" + new SimpleDateFormat("d/MMM/YY", Locale.ENGLISH).format(new Date()) + "\",\n" +
            "  \"endDate\": \"" + new SimpleDateFormat("d/MMM/YY", Locale.ENGLISH).format(new Date()) + "\",\n" +
            "  \"projectId\": \"" + Constants.PROJECT_ID + "\",\n" +
            "  \"versionId\": \"-1\",\n" +
            "  \"sprintId\": 1\n" +
            "}";

}

package utils;

import cucumber.runtime.ScenarioImpl;
import data.Constants;
import org.apache.commons.lang.reflect.FieldUtils;
import org.apache.http.HttpException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import gherkin.formatter.model.Result;
import java.io.*;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import cucumber.api.Scenario;

/**
 * @Name           : CommonFunctions
 * @Description    : Implements Methods, which will be across Classes
 *
 * @version 1.0 20th Feb 2019
 * @author Navdit Sharma
 */
public class CommonFunctions {

    /**
     * @Name           : readResponse
     * @Description    : Reads and validates Response from the given request, based on response code
     *                   and returns a String formatted Response body.
     * @param urlConn  : HTTPUrlConnection, set up to create the request
     *
     * @return response: Returns response body in string format
     *
     * @version 1.0 20th Feb 2019
     * @author Navdit Sharma
     */
    public String readResponse(HttpURLConnection urlConn){

        // Declare Empty Response Variable
        StringBuilder response = new StringBuilder();

        try {
            // Check for Response Code
            int responseCode = urlConn.getResponseCode();

            // Save Response
            InputStream responseInputStream;
            if (200 <= responseCode && responseCode <= 299) {
                responseInputStream = urlConn.getInputStream();
            } else {
                responseInputStream = urlConn.getErrorStream();
            }

            BufferedReader responseBuff = new BufferedReader(new InputStreamReader(responseInputStream));

            String responseCurrentLine;

            while ((responseCurrentLine = responseBuff.readLine()) != null)
                response.append(responseCurrentLine);

            responseBuff.close();

        } catch (IOException e){
        }

        // Return response in String
        return response.toString();
    }


    /**
     * @Name           : getBasicAuthHeader
     * @Description    : Reads credentials.json in Resource Folder to return Basic Auth Header.
     *                   and returns a String formatted Response body.
     *
     * @return         : Returns Basic Auth Header.
     *
     * @version 1.0 20th Feb 2019
     * @author Navdit Sharma
     */
    public String getBasicAuthHeader(){
        String basicAuth = "Null";

        try {
            JSONParser parser = new JSONParser();
            ClassLoader classLoader = getClass().getClassLoader();
            Object obj = parser.parse(new FileReader(new File(classLoader.getResource("credentials.json").getFile())));

            org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) obj;
            basicAuth = (String) jsonObject.get("BasicAuth");

        } catch (FileNotFoundException e){
        } catch (IOException e){
        } catch (ParseException e){
        }

        return "Basic " + basicAuth;
    }


    /**
     * @Name           : sendRequest
     * @Description    : Sends request and read  it's response into Jason Object
     * @param method   : Method can be POST, GET, PUT, DELETE any HTTP valid method.
     * @param uri      : uri of Create Cycle API
     * @param payload  : Jason payload having name, description, start and end date, project id, version id
     *
     * @return         : Return response body in Jason Object.
     *
     * @version 1.0 20th Feb 2019
     * @author Navdit Sharma
     */
    public JSONObject sendRequest(String method, String uri, String payload){

        JSONObject responseJsonObj = null;
        HttpURLConnection requestConn;

        // Todo: This workaround needs to be removed prior to deployment, as this is not recommended in Prod.
        //***************************** Workaround *****************************
        // Install the all-trusting trust manager
        AllTrustManager trustManager = new AllTrustManager();
        TrustManager[] trustAllCerts = trustManager.installAllTrustingManager();

        try{
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
        }
        //***************************** Workaround *****************************

        try {
            // POST Request
            // Open Connection
            URL requestUrl = new URL(Constants.JIRA_SERVER + uri);
            requestConn = (HttpURLConnection)requestUrl.openConnection();

            // Add Headers
            requestConn.setRequestProperty ("Authorization", getBasicAuthHeader());
            requestConn.setRequestProperty("Content-Type", "application/json");
            requestConn.setRequestMethod(method.toUpperCase());
            requestConn.setDoOutput(true);
            requestConn.setDoInput(true);
            OutputStream os = requestConn.getOutputStream();

            if (!method.equalsIgnoreCase( "GET")){
                os.write(payload.getBytes("UTF-8"));
                os.close();
            }

            // Validate Response and Retrieve any information required
            String response = readResponse(requestConn);
            if (requestConn.getResponseCode() == 200) {
                // Converting Response from String to Json
                responseJsonObj = new JSONObject(response);

            } else {
                throw new HttpException(response);
            }

        } catch (UnsupportedEncodingException e) {
        } catch (MalformedURLException e) {
        } catch (IOException e){
        } catch (HttpException e){
            System.out.println(e);
        }

        return responseJsonObj;
    }

    /**
     * @Name               : findSubstring
     * @Description        : Finds the substring between two given substrings, defined by left and right boundary.
     * @param inputString  : Main string in which substring/pattern needs to be matched and retrieved.
     * @param leftBoundary : Left boundary of the substring, which needs to be retrieved.
     * @param rightBoundary: Right boundary of the substring, which needs to be retrieved.
     *
     * @return             : Return substring between the given left and right boundary
     *
     * @version 1.0 20th Feb 2019
     * @author Navdit Sharma
     */
    public static String findSubstring (String inputString, String leftBoundary, String rightBoundary){
        String outputString = null;
        Pattern pattern = Pattern.compile(leftBoundary + "(.*?)" + rightBoundary);
        Matcher matcher = pattern.matcher(inputString);
        while (matcher.find()) {
            outputString = matcher.group(1);
        }

        return outputString;
    }


    /**
     * @Name           : logError
     * @Description    : Logs the Error found during Cucumber Execution.
     * @param scenario : Main string in which substring/pattern needs to be matched and retrieved.
     *
     * @return         : Returns the Error Message in String Format.
     *
     * @version 1.0 21st Feb 2019
     * @author Navdit Sharma
     */
    public static String logError(Scenario scenario) {
        String errorMsg = null;

        Field field = FieldUtils.getField(((ScenarioImpl) scenario).getClass(), "stepResults", true);
        field.setAccessible(true);
        try {
            ArrayList<Result> results = (ArrayList<Result>) field.get(scenario);
            for (Result result : results) {
                if (result.getError() != null) {
                    errorMsg = result.getErrorMessage();
                }
            }
        } catch (Exception e) {
            System.out.println("Error while logging error" + e);
        }

        return errorMsg;
    }


    /**
     * @Name                   : getExecutionStatusCode
     * @Description            : Returns the Zephyr understandable execution status code from Human understandable Execution
     *                           Status Code
     * @param executionStatus  : Main string in which substring/pattern needs to be matched and retrieved.
     *
     * @return                 : Return executionStatusCode in String Format
     *
     * @version 1.0 21st Feb 2019
     * @author Navdit Sharma
     */
    public static String getExecutionStatusCode(String executionStatus){
        // Getting Status Code
        String statusCode = "NULL";

        try{
            if (executionStatus.equalsIgnoreCase("PASS")){
                statusCode = "1";
            }
            else if (executionStatus.equalsIgnoreCase("FAIL")){
                statusCode = "2";
            }
            else if (executionStatus.equalsIgnoreCase("WIP")){
                statusCode = "3";
            }
            else if (executionStatus.equalsIgnoreCase("BLOCKED")){
                statusCode = "4";
            }
            else if (executionStatus.equalsIgnoreCase("UNEXECUTED")){
                statusCode = "-1";
            }
            else
            {
                throw new Exception("Seems an issue with Execution Status. " +
                        "Values can be - PASS, FAIL, WIP, BLOCKED, UNEXECUTED");
            }
        } catch (Exception e){
            System.out.println(e);
        }

        return statusCode;
    }


    /**
     * @Name              : removeIllegalUnquotedCharacters
     * @Description       : Replaces Unquoted Characters with one more backlash so that they can be passed to Jason
     * @param inputString : Main string in which substring/pattern needs to be matched and retrieved.
     *
     * @return            : Return OutputString, which has got the replacements with double backlash
     *
     * @version 1.0 21th Feb 2019
     * @author Navdit Sharma
     */
    public static String removeIllegalUnquotedCharacters(String inputString){
        //array to hold replacements
        String[][] replacements = {{"\n", "\\n"},
                                   {"\t", "\\t"},
                                   {"\\v", "\\\\v"},
                                   {"\r", "\\r"},
                                   {"\0", "\\0"}};

        //loop over the array and replace
        String outputString = inputString;
        for(String[] replacement: replacements) {
            outputString = outputString.replace(replacement[0], replacement[1]);
        }

        return outputString;
    }

}

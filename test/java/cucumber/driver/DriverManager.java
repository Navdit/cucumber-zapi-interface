package cucumber.driver;

import au.com.tava.core.seleniumDriverFactory.SeleniumDriverFactory;
import org.openqa.selenium.WebDriver;

public class DriverManager {
    public static WebDriver getDriver() throws Exception {
        return new SeleniumDriverFactory("chrome.json").getNewDriver(); }
}

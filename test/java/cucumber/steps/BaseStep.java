package cucumber.steps;

import cucumber.api.Scenario;
import cucumber.driver.DriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author Thanh Le
 */
public class BaseStep {
    protected static WebDriver driver;
    protected static WebDriverWait wait;


    public void startWeb() throws Exception {
        driver = DriverManager.getDriver();
    }

    public void stopWeb() {
        if (driver != null) {
            driver.quit();
        }
    }

    protected void captureScreenShot(Scenario scenario) {
        try {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.embed(screenshot, "image/png");
        } catch (WebDriverException wde) {
            System.err.println(wde.getMessage());
        } catch (ClassCastException cce) {
            cce.printStackTrace();
        }
    }
}
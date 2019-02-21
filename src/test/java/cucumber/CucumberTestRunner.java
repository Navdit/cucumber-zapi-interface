package cucumber;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;
/**
 * @author Thanh Le
 */
@RunWith(Cucumber.class) @CucumberOptions(
        strict = true,
        features = {"src/test/resources/features"},
        plugin = {"json:target/cucumber/report.json", "html:target/cucumber/report.html"},
        monochrome = false,
        tags = {},
        glue = {"cucumber.steps"}
)
public class CucumberTestRunner{ }

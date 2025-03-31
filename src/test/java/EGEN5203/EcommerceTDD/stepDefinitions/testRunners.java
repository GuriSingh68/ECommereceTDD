package EGEN5203.EcommerceTDD.stepDefinitions;


import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/feature",
        glue = "EGEN5203.EcommerceTDD.stepDefinitions", // Base package for all step defs
        plugin = {"pretty", "html:target/cucumber-reports"},
        monochrome = true
)
public class testRunners {
}

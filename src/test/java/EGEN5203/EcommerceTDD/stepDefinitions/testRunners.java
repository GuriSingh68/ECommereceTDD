package EGEN5203.EcommerceTDD.stepDefinitions;


import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/feature",  // Location of feature files
        glue = "EGEN5203.EcommerceTDD.stepDefinitions"                   // Location of step definition classes
)
public class testRunners {

}

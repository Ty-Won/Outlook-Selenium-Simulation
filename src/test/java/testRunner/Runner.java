package testRunner;

import org.junit.runner.RunWith;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;     

@RunWith(Cucumber.class)                
@CucumberOptions(features="resources",glue={"com.outlook.cucumber"})                       
public class Runner                 
{       

}

package com.outlook.cucumber;


import org.junit.runner.RunWith;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;     

@RunWith(Cucumber.class)                
@CucumberOptions(features= {"src/test/resources/mcgill-outlook.feature"}, glue={"com.outlook.cucumber"})                       
public class Runner                 
{       

}

package com.outlook.cucumber;

import cucumber.api.java8.En;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class StepDefinition implements En{


  private WebDriver driver = new ChromeDriver();
  private final String MCGILL_OUTLOOK_URL = "https://outlook.office.com/owa/?realm=mcgill.ca";





  public StepDefinition() {

    Given("I am on the McGill Outlook Email page", () -> {
      // Write code here that turns the phrase above into concrete actions
      visitURL(MCGILL_OUTLOOK_URL);
      throw new cucumber.api.PendingException();
    });

    Given("the email contains all the correct credentials", () -> {
      // Write code here that turns the phrase above into concrete actions
      throw new cucumber.api.PendingException();
    });

    Given("the email has an image attached", () -> {
      // Write code here that turns the phrase above into concrete actions
      throw new cucumber.api.PendingException();
    });

    When("I click {string}", (String string) -> {
      // Write code here that turns the phrase above into concrete actions
      throw new cucumber.api.PendingException();
    });

    Then("the email should be sent with the photo", () -> {
      // Write code here that turns the phrase above into concrete actions
      throw new cucumber.api.PendingException();
    });
  }

  private void visitURL(String url){
    if(driver!=null){
      System.out.println("Visiting: " + url);
      driver.get(url);
    }
  }
}

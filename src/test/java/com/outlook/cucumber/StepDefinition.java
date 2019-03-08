package com.outlook.cucumber;

import cucumber.api.java8.En;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.junit.Assert;
import org.junit.Assert;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class StepDefinition implements En{


  private WebDriver driver = new ChromeDriver();
  private final String MCGILL_OUTLOOK_URL = "https://outlook.office.com/owa/?realm=mcgill.ca";
  private final String MCGILL_OUTLOOK_SENT_EMAILS_URL = "https://outlook.office.com/owa/?realm=mcgill.ca&path=/mail/sentitems";

  //Prompt user for email and password in terminal in Main
  public StepDefinition(String login,String password,String recipient) {


    Given("a valid user with an email draft open in the McGill Outlook Email page", () -> {

      visitURL(MCGILL_OUTLOOK_URL);

      System.out.println("Signing In");

      WebElement emailForm = (new WebDriverWait(driver, 10))
      .until(ExpectedConditions.presenceOfElementLocated(By.id("userNameInput")));
      emailForm.sendKeys(login);

      WebElement passwordForm = (new WebDriverWait(driver, 10))
      .until(ExpectedConditions.presenceOfElementLocated(By.id("passwordInput")));
      passwordForm.sendKeys(login);

      driver.findElement(By.id("submitButton")).click();

      //Handles the "Do you want to remain logged in prompt
      if(driver.findElements(By.id("idBtn_Back")).size()!=0) {
        driver.findElement(By.id("idBtn_Back")).click();
      }

      //Wait for the page to load and try to find the new email button
      WebElement newEmailButton = (new WebDriverWait(driver, 10))
              .until(ExpectedConditions.presenceOfElementLocated(By.id("_ariaId_23")));
      newEmailButton.click();
    });


    When("they send an email to <address>", () -> {
      WebElement email_field = driver.findElements(By.className("_fp_5")).get(0);
      email_field.sendKeys(recipient);
    });

    And("an <image> is attached", () -> {
      WebElement btn = driver.findElements(By.className("_mcp_92")).get(0);
      btn.click();
      System.out.println("Clicking the attachments button");

      JavascriptExecutor js = (JavascriptExecutor)driver;
      js.executeScript("document.querySelector(\"button[autoid='_fce_6']\").click();");

      /*********************** Swap to directory where image is located ***********************/
      Process p = Runtime.getRuntime().exec("/home/twong/Pictures/graph.png");

    });



    And("attach a large image",() ->{
      JavascriptExecutor js = (JavascriptExecutor)driver;
      js.executeScript("document.querySelector(\"button[autoid='_fce_6']\").click();");

            /*********************** Swap to directory where oversized image is located ***********************/
      Process p = Runtime.getRuntime().exec("<insert path>");
    });


    Then("outlook throws a size error for <image>", ()->{
      WebElement attachButton = (new WebDriverWait(driver, 10))
      .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("button[autoid=_av_2]")));

      //If the attach button is disabled
      if(!attachButton.isEnabled()){
        WebElement closePromptButton = (new WebDriverWait(driver, 10))
        .until(ExpectedConditions.presenceOfElementLocated(By.className("_av_4 o365button")));
        closePromptButton.click();
        System.out.println("Error in Size detected. Closing attachment prompt");
      }
      else{
        throw new cucumber.api.PendingException();
      }
    });


    Then("the email can be viewed in the sent section to <address> with <image> attached", ()->{
      //TODO send email and verify its been sent
      visitURL(MCGILL_OUTLOOK_SENT_EMAILS_URL);
      


    });


  }

  //helper method to visit certain pages
  private void visitURL(String url){
    if(driver!=null){
      System.out.println("Visiting: " + url);
      driver.get(url);
    }
  }
}

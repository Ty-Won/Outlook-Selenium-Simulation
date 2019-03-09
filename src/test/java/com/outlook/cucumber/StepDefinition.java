package com.outlook.cucumber;

import static io.github.bonigarcia.wdm.DriverManagerType.CHROME;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;

public class StepDefinition {
  private final String MCGILL_OUTLOOK_URL = "https://outlook.office.com/owa/?realm=mcgill.ca";
  private final String MCGILL_OUTLOOK_SENT_EMAILS_URL =
      "https://outlook.office.com/owa/?realm=mcgill.ca&path=/mail/sentitems";

  private final String OUTLOOK_SIGN_IN =
      "https://login.live.com/login.srf?wa=wsignin1.0&rpsnv=13&ct=1552110548&rver=7.0.6737.0&wp=MBI_SSL&wreply=https%3a%2f%2foutlook.live.com%2fowa%2f%3fnlp%3d1%26RpsCsrfState%3d39c113d7-80fd-b660-3327-7f08e0f9b60e&id=292841&aadredir=1&CBCXT=out&lw=1&fl=dob%2cflname%2cwld&cobrandid=90015";

  private WebDriver driver;

  public StepDefinition() {
    WebDriverManager.getInstance(CHROME).setup();
    driver = new ChromeDriver();
  }

  @Given("^a user with username (.*) and password (.*) has an email draft open in the McGill Outlook Email page$")
  public void navigateToPage(String username, String password) {
    visitURL(OUTLOOK_SIGN_IN);

    System.out.println("Signing In");

    WebElement emailForm = (new WebDriverWait(driver, 10))
        .until(ExpectedConditions.presenceOfElementLocated(By.id("i0116")));
    emailForm.sendKeys(username);

    // Next button to pass to password form
    (new WebDriverWait(driver, 10))
        .until(ExpectedConditions.presenceOfElementLocated(By.id("idSIButton9"))).click();

    WebElement passwordForm = (new WebDriverWait(driver, 10))
        .until(ExpectedConditions.elementToBeClickable(By.id("i0118")));
    passwordForm.click();
    passwordForm.clear();
    passwordForm.sendKeys(password);

    // Sign in button, takes time to appear
    (new WebDriverWait(driver, 15))
        .until(ExpectedConditions.elementToBeClickable(By.id("idSIButton9"))).click();


    // Wait for the page to load and try to find the new email button
    (new WebDriverWait(driver, 15))
        .until(ExpectedConditions.presenceOfElementLocated(By.id("id__5"))).click();
  }

  @And("^image at (.*) is attached$")
  public void attachImage(String path) {

    // Attachment button
    (new WebDriverWait(driver, 10))
        .until(ExpectedConditions.elementToBeClickable(
            By.cssSelector(".ms-Button-menuIcon.menuIcon-146[data-icon-name=ChevronDown]")))
        .click();

    System.out.println("Clicked attachment button");

    WebElement thisCPButton = driver.findElements(By.className("label-193")).get(0);
    thisCPButton.click();

    Path relativePath = Paths.get("").toAbsolutePath();
    String fullPath = relativePath.resolve(path).toString();

    StringSelection ss = new StringSelection(fullPath);
    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
    Robot robot;
    try {
      robot = new Robot();
      Thread.sleep(1000);
      robot.keyPress(KeyEvent.VK_CONTROL);
      robot.keyPress(KeyEvent.VK_V);
      robot.keyRelease(KeyEvent.VK_V);
      robot.keyRelease(KeyEvent.VK_CONTROL);
      robot.keyPress(KeyEvent.VK_ENTER);
      robot.keyRelease(KeyEvent.VK_ENTER);
    } catch (AWTException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @When("^they send an email to (.*)$")
  public void sendEmail(String recipientEmail) {
    WebElement email_field =
        driver.findElement(By.cssSelector(".ms-BasePicker-input.pickerInput_269bfa71"));
    email_field.sendKeys(recipientEmail);
    
    driver.findElement(By.id("subjectLine0")).sendKeys("Subject");

    (new WebDriverWait(driver, 10))
        .until(ExpectedConditions
            .elementToBeClickable(By.cssSelector(".ms-Button-icon.icon-145[data-icon-name=Send]")))
        .click();


  }


  @Then("an email with recipient address (.*) with an attachment will appear in the \"sent\" section")
  public void checkSent(String recipientEmail) {

  }

  @And("a large image at <imagePath> is attached")
  public void attachLargeImage(String path) {
    JavascriptExecutor js = (JavascriptExecutor) driver;
    js.executeScript("document.querySelector(\"button[autoid='_fce_6']\").click();");

    /***********************
     * Swap to directory where oversized image is located
     ***********************/
    try {
      Process p = Runtime.getRuntime().exec(path);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Then("^outlook throws a size error")
  public void checkError() {
    WebElement attachButton = (new WebDriverWait(driver, 10))
        .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("button[autoid=_av_2]")));

    // If the attach button is disabled
    if (!attachButton.isEnabled()) {
      WebElement closePromptButton = (new WebDriverWait(driver, 10))
          .until(ExpectedConditions.presenceOfElementLocated(By.className("_av_4 o365button")));
      closePromptButton.click();
      System.out.println("Error in Size detected. Closing attachment prompt");
    } else {
      throw new cucumber.api.PendingException();
    }
  }


  // helper method to visit certain pages
  private void visitURL(String url) {
    if (driver != null) {
      System.out.println("Visiting: " + url);
      driver.get(url);
    }
  }
}

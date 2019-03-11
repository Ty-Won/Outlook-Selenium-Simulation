package com.outlook.cucumber;

import static io.github.bonigarcia.wdm.DriverManagerType.CHROME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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

  private final String OUTLOOK_SIGN_IN =
      "https://login.live.com/login.srf?wa=wsignin1.0&rpsnv=13&ct=1552110548&rver=7.0.6737.0&wp=MBI_SSL&wreply=https%3a%2f%2foutlook.live.com%2fowa%2f%3fnlp%3d1%26RpsCsrfState%3d39c113d7-80fd-b660-3327-7f08e0f9b60e&id=292841&aadredir=1&CBCXT=out&lw=1&fl=dob%2cflname%2cwld&cobrandid=90015";
  private String emailSendTime;
  private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

  private WebDriver driver;

  public StepDefinition() {
    WebDriverManager.getInstance(CHROME).setup();
    driver = new ChromeDriver();
  }

  @Given("^a user with username (.*) and password (.*) has an email draft open in the McGill Outlook Email page$")
  public void navigateToPage(String username, String password) {
    try {
      visitURL(OUTLOOK_SIGN_IN);

      System.out.println("Signing In");

      WebElement emailForm = (new WebDriverWait(driver, 10))
          .until(ExpectedConditions.presenceOfElementLocated(By.id("i0116")));
      emailForm.sendKeys(username);

      // Next button to pass to password form
      (new WebDriverWait(driver, 10))
          .until(ExpectedConditions.elementToBeClickable(By.id("idSIButton9"))).click();

      WebElement passwordForm = (new WebDriverWait(driver, 10))
          .until(ExpectedConditions.elementToBeClickable(By.id("i0118")));
      passwordForm.click();
      passwordForm.clear();
      passwordForm.sendKeys(password);

      // Sign in button, takes time to appear
      (new WebDriverWait(driver, 15))
          .until(ExpectedConditions.elementToBeClickable(By.id("idSIButton9"))).click();

      // Wait for the page to load and try to find the new email button
      (new WebDriverWait(driver, 15)).until(ExpectedConditions.elementToBeClickable(By.id("id__5")))
          .click();
    } catch (Exception e) {
      driver.close();
      throw e;
    }
  }

  @And("^image at (.*) is attached$")
  public void attachImage(String path) throws Exception {
    try {
      // Attachment button
      (new WebDriverWait(driver, 10))
          .until(
              ExpectedConditions.elementToBeClickable(By.cssSelector(".ms-Button[name='Attach']")))
          .click();

      System.out.println("Clicked attachment button");

      // Click browse this computer button
      (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(
          By.cssSelector(".ms-ContextualMenu-link[name='Browse this computer']"))).click();

      Path relativePath = Paths.get("").toAbsolutePath();
      String fullPath = relativePath.resolve(path).toString();

      StringSelection ss = new StringSelection(fullPath);
      Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
      Robot robot;
      robot = new Robot();
      Thread.sleep(1000);
      robot.keyPress(KeyEvent.VK_CONTROL);
      robot.keyPress(KeyEvent.VK_V);
      robot.keyRelease(KeyEvent.VK_V);
      robot.keyRelease(KeyEvent.VK_CONTROL);
      robot.keyPress(KeyEvent.VK_ENTER);
      robot.keyRelease(KeyEvent.VK_ENTER);
      Thread.sleep(1000);
    } catch (Exception e) {
      driver.close();
      throw e;
    }
  }

  @When("^they send an email to (.*) with subject (.*)$")
  public void sendEmail(String recipientEmail, String subject) {
    try {
      WebElement email_field =
          driver.findElement(By.cssSelector(".ms-BasePicker-input.pickerInput_269bfa71"));
      email_field.sendKeys(recipientEmail);

      driver.findElement(By.id("subjectLine0")).sendKeys(subject);

      emailSendTime = LocalTime.now().format(formatter);

      (new WebDriverWait(driver, 15))
          .until(ExpectedConditions.elementToBeClickable(By.cssSelector(".ms-Button[name='Send']")))
          .click();

      System.out.println("Email sent");
    } catch (Exception e) {
      driver.close();
      throw e;
    }
  }

  @Then("an email with an attachment sent to recipient address (.*) with subject (.*) will appear in the \"sent\" section")
  public void checkSent(String recipientEmail, String subject) throws Exception {
    try {
      Thread.sleep(1000);

      JavascriptExecutor js = (JavascriptExecutor) driver;
      js.executeScript("document.querySelector('div[title=\"Sent Items\"]').click()");

      WebElement most_recent_email =
          (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(
              By.cssSelector("div[class='_1t7vHwGnGnpVspzC4A22UM'] div:nth-child(2)")));

      String recipient_sent = most_recent_email
          .findElement(By.cssSelector("._3HQ_h7iVcVeOo03bOFpl__ > span")).getText();
      String sent_subject =
          most_recent_email.findElement(By.cssSelector(".RKJYnFQ991LYsw_styUw > span")).getText();

      assertTrue(recipient_sent.equals(recipientEmail) && subject.equals(sent_subject));

      String emailTimeStamp = (new WebDriverWait(driver, 10))
          .until(ExpectedConditions
              .presenceOfElementLocated(By.cssSelector("._33-Hq3-X20Ind6j6gvICV9")))
          .getAttribute("title");

      assertEquals(emailSendTime, emailTimeStamp);

      System.out.println("Successfully Sent");
    } catch (Exception e) {
      throw e;
    } finally {
      driver.close();
    }
  }

  @When("^they try to attach a large image from (.*)$")
  public void attachLargeImage(String path) throws Exception {
    attachImage(path);
  }

  @Then("^outlook throws a size error")
  public void checkError() {
    // try to find the error element
    try {
      WebElement errorElement = (new WebDriverWait(driver, 10))
          .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".ms-Dialog-main")));
      assertTrue(errorElement != null);
    } catch (Exception e) {
      throw e;
    } finally {
      driver.close();
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

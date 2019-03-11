package com.outlook.cucumber;

import static io.github.bonigarcia.wdm.DriverManagerType.CHROME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import org.openqa.selenium.Alert;
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
  // Used to make sure the email was sent correctly
  private String emailSendTime;
  private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

  private WebDriver driver;
  // Link to the outlook sign in page
  private final String OUTLOOK_SIGN_IN =
      "https://login.live.com/login.srf?wa=wsignin1.0&rpsnv=13&ct=1552110548&rver=7.0.6737.0&wp=MBI_SSL&wreply=https%3a%2f%2foutlook.live.com%2fowa%2f%3fnlp%3d1%26RpsCsrfState%3d39c113d7-80fd-b660-3327-7f08e0f9b60e&id=292841&aadredir=1&CBCXT=out&lw=1&fl=dob%2cflname%2cwld&cobrandid=90015";

  public StepDefinition() {
    WebDriverManager.getInstance(CHROME).setup();
    driver = new ChromeDriver();
  }


  @Given("^a user with username (.*) and password (.*) has a new email open in the Outlook Email page$")
  public void navigateToNewEmail(String username, String password) {
    try {
      // Navigate to the outlook page to start testing
      visitURL(OUTLOOK_SIGN_IN);

      System.out.println("Attempting to sign in..");

      // Fill in the sign in information
      WebElement emailForm = (new WebDriverWait(driver, 10))
          .until(ExpectedConditions.presenceOfElementLocated(By.id("i0116")));
      emailForm.sendKeys(username);

      // Next button to move on to password form
      (new WebDriverWait(driver, 10))
          .until(ExpectedConditions.elementToBeClickable(By.id("idSIButton9"))).click();

      // Fill in password
      WebElement passwordForm = (new WebDriverWait(driver, 10))
          .until(ExpectedConditions.elementToBeClickable(By.id("i0118")));
      passwordForm.click();
      passwordForm.clear();
      passwordForm.sendKeys(password);

      // Sign in button to move on to the next page
      (new WebDriverWait(driver, 15))
          .until(ExpectedConditions.elementToBeClickable(By.id("idSIButton9"))).click();

      System.out.println("Signed in!");

      // Wait for the page to load and try to find the new email button
      (new WebDriverWait(driver, 15)).until(ExpectedConditions.elementToBeClickable(By.id("id__5")))
          .click();
    } catch (Exception e) {
      driver.close();
      throw e;
    }
  }

  @Given("^a user with username (.*) and password (.*) has an existing email draft with image (.*)$")
  public void navigateToDraft(String username, String password, String imagePath) throws Exception {
    try {
      // Navigate to a new email page to create a draft email (with an image attachment)
      navigateToNewEmail(username, password);
      attachImage(imagePath);

      // Navigate to the draft page
      (new WebDriverWait(driver, 15))
          .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[title='Drafts']")))
          .click();

      // Select the most recent draft to send
      (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(
          By.cssSelector("div[class='_1t7vHwGnGnpVspzC4A22UM'] div:nth-child(2)"))).click();
      
      // Wait until the image editor page opens 
      (new WebDriverWait(driver, 10)).until(ExpectedConditions
          .presenceOfElementLocated(By.cssSelector("input[placeholder='Add a subject']")));

    } catch (Exception e) {
      driver.close();
      throw e;
    }
  }

  @And("^image at (.*) is attached$")
  public void addImageAttachment(String path) throws Exception {
    System.out.println("Attempting to attach image..");

    // Attach the actual image to the email
    attachImage(path);

    // Wait til the image attachment is visable in the email
    (new WebDriverWait(driver, 10)).until(
        ExpectedConditions.visibilityOfElementLocated(By.cssSelector("._1RJtcqtrjg-k7PLpqrbSph")));

    System.out.println("Image uploaded!");
  }

  @When("^they send an email to (.*) with subject (.*)$")
  public void sendEmail(String recipientEmail, String subject) {
    try {
      // Fill recipient field with receiver address
      WebElement email_field =
          driver.findElement(By.cssSelector(".ms-BasePicker-input.pickerInput_269bfa71"));
      email_field.sendKeys(recipientEmail);

      driver.findElement(By.cssSelector("input[placeholder='Add a subject']")).sendKeys(subject);

      // Get the time this email was sent and save it
      emailSendTime = LocalTime.now().format(formatter);

      // Send the email
      (new WebDriverWait(driver, 15))
          .until(ExpectedConditions.elementToBeClickable(By.cssSelector(".ms-Button[name='Send']")))
          .click();

    } catch (Exception e) {
      driver.close();
      throw e;
    }
  }


  @Then("^an email with an attachment sent to recipient address (.*) with subject (.*) will appear in the \"sent\" section$")
  public void checkSent(String recipientEmail, String subject) throws Exception {
    try {
      // Navigate to the sent items page
      JavascriptExecutor js = (JavascriptExecutor) driver;
      js.executeScript("document.querySelector('div[title=\"Sent Items\"]').click()");

      // Refresh the page to get the latest sent emails
      driver.navigate().refresh();

      // Handles the "are you sure you want to refresh" prompt
      WebDriverWait wait = new WebDriverWait(driver, 10);
      Alert alertPrompt = wait.until(ExpectedConditions.alertIsPresent());

      if (alertPrompt != null) {
        alertPrompt.accept();
      }

      // Grabs the most recent email to compare recipient and subject to the expected one we just
      // sent
      WebElement most_recent_email =
          (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(
              By.cssSelector("div[class='_1t7vHwGnGnpVspzC4A22UM'] div:nth-child(2)")));

      String recipient_sent = most_recent_email
          .findElement(By.cssSelector("._3HQ_h7iVcVeOo03bOFpl__ > span")).getText();
      String sent_subject =
          most_recent_email.findElement(By.cssSelector(".RKJYnFQ991LYsw_styUw > span")).getText();

      String emailTimeStamp = (new WebDriverWait(driver, 10))
          .until(ExpectedConditions
              .presenceOfElementLocated(By.cssSelector("._33-Hq3-X20Ind6j6gvICV9")))
          .getAttribute("title");

      // Confirm this is the same email we just sent by checking if subject, recipient and time
      // match
      assertEquals(recipient_sent, recipientEmail);
      assertEquals(subject, sent_subject);
      assertEquals(emailSendTime, emailTimeStamp);

      System.out.println("Successfully Sent");
    } catch (Exception e) {
      throw e;
    } finally {
      driver.close();
    }
  }

  @When("^user attempts to attach a large image from (.*)$")
  public void attachLargeImage(String path) throws Exception {
    attachImage(path);
  }

  @Then("^outlook throws a size error$")
  public void checkError() {
    // Try to find the error element
    try {
      WebElement errorElement = (new WebDriverWait(driver, 10))
          .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".ms-Dialog-main")));
      // If outlook gave us an error, this test passed
      assertTrue(errorElement != null);
    } catch (Exception e) {
      throw e;
    } finally {
      driver.close();
    }
  }

  // Helper method to visit certain pages
  private void visitURL(String url) {
    if (driver != null) {
      System.out.println("Visiting: " + url);
      driver.get(url);
    }
  }

  // Helper method to attach images to an email
  private void attachImage(String path) throws Exception {
    try {
      // Attempt to find the attach button and click it
      (new WebDriverWait(driver, 10))
          .until(
              ExpectedConditions.elementToBeClickable(By.cssSelector(".ms-Button[name='Attach']")))
          .click();

      // Click "browse this computer" button to select from where to upload the file
      (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(
          By.cssSelector(".ms-ContextualMenu-link[name='Browse this computer']"))).click();

      // Get absolute path to file
      Path relativePath = Paths.get("").toAbsolutePath();
      String fullPath = relativePath.resolve(path).toString();

      // Copy the path onto the clipboard in order to paste it into the windows file prompt
      StringSelection ss = new StringSelection(fullPath);
      Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);

      // Need to use robot to paste the path into the file explorer since Selenium has no control
      // outside of the browser
      Robot robot;
      robot = new Robot();
      (new WebDriverWait(driver, 10)).until(ExpectedConditions.invisibilityOfElementLocated(
          By.cssSelector(".ms-ContextualMenu-link[name='Browse this computer']")));

      // Paste the path into the file selector
      robot.keyPress(KeyEvent.VK_CONTROL);
      robot.keyPress(KeyEvent.VK_V);
      robot.keyRelease(KeyEvent.VK_V);
      robot.keyRelease(KeyEvent.VK_CONTROL);
      robot.keyPress(KeyEvent.VK_ENTER);
      robot.keyRelease(KeyEvent.VK_ENTER);

    } catch (Exception e) {
      driver.close();
      throw e;
    }
  }
}

package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NoteTests {

    @LocalServerPort
    private int port;

    private WebDriver driver;

    private final JavascriptExecutor js;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            this.driver.quit();
        }
    }

    @Test
    public void requireLogin() {
        driver.get("http://localhost:" + this.port + "/home");
        Assertions.assertEquals("Login", driver.getTitle());
    }

    @Test
    public void noteManagement() {
        AuthenticationTests authentication = new AuthenticationTests();
        WebDriverWait webDriverWait = new WebDriverWait(this.driver, Duration.ofSeconds(2));
        String timestamp = String.valueOf((new Date().getTime()));

        String username = "NM-" + timestamp;
        authentication.signup(username, this.driver, this.port);
        authentication.login(username, "12345678", this.driver, this.port);

        // Click "Notes" tab
        WebElement noteTab = webDriverWait.until(webDriver -> webDriver.findElement(By.id("nav-notes-tab")));
        noteTab.click();

        WebElement showAddNoteButton = webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("showAddNoteButton")));
//        WebElement showAddNoteButton = webDriverWait.until(webDriver -> webDriver.findElement(By.id("showAddNoteButton")));
        showAddNoteButton.click();

        WebElement inputNoteTitle = webDriverWait.until(webDriver -> webDriver.findElement(By.id("note-title")));
        inputNoteTitle.click();
        String noteTitle = "Note " + timestamp;
        inputNoteTitle.sendKeys("Note " + timestamp);

        WebElement inputNoteDesc = webDriverWait.until(webDriver -> webDriver.findElement(By.id("note-description")));
        inputNoteDesc.click();
        inputNoteDesc.sendKeys("Note content " + timestamp);

        // Submit form
        WebElement submitNoteButton = webDriverWait.until(webDriver -> webDriver.findElement(By.id("submitNoteButton")));
        submitNoteButton.click();

        // Click "Notes" tab
//        noteTab = webDriverWait.until(webDriver -> webDriver.findElement(By.id("nav-notes-tab")));
        noteTab.click();

        // Wait all note items
        List<WebElement> notes = webDriverWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("#notes-list li")));

        boolean found = false;

        // fetch
        for (WebElement note : notes) {
            String actualTitle = note.findElement(By.cssSelector("#noteTable tbody tr th")).getText();

            if (actualTitle.equals(noteTitle)) {
                found = true;
                break;
            }
        }

        Assertions.assertTrue(found);
    }
}

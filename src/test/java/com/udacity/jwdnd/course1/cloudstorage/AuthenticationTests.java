package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;
import java.time.LocalDateTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationTests {

    @LocalServerPort
    private int port;

    private WebDriver driver;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
        ;
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
    public void signupAndLogout() {
        String username = "SAL-" + LocalDateTime.now();
        this.signup(username, this.driver, this.port);

        this.login(username, "12345678", this.driver, this.port);
        Assertions.assertEquals("Home", driver.getTitle());

        this.logout(this.driver, this.port);
        driver.get("http://localhost:" + this.port + "/home");
        Assertions.assertEquals("Login", driver.getTitle());
    }



    // =========== COMMON BEHAVIORS ============

    void signup(String username, WebDriver driver, int port) {
        // Visit the sign-up page.
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        driver.get("http://localhost:" + port + "/signup");
        webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));

        // Fill out credentials
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
        WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
        inputFirstName.click();
        inputFirstName.sendKeys(username);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
        WebElement inputLastName = driver.findElement(By.id("inputLastName"));
        inputLastName.click();
        inputLastName.sendKeys(username);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
        WebElement inputUsername = driver.findElement(By.id("inputUsername"));
        inputUsername.click();
        inputUsername.sendKeys(username);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
        WebElement inputPassword = driver.findElement(By.id("inputPassword"));
        inputPassword.click();
        inputPassword.sendKeys("12345678");

        // Attempt to sign up.
        webDriverWait.until((ExpectedConditions.visibilityOfElementLocated((By.id("buttonSignUp")))));
        WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
        buttonSignUp.click();

        // Check that the sign up was successful.
//        Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
    }

    void login(String userName, String password, WebDriver driver, int port) {
        // Log in to our dummy account.
        driver.get("http://localhost:" + port + "/login");
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
        WebElement loginUserName = driver.findElement(By.id("inputUsername"));
        loginUserName.click();
        loginUserName.sendKeys(userName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
        WebElement loginPassword = driver.findElement(By.id("inputPassword"));
        loginPassword.click();
        loginPassword.sendKeys(password);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
        WebElement loginButton = driver.findElement(By.id("login-button"));
        loginButton.click();

        webDriverWait.until(ExpectedConditions.titleContains("Home"));
    }

    void logout(WebDriver driver, int port) {
        // Visit the sign-up page.
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        driver.get("http://localhost:" + port + "/home");
        webDriverWait.until(ExpectedConditions.titleContains("Home"));

        // Check is logged in
        Assertions.assertEquals("Home", driver.getTitle());

        // Do logout
        webDriverWait.until((ExpectedConditions.visibilityOfElementLocated((By.id("buttonLogout")))));
        WebElement buttonLogout = driver.findElement(By.id("buttonLogout"));
        buttonLogout.click();

        // Check that the sign up was successful.
        webDriverWait.until(ExpectedConditions.titleContains("Login"));
//        Assertions.assertEquals("Login", driver.getTitle());
    }
}

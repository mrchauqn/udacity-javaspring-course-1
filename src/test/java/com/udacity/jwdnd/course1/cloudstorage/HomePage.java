package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage {

    // ======= NOTES =======

    @FindBy(id = "nav-notes-tab")
    private WebElement navNotesTab;

    @FindBy(id = "note-title")
    private WebElement noteTitleInput;

    @FindBy(id = "note-description")
    private WebElement noteDescriptionInput;

    @FindBy(id = "noteSubmit")
    private WebElement noteSubmit;

    // ======= CREDENTIALS =======

    @FindBy(id = "credential-url")
    private WebElement credentialUrlInput;

    @FindBy(id = "credential-username")
    private WebElement credentialUsernameInput;

    @FindBy(id = "credential-password")
    private WebElement credentialPasswordInput;

    @FindBy(css = "#noteTable > tbody > tr:last-child > th")
    private WebElement newestNoteTitleTxt;

    @FindBy(css = "#noteTable > tbody > tr:last-child > td:first-child > button")
    private WebElement newestNoteEditButton;

    @FindBy(css = "#noteTable > tbody > tr:last-child > td:first-child > a")
    private WebElement newestNoteDeleteButton;

    // ======= CONFIG =======

    private final JavascriptExecutor js;
    private final WebDriverWait wait;

    public HomePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        js = (JavascriptExecutor) driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    // ======= BEHAVIORS =======

    public void goNotesTab() {
        js.executeScript("arguments[0].click();", navNotesTab);
    }

    public void saveNote() {
        js.executeScript("arguments[0].click();", noteSubmit);
    }

    public void clickEditFirstNote() {
        js.executeScript("arguments[0].click();", newestNoteEditButton);
    }

    public void clickDeleteFirstNote() {
        js.executeScript("arguments[0].click();", newestNoteDeleteButton);
    }

    public String getNewestNoteTitle() {
        return wait.until(ExpectedConditions.elementToBeClickable(newestNoteTitleTxt)).getText();
    }

    // ======= SETTERS =======

    public void setNoteDescriptionInput(String noteDescriptionInput) {
        js.executeScript("arguments[0].value='" + noteDescriptionInput + "'", this.noteDescriptionInput);
    }

    public void setNoteTitleInput(String noteTitleInput) {
        js.executeScript("arguments[0].value='" + noteTitleInput + "'", this.noteTitleInput);
    }

    public void setCredentialUrlInput(String credentialUrlInput) {
        js.executeScript("arguments[0].value='" + credentialUrlInput + "'", this.credentialUrlInput);
    }

    public void setCredentialUsernameInput(String credentialUsernameInput) {
        js.executeScript("arguments[0].value='" + credentialUsernameInput + "'", this.credentialUsernameInput);
    }

    public void setCredentialPasswordInput(String credentialPasswordInput) {
        js.executeScript("arguments[0].value='" + credentialPasswordInput + "'", this.credentialPasswordInput);
    }
}

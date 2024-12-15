package com.udacity.jwdnd.course1.cloudstorage;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NoteTests extends CloudStorageApplicationTests {

    @Test
    public void testCreateNote() {
        String noteTitle = "Title " + this.TIME_STAMP;
        String noteDesc = "Description " + this.TIME_STAMP;

        HomePage homePage = signupAndLogin();
        createNote(homePage, noteTitle, noteDesc);
        homePage.goNotesTab();

        Assertions.assertEquals(noteTitle, homePage.getNewestNoteTitle());
    }

    @Test
    public void testUpdateNote() {
        String noteTitle = "Title " + this.TIME_STAMP;
        String noteDesc = "Description " + this.TIME_STAMP;
        HomePage homePage = signupAndLogin();
        createNote(homePage, noteTitle, noteDesc);
        homePage.goNotesTab();

        String newNoteTitle = noteTitle + "U";
        String newNoteDesc = noteDesc + "_updated";
        homePage.clickEditFirstNote();
        homePage.setNoteTitleInput(newNoteTitle);
        homePage.setNoteDescriptionInput(newNoteDesc);
        homePage.saveNote();
        homePage.goNotesTab();

        Assertions.assertEquals(newNoteTitle, homePage.getNewestNoteTitle());
    }

    @Test
    public void testDeleteNote() {
        String noteTitle = "Title " + this.TIME_STAMP;
        String noteDesc = "Description " + this.TIME_STAMP;
        HomePage homePage = signupAndLogin();
        createNote(homePage, noteTitle, noteDesc);
        homePage.goNotesTab();

        homePage.clickDeleteFirstNote();
        homePage.goNotesTab();
        String newestNoteTitle = null;
        try {
            newestNoteTitle = homePage.getNewestNoteTitle();
        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println("Cannot find any note!");
        }

        Assertions.assertNull(newestNoteTitle);
    }

    private void createNote(HomePage homePage, String noteTitle, String noteDesc) {
        homePage.goNotesTab();
        homePage.setNoteTitleInput(noteTitle);
        homePage.setNoteDescriptionInput(noteDesc);
        homePage.saveNote();
    }
}

package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.controller.CredentialController;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.TimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CredentialTests extends CloudStorageApplicationTests {

    @Autowired
    EncryptionService encryptionService;

    @Test
    public void testCreateCredential() {
        String url = "http://news-" + this.TIME_STAMP + ".com";
        String username = "us-" + this.TIME_STAMP;
        String password = "12345678";

        HomePage homePage = signupAndLogin();
        createCredential(homePage, url, username, password);
        homePage.goCredentialsTab();

        Assertions.assertEquals(url, homePage.getNewestCredentialUrl());
        Assertions.assertEquals(username, homePage.getNewestCredentialUsername());
        String encryptPassword = encryptionService.encryptValue(password, CredentialController.SECRET_KEY);
        Assertions.assertEquals(encryptPassword, homePage.getNewestCredentialPassword());
    }

    @Test
    public void testUpdateCredential() {
        String url = "http://news-" + this.TIME_STAMP + ".com";
        String username = "us-" + this.TIME_STAMP;
        String password = "12345678";
        HomePage homePage = signupAndLogin();
        createCredential(homePage, url, username, password);
        homePage.goCredentialsTab();

        String newUrl = url + ".updated";
        String newUsername = username + "_updated";
        String newPassword = password + "_updated";
        homePage.clickEditFirstCredential();
        homePage.setCredentialUrlInput(newUrl);
        homePage.setCredentialUsernameInput(newUsername);
        homePage.setCredentialPasswordInput(newPassword);
        homePage.saveCredential();
        homePage.goCredentialsTab();

        Assertions.assertEquals(newUrl, homePage.getNewestCredentialUrl());
        Assertions.assertEquals(newUsername, homePage.getNewestCredentialUsername());
        String encryptPassword = encryptionService.encryptValue(newPassword, CredentialController.SECRET_KEY);
        Assertions.assertEquals(encryptPassword, homePage.getNewestCredentialPassword());
    }

    @Test
    public void testDeleteCredential() {
        String url = "http://news-" + this.TIME_STAMP + ".com";
        String username = "us-" + this.TIME_STAMP;
        String password = "12345678";
        HomePage homePage = signupAndLogin();
        createCredential(homePage, url, username, password);
        homePage.goCredentialsTab();

        homePage.clickDeleteFirstCredential();
        homePage.goCredentialsTab();
        String newestCredentialUrl = null;
        try {
            newestCredentialUrl = homePage.getNewestCredentialUrl();
        } catch (TimeoutException e) {
            System.out.println("Cannot find any credential!");
        }

        Assertions.assertNull(newestCredentialUrl);
    }

    private void createCredential(HomePage homePage, String url, String username, String basePassword) {
        homePage.goCredentialsTab();
        homePage.setCredentialUrlInput(url);
        homePage.setCredentialUsernameInput(username);
        homePage.setCredentialPasswordInput(basePassword);
        homePage.saveCredential();
    }
}

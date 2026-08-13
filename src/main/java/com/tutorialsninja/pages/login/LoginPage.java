package com.tutorialsninja.pages.login;

import com.tutorialsninja.core.base.BasePage;
import org.openqa.selenium.By;

/**
 * LoginPage — TutorialsNinja Login Page
 *
 * RULE: Only locators + page-specific composite actions.
 * When UI changes → update ONLY the By field below.
 */
public class LoginPage extends BasePage {

    // ════════════════════════════════════════════════════════
    // LOCATORS — update here when UI changes
    // ════════════════════════════════════════════════════════
    public By loginPageHeading   = By.xpath("//h2[text()='Returning Customer']");
    public By emailInput         = By.id("input-email");
    public By passwordInput      = By.id("input-password");
    public By loginButton        = By.cssSelector("input[value='Login']");
    public By forgotPasswordLink = By.linkText("Forgotten Password");
    public By errorMessage       = By.cssSelector("div.alert.alert-danger.alert-dismissible");
    public By newCustomerHeading = By.xpath("//h2[text()='New Customer']");
    public By continueButton     = By.xpath("//div[@class='well']//a[@class='btn btn-primary']");

    // ════════════════════════════════════════════════════════
    // PAGE ACTIONS
    // ════════════════════════════════════════════════════════
    public void login(String email, String password) {
        type(emailInput, email);
        type(passwordInput, password);
        click(loginButton);
    }

    public boolean isLoginPageDisplayed() { return isDisplayed(loginPageHeading); }
    public boolean isErrorDisplayed()     { return isDisplayed(errorMessage);     }
    public String  getErrorMessage()      { return getText(errorMessage);          }
    public void    clickForgotPassword()  { click(forgotPasswordLink);             }
}

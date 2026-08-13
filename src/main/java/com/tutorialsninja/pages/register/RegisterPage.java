package com.tutorialsninja.pages.register;

import com.tutorialsninja.core.base.BasePage;
import org.openqa.selenium.By;

/**
 * RegisterPage — TutorialsNinja Register Page
 *
 * RULE: Only locators + page-specific composite actions.
 * When UI changes → update ONLY the By field below.
 */
public class RegisterPage extends BasePage {

    // ════════════════════════════════════════════════════════
    // LOCATORS — update here when UI changes
    // ════════════════════════════════════════════════════════
    public By registerPageHeading  = By.xpath("//h1[text()='Register Account']");
    public By firstNameInput       = By.id("input-firstname");
    public By lastNameInput        = By.id("input-lastname");
    public By emailInput           = By.id("input-email");
    public By telephoneInput       = By.id("input-telephone");
    public By passwordInput        = By.id("input-password");
    public By confirmPasswordInput = By.id("input-confirm");
    public By newsletterYesRadio   = By.xpath("//input[@name='newsletter'][@value='1']");
    public By newsletterNoRadio    = By.xpath("//input[@name='newsletter'][@value='0']");
    public By privacyPolicyCheckbox= By.name("agree");
    public By continueButton       = By.cssSelector("input[value='Continue']");
    public By successHeading       = By.xpath("//h1[text()='Your Account Has Been Created!']");
    public By errorMessage         = By.cssSelector("div.alert.alert-danger.alert-dismissible");
    public By firstNameError       = By.xpath("//input[@id='input-firstname']/following-sibling::div");
    public By lastNameError        = By.xpath("//input[@id='input-lastname']/following-sibling::div");
    public By emailError           = By.xpath("//input[@id='input-email']/following-sibling::div");
    public By telephoneError       = By.xpath("//input[@id='input-telephone']/following-sibling::div");
    public By passwordError        = By.xpath("//input[@id='input-password']/following-sibling::div");

    // ════════════════════════════════════════════════════════
    // PAGE ACTIONS
    // ════════════════════════════════════════════════════════
    public void enterFirstName(String v)      { type(firstNameInput, v);       }
    public void enterLastName(String v)       { type(lastNameInput, v);        }
    public void enterEmail(String v)          { type(emailInput, v);           }
    public void enterTelephone(String v)      { type(telephoneInput, v);       }
    public void enterPassword(String v)       { type(passwordInput, v);        }
    public void enterConfirmPassword(String v){ type(confirmPasswordInput, v); }
    public void clickPrivacyPolicy()          { click(privacyPolicyCheckbox);  }
    public void clickContinue()               { click(continueButton);         }

    public void selectNewsletter(String option) {
        if (option.equalsIgnoreCase("yes")) click(newsletterYesRadio);
        else click(newsletterNoRadio);
    }

    public boolean isRegisterPageDisplayed() { return isDisplayed(registerPageHeading); }
    public boolean isSuccessDisplayed()      { return isDisplayed(successHeading);       }
    public boolean isErrorDisplayed()        { return isDisplayed(errorMessage);         }
    public String  getErrorMessage()         { return getText(errorMessage);             }
}

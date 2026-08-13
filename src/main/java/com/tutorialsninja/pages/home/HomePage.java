package com.tutorialsninja.pages.home;

import com.tutorialsninja.core.base.BasePage;
import org.openqa.selenium.By;

/**
 * HomePage — TutorialsNinja Home Page
 *
 * RULE: Only locators + page-specific composite actions.
 * All Selenium calls go through BasePage methods.
 * When UI changes → update ONLY the By field below.
 */
public class HomePage extends BasePage {

    // ════════════════════════════════════════════════════════
    // LOCATORS — update here when UI changes
    // ════════════════════════════════════════════════════════
    public By logoImage         = By.cssSelector("#logo");
    public By myAccountDropdown = By.xpath("//span[text()='My Account']");
    public By loginOption       = By.xpath("//ul[@class='dropdown-menu dropdown-menu-right']//a[text()='Login']");
    public By registerOption    = By.xpath("//ul[@class='dropdown-menu dropdown-menu-right']//a[text()='Register']");
    public By searchBox         = By.name("search");
    public By searchButton      = By.cssSelector(".btn.btn-default.btn-lg");
    public By cartButton        = By.cssSelector("#cart > button");
    public By currencyDropdown  = By.cssSelector(".btn-group .btn-link");

    // ════════════════════════════════════════════════════════
    // PAGE ACTIONS
    // ════════════════════════════════════════════════════════
    public void goToHomePage(String url) {
        navigateTo(url);
        waitForVisible(logoImage);
    }

    public void clickMyAccount()  { click(myAccountDropdown); }
    public void clickLogin()      { clickMyAccount(); click(loginOption);    }
    public void clickRegister()   { clickMyAccount(); click(registerOption); }
    public boolean isHomePageDisplayed() { return isDisplayed(logoImage); }

    public void searchProduct(String productName) {
        type(searchBox, productName);
        click(searchButton);
    }
}

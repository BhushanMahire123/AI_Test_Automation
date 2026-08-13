package com.tutorialsninja.pages.account;

import com.tutorialsninja.core.base.BasePage;
import org.openqa.selenium.By;

/**
 * AccountPage — TutorialsNinja My Account Page (after login)
 */
public class AccountPage extends BasePage {

    // ════════════════════════════════════════════════════════
    // LOCATORS — update here when UI changes
    // ════════════════════════════════════════════════════════
    public By accountPageHeading = By.xpath("//h2[text()='My Account']");
    public By myAccountDropdown  = By.xpath("//span[text()='My Account']");
    public By logoutOption       = By.xpath("//ul[@class='dropdown-menu dropdown-menu-right']//a[text()='Logout']");
    public By editAccountLink    = By.linkText("Edit Account");
    public By orderHistoryLink   = By.linkText("Order History");

    // ════════════════════════════════════════════════════════
    // PAGE ACTIONS
    // ════════════════════════════════════════════════════════
    public boolean isAccountPageDisplayed() { return isDisplayed(accountPageHeading); }

    public void logout() {
        click(myAccountDropdown);
        click(logoutOption);
    }
}

package com.tutorialsninja.core.base;

import com.tutorialsninja.config.ConfigManager;
import com.tutorialsninja.core.driver.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * BasePage — Single source of truth for ALL Selenium interactions.
 *
 * Rules:
 *  - All raw Selenium API calls live HERE only
 *  - Page classes call BasePage methods — never touch WebDriver directly
 *  - Every method handles waits internally
 */
public abstract class BasePage {

    private static final Logger log = LogManager.getLogger(BasePage.class);
    private static final int TIMEOUT = ConfigManager.getExplicitWait();

    // ── Driver ───────────────────────────────────────────────
    protected WebDriver getDriver() { return DriverFactory.getDriver(); }

    protected WebDriverWait getWait() {
        return new WebDriverWait(getDriver(), Duration.ofSeconds(TIMEOUT));
    }

    protected WebDriverWait getWait(int seconds) {
        return new WebDriverWait(getDriver(), Duration.ofSeconds(seconds));
    }

    // ── Click ────────────────────────────────────────────────
    public void click(By locator) {
        log.info("Click: {}", locator);
        waitForClickable(locator).click();
    }

    public void jsClick(By locator) {
        log.info("JS Click: {}", locator);
        ((JavascriptExecutor) getDriver())
            .executeScript("arguments[0].click();", waitForVisible(locator));
    }

    public void doubleClick(By locator) {
        log.info("Double Click: {}", locator);
        new Actions(getDriver()).doubleClick(waitForClickable(locator)).perform();
    }

    public void rightClick(By locator) {
        log.info("Right Click: {}", locator);
        new Actions(getDriver()).contextClick(waitForVisible(locator)).perform();
    }

    // ── Type ─────────────────────────────────────────────────
    public void type(By locator, String text) {
        log.info("Type '{}' into: {}", text, locator);
        WebElement el = waitForVisible(locator);
        el.clear();
        el.sendKeys(text);
    }

    public void typeWithoutClear(By locator, String text) {
        waitForVisible(locator).sendKeys(text);
    }

    public void pressKey(By locator, Keys key) {
        waitForVisible(locator).sendKeys(key);
    }

    public void clear(By locator) {
        waitForVisible(locator).clear();
    }

    // ── Dropdown ─────────────────────────────────────────────
    public void selectByVisibleText(By locator, String text) {
        log.info("Select '{}' from: {}", text, locator);
        new Select(waitForVisible(locator)).selectByVisibleText(text);
    }

    public void selectByValue(By locator, String value) {
        new Select(waitForVisible(locator)).selectByValue(value);
    }

    public void selectByIndex(By locator, int index) {
        new Select(waitForVisible(locator)).selectByIndex(index);
    }

    // ── Get Text / Attribute ─────────────────────────────────
    public String getText(By locator) {
        String text = waitForVisible(locator).getText().trim();
        log.info("getText({}) = '{}'", locator, text);
        return text;
    }

    public String getAttribute(By locator, String attr) {
        return waitForVisible(locator).getAttribute(attr);
    }

    public String getValue(By locator) { return getAttribute(locator, "value"); }
    public String getTitle()           { return getDriver().getTitle(); }
    public String getCurrentUrl()      { return getDriver().getCurrentUrl(); }

    // ── State Checks ─────────────────────────────────────────
    public boolean isDisplayed(By locator) {
        try { return getDriver().findElement(locator).isDisplayed(); }
        catch (NoSuchElementException e) { return false; }
    }

    public boolean isEnabled(By locator) {
        try { return getDriver().findElement(locator).isEnabled(); }
        catch (NoSuchElementException e) { return false; }
    }

    public boolean isSelected(By locator) {
        try { return getDriver().findElement(locator).isSelected(); }
        catch (NoSuchElementException e) { return false; }
    }

    // ── Waits ─────────────────────────────────────────────────
    public WebElement waitForVisible(By locator) {
        return getWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForVisible(By locator, int seconds) {
        return getWait(seconds).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForClickable(By locator) {
        return getWait().until(ExpectedConditions.elementToBeClickable(locator));
    }

    public boolean waitForUrlContains(String text) {
        return getWait().until(ExpectedConditions.urlContains(text));
    }

    public boolean waitForInvisible(By locator) {
        return getWait().until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public void waitForPageLoad() {
        getWait().until(d ->
            ((JavascriptExecutor) d)
                .executeScript("return document.readyState").equals("complete"));
    }

    // ── Scroll ───────────────────────────────────────────────
    public void scrollToElement(By locator) {
        ((JavascriptExecutor) getDriver()).executeScript(
            "arguments[0].scrollIntoView({block:'center'});",
            getDriver().findElement(locator));
    }

    public void scrollToTop() {
        ((JavascriptExecutor) getDriver()).executeScript("window.scrollTo(0,0);");
    }

    // ── Hover ────────────────────────────────────────────────
    public void hoverOver(By locator) {
        new Actions(getDriver()).moveToElement(waitForVisible(locator)).perform();
    }

    // ── Navigation ───────────────────────────────────────────
    public void navigateTo(String url) {
        log.info("Navigate to: {}", url);
        getDriver().get(url);
        waitForPageLoad();
    }

    public void refreshPage() {
        getDriver().navigate().refresh();
        waitForPageLoad();
    }

    // ── Alert ────────────────────────────────────────────────
    public void acceptAlert() {
        getWait().until(ExpectedConditions.alertIsPresent()).accept();
    }

    public void dismissAlert() {
        getWait().until(ExpectedConditions.alertIsPresent()).dismiss();
    }

    public String getAlertText() {
        return getWait().until(ExpectedConditions.alertIsPresent()).getText();
    }

    // ── Frame ─────────────────────────────────────────────────
    public void switchToFrame(By locator) {
        getDriver().switchTo().frame(getDriver().findElement(locator));
    }

    public void switchToDefaultContent() {
        getDriver().switchTo().defaultContent();
    }

    // ── Helpers ──────────────────────────────────────────────
    protected WebElement findElement(By locator) {
        return getWait().until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    protected List<WebElement> findElements(By locator) {
        return getDriver().findElements(locator);
    }

    public byte[] takeScreenshot() {
        return ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BYTES);
    }
}

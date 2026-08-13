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

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
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

    private static final Logger log =
            LogManager.getLogger(BasePage.class);

    private static final int TIMEOUT =
            ConfigManager.getExplicitWait();

    // ─────────────────────────────────────────────────────────
    // Driver
    // ─────────────────────────────────────────────────────────

    protected WebDriver getDriver() {
        return DriverFactory.getDriver();
    }

    protected WebDriverWait getWait() {
        return new WebDriverWait(
                getDriver(),
                Duration.ofSeconds(TIMEOUT)
        );
    }

    protected WebDriverWait getWait(int seconds) {
        return new WebDriverWait(
                getDriver(),
                Duration.ofSeconds(seconds)
        );
    }

    // ─────────────────────────────────────────────────────────
    // Click
    // ─────────────────────────────────────────────────────────

    public void click(By locator) {

        log.info("==============================================");
        log.info("CLICK START");
        log.info("Locator      : {}", locator);
        log.info("Current URL  : {}", getDriver().getCurrentUrl());
        log.info("Page Title   : {}", getDriver().getTitle());

        try {

            boolean myAccountPresent =
                    getDriver()
                            .getPageSource()
                            .contains("My Account");

            log.info(
                    "Page contains 'My Account': {}",
                    myAccountPresent
            );

            log.info(
                    "Element present: {}",
                    !getDriver().findElements(locator).isEmpty()
            );

            WebElement element = waitForClickable(locator);

            log.info(
                    "Element visible : {}",
                    element.isDisplayed()
            );

            log.info(
                    "Element enabled : {}",
                    element.isEnabled()
            );

            // Make sure element is inside the viewport
            scrollElementIntoView(element);

            log.info("Attempting normal Selenium click...");

            element.click();

            log.info("CLICK SUCCESS: {}", locator);
            log.info("==============================================");

        } catch (TimeoutException e) {

            log.error("CLICK TIMEOUT");
            log.error("Locator     : {}", locator);
            log.error("Current URL : {}", getDriver().getCurrentUrl());
            log.error("Page Title  : {}", getDriver().getTitle());

            try {
                log.error(
                        "Element count: {}",
                        getDriver().findElements(locator).size()
                );

                log.error(
                        "My Account present: {}",
                        getDriver()
                                .getPageSource()
                                .contains("My Account")
                );
            } catch (Exception ignored) {
                log.error("Unable to inspect page after timeout.");
            }

            takeFailureScreenshot("click-timeout");

            log.error("==============================================");

            throw e;

        } catch (ElementClickInterceptedException e) {

            log.error(
                    "CLICK INTERCEPTED for locator: {}",
                    locator
            );

            log.error(
                    "Current URL: {}",
                    getDriver().getCurrentUrl()
            );

            takeFailureScreenshot("click-intercepted");

            throw e;

        } catch (Exception e) {

            log.error(
                    "CLICK FAILED for locator: {}",
                    locator,
                    e
            );

            takeFailureScreenshot("click-failed");

            throw e;
        }
    }

    public void jsClick(By locator) {

        log.info("JS Click: {}", locator);

        WebElement element = waitForVisible(locator);

        scrollElementIntoView(element);

        ((JavascriptExecutor) getDriver())
                .executeScript(
                        "arguments[0].click();",
                        element
                );
    }

    public void doubleClick(By locator) {

        log.info("Double Click: {}", locator);

        WebElement element = waitForClickable(locator);

        scrollElementIntoView(element);

        new Actions(getDriver())
                .doubleClick(element)
                .perform();
    }

    public void rightClick(By locator) {

        log.info("Right Click: {}", locator);

        WebElement element = waitForVisible(locator);

        scrollElementIntoView(element);

        new Actions(getDriver())
                .contextClick(element)
                .perform();
    }

    // ─────────────────────────────────────────────────────────
    // Type
    // ─────────────────────────────────────────────────────────

    public void type(By locator, String text) {

        log.info(
                "Type '{}' into: {}",
                text,
                locator
        );

        WebElement element = waitForVisible(locator);

        scrollElementIntoView(element);

        element.clear();
        element.sendKeys(text);
    }

    public void typeWithoutClear(
            By locator,
            String text) {

        waitForVisible(locator)
                .sendKeys(text);
    }

    public void pressKey(
            By locator,
            Keys key) {

        waitForVisible(locator)
                .sendKeys(key);
    }

    public void clear(By locator) {

        log.info("Clear: {}", locator);

        waitForVisible(locator)
                .clear();
    }

    // ─────────────────────────────────────────────────────────
    // Dropdown
    // ─────────────────────────────────────────────────────────

    public void selectByVisibleText(
            By locator,
            String text) {

        log.info(
                "Select '{}' from: {}",
                text,
                locator
        );

        new Select(
                waitForVisible(locator)
        ).selectByVisibleText(text);
    }

    public void selectByValue(
            By locator,
            String value) {

        new Select(
                waitForVisible(locator)
        ).selectByValue(value);
    }

    public void selectByIndex(
            By locator,
            int index) {

        new Select(
                waitForVisible(locator)
        ).selectByIndex(index);
    }

    // ─────────────────────────────────────────────────────────
    // Get Text / Attribute
    // ─────────────────────────────────────────────────────────

    public String getText(By locator) {

        String text =
                waitForVisible(locator)
                        .getText()
                        .trim();

        log.info(
                "getText({}) = '{}'",
                locator,
                text
        );

        return text;
    }

    public String getAttribute(
            By locator,
            String attr) {

        return waitForVisible(locator)
                .getAttribute(attr);
    }

    public String getValue(By locator) {
        return getAttribute(locator, "value");
    }

    public String getTitle() {
        return getDriver().getTitle();
    }

    public String getCurrentUrl() {
        return getDriver().getCurrentUrl();
    }

    // ─────────────────────────────────────────────────────────
    // State Checks
    // ─────────────────────────────────────────────────────────

    public boolean isDisplayed(By locator) {

        try {

            return getDriver()
                    .findElement(locator)
                    .isDisplayed();

        } catch (NoSuchElementException e) {

            return false;
        }
    }

    public boolean isEnabled(By locator) {

        try {

            return getDriver()
                    .findElement(locator)
                    .isEnabled();

        } catch (NoSuchElementException e) {

            return false;
        }
    }

    public boolean isSelected(By locator) {

        try {

            return getDriver()
                    .findElement(locator)
                    .isSelected();

        } catch (NoSuchElementException e) {

            return false;
        }
    }

    // ─────────────────────────────────────────────────────────
    // Waits
    // ─────────────────────────────────────────────────────────

    public WebElement waitForVisible(By locator) {

        return getWait()
                .until(
                        ExpectedConditions
                                .visibilityOfElementLocated(locator)
                );
    }

    public WebElement waitForVisible(
            By locator,
            int seconds) {

        return getWait(seconds)
                .until(
                        ExpectedConditions
                                .visibilityOfElementLocated(locator)
                );
    }

    public WebElement waitForClickable(By locator) {

        return getWait()
                .until(
                        ExpectedConditions
                                .elementToBeClickable(locator)
                );
    }

    public boolean waitForUrlContains(
            String text) {

        return getWait()
                .until(
                        ExpectedConditions
                                .urlContains(text)
                );
    }

    public boolean waitForInvisible(
            By locator) {

        return getWait()
                .until(
                        ExpectedConditions
                                .invisibilityOfElementLocated(locator)
                );
    }

    public void waitForPageLoad() {

        getWait().until(driver -> {

            Object state =
                    ((JavascriptExecutor) driver)
                            .executeScript(
                                    "return document.readyState"
                            );

            return "complete".equals(state);
        });
    }

    // ─────────────────────────────────────────────────────────
    // Scroll
    // ─────────────────────────────────────────────────────────

    public void scrollToElement(By locator) {

        WebElement element =
                getDriver().findElement(locator);

        scrollElementIntoView(element);
    }

    private void scrollElementIntoView(
            WebElement element) {

        try {

            ((JavascriptExecutor) getDriver())
                    .executeScript(
                            "arguments[0].scrollIntoView({block:'center', inline:'nearest'});",
                            element
                    );

        } catch (Exception e) {

            log.warn(
                    "Unable to scroll element into view.",
                    e
            );
        }
    }

    public void scrollToTop() {

        ((JavascriptExecutor) getDriver())
                .executeScript(
                        "window.scrollTo(0,0);"
                );
    }

    // ─────────────────────────────────────────────────────────
    // Hover
    // ─────────────────────────────────────────────────────────

    public void hoverOver(By locator) {

        WebElement element =
                waitForVisible(locator);

        scrollElementIntoView(element);

        new Actions(getDriver())
                .moveToElement(element)
                .perform();
    }

    // ─────────────────────────────────────────────────────────
    // Navigation
    // ─────────────────────────────────────────────────────────

    public void navigateTo(String url) {

        log.info("Navigate to: {}", url);

        getDriver().get(url);

        waitForPageLoad();

        log.info(
                "Navigation completed. Current URL: {}",
                getDriver().getCurrentUrl()
        );

        log.info(
                "Page title: {}",
                getDriver().getTitle()
        );
    }

    public void refreshPage() {

        getDriver()
                .navigate()
                .refresh();

        waitForPageLoad();
    }

    // ─────────────────────────────────────────────────────────
    // Alert
    // ─────────────────────────────────────────────────────────

    public void acceptAlert() {

        getWait()
                .until(
                        ExpectedConditions.alertIsPresent()
                )
                .accept();
    }

    public void dismissAlert() {

        getWait()
                .until(
                        ExpectedConditions.alertIsPresent()
                )
                .dismiss();
    }

    public String getAlertText() {

        return getWait()
                .until(
                        ExpectedConditions.alertIsPresent()
                )
                .getText();
    }

    // ─────────────────────────────────────────────────────────
    // Frame
    // ─────────────────────────────────────────────────────────

    public void switchToFrame(By locator) {

        getWait().until(
                ExpectedConditions
                        .frameToBeAvailableAndSwitchToIt(locator)
        );
    }

    public void switchToDefaultContent() {

        getDriver()
                .switchTo()
                .defaultContent();
    }

    // ─────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────

    protected WebElement findElement(By locator) {

        return getWait()
                .until(
                        ExpectedConditions
                                .presenceOfElementLocated(locator)
                );
    }

    protected List<WebElement> findElements(
            By locator) {

        return getDriver()
                .findElements(locator);
    }

    // ─────────────────────────────────────────────────────────
    // Screenshot
    // ─────────────────────────────────────────────────────────

    public byte[] takeScreenshot() {

        return ((TakesScreenshot) getDriver())
                .getScreenshotAs(OutputType.BYTES);
    }

    private void takeFailureScreenshot(
            String name) {

        try {

            File source =
                    ((TakesScreenshot) getDriver())
                            .getScreenshotAs(
                                    OutputType.FILE
                            );

            Path directory =
                    Path.of(
                            "target",
                            "debug-screenshots"
                    );

            Files.createDirectories(directory);

            Path destination =
                    directory.resolve(
                            name + "-" +
                            System.currentTimeMillis() +
                            ".png"
                    );

            Files.copy(
                    source.toPath(),
                    destination,
                    StandardCopyOption.REPLACE_EXISTING
            );

            log.error(
                    "Failure screenshot saved: {}",
                    destination.toAbsolutePath()
            );

        } catch (Exception screenshotError) {

            log.error(
                    "Unable to save failure screenshot.",
                    screenshotError
            );
        }
    }
}
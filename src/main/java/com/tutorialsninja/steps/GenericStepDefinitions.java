package com.tutorialsninja.steps;

import com.tutorialsninja.core.base.BasePage;
import com.tutorialsninja.core.base.LocatorRepository;
import com.tutorialsninja.core.base.PageObjectRegistry;
import com.tutorialsninja.enums.ActionType;
import com.tutorialsninja.enums.ElementType;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

/**
 * GenericStepDefinitions — Universal BDD Step Dispatcher.
 *
 * ONE step definition handles ALL UI interactions.
 * No need to write separate step definitions for each action.
 *
 * ┌─────────────────────────────────────────────────────────┐
 * │ FORMAT 1 — DataTable (multi-step sequence):             │
 * │ When user performs the following actions:               │
 * │   | Summary | Element | Action | Value | Page | Locator │
 * │                                                         │
 * │ FORMAT 2 — Inline (single step):                        │
 * │ When "summary", "Element", "Action", "Value", "Page",   │
 * │      "Locator"                                          │
 * └─────────────────────────────────────────────────────────┘
 */
public class GenericStepDefinitions {

    private static final Logger log = LogManager.getLogger(GenericStepDefinitions.class);
    private final PageObjectRegistry pageRegistry = new PageObjectRegistry();
    private final ThreadLocal<String> lastResult  = new ThreadLocal<>();

    // ─────────────────────────────────────────────────────────
    // FORMAT 1 — DataTable
    // ─────────────────────────────────────────────────────────

    @When("user performs the following actions:")
    public void userPerformsActions(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        log.info("Executing {} step(s)", rows.size());
        for (Map<String, String> row : rows) {
            executeStep(row);
        }
    }

    // ─────────────────────────────────────────────────────────
    // FORMAT 2 — Inline step
    // When "Click login", "Button", "Click", "NA", "LoginPage", "loginButton"
    // ─────────────────────────────────────────────────────────

    @When("{string}, {string}, {string}, {string}, {string}, {string}")
    public void inlineStep(String summary, String element, String action,
                           String value, String page, String locator) {
        log.info("Inline Step: [{}] | {}|{}|{}|{}|{}",
            summary, element, action, value, page, locator);
        executeStep(Map.of(
            "Summary", summary, "Element", element, "Action", action,
            "Value", value,     "Page", page,       "Locator", locator
        ));
    }

    // ─────────────────────────────────────────────────────────
    // Assertion Steps
    // ─────────────────────────────────────────────────────────

    @Then("the result should be {string}")
    public void theResultShouldBe(String expected) {
        Assert.assertEquals(lastResult.get(), expected,
            "Result mismatch. Expected: '" + expected + "' Actual: '" + lastResult.get() + "'");
    }

    @Then("the result should contain {string}")
    public void theResultShouldContain(String expected) {
        String actual = lastResult.get();
        Assert.assertTrue(actual != null && actual.contains(expected),
            "Expected to contain '" + expected + "' but got: '" + actual + "'");
    }

    // ─────────────────────────────────────────────────────────
    // CORE DISPATCHER
    // ─────────────────────────────────────────────────────────

    private void executeStep(Map<String, String> row) {
        String summary    = row.getOrDefault("Summary", "");
        String element    = row.getOrDefault("Element", "");
        String action     = row.getOrDefault("Action", "");
        String value      = row.getOrDefault("Value", "NA");
        String pageName   = row.getOrDefault("Page", "");
        String locatorName= row.getOrDefault("Locator", "");

        log.info(">> [{}] | Page:{} | Locator:{} | Action:{} | Value:{}",
            summary, pageName, locatorName, action, value);

        ElementType elementType = ElementType.fromString(element);
        ActionType  actionType  = ActionType.fromString(action);

        // Browser-level actions — no locator needed
        if (elementType == ElementType.BROWSER) {
            executeBrowserAction(actionType, value, pageName);
            return;
        }

        // Pure wait — no locator needed
        if (elementType == ElementType.WAIT && actionType == ActionType.WAIT) {
            safeSleep(Long.parseLong(value.equalsIgnoreCase("NA") ? "1000" : value));
            return;
        }

        // Resolve page + locator and dispatch
        BasePage page   = pageRegistry.getPage(pageName);
        By locator      = LocatorRepository.resolve(page, locatorName);
        dispatch(page, locator, actionType, value);
    }

    private void dispatch(BasePage page, By locator, ActionType actionType, String value) {
        switch (actionType) {

            case WAIT_FOR_DISPLAY:
                page.waitForVisible(locator);
                log.info("  WaitForDisplay OK: {}", locator);
                break;

            case IS_DISPLAYED:
                boolean displayed = page.isDisplayed(locator);
                lastResult.set(String.valueOf(displayed));
                Assert.assertTrue(displayed, "Element not displayed: " + locator);
                log.info("  IsDisplayed=true: {}", locator);
                break;

            case IS_ENABLED:
                boolean enabled = page.isEnabled(locator);
                lastResult.set(String.valueOf(enabled));
                Assert.assertTrue(enabled, "Element not enabled: " + locator);
                log.info("  IsEnabled=true: {}", locator);
                break;

            case IS_SELECTED:
                boolean selected = page.isSelected(locator);
                lastResult.set(String.valueOf(selected));
                Assert.assertTrue(selected, "Element not selected: " + locator);
                log.info("  IsSelected=true: {}", locator);
                break;

            case CLICK:
                page.click(locator);
                log.info("  Clicked: {}", locator);
                break;

            case DOUBLE_CLICK:
                page.doubleClick(locator);
                log.info("  DoubleClicked: {}", locator);
                break;

            case JS_CLICK:
                page.jsClick(locator);
                log.info("  JSClicked: {}", locator);
                break;

            case RIGHT_CLICK:
                page.rightClick(locator);
                log.info("  RightClicked: {}", locator);
                break;

            case TYPE:
                page.type(locator, resolveValue(value));
                log.info("  Typed '{}': {}", value, locator);
                break;

            case CLEAR:
                page.clear(locator);
                log.info("  Cleared: {}", locator);
                break;

            case SELECT:
                page.selectByVisibleText(locator, resolveValue(value));
                log.info("  Selected '{}': {}", value, locator);
                break;

            case GET_TEXT:
                String text = page.getText(locator);
                lastResult.set(text);
                log.info("  GetText='{}': {}", text, locator);
                break;

            case GET_ATTRIBUTE:
                String attr = page.getAttribute(locator, resolveValue(value));
                lastResult.set(attr);
                log.info("  GetAttribute({})='{}': {}", value, attr, locator);
                break;

            case VERIFY_TEXT:
                String actual = page.getText(locator);
                Assert.assertEquals(actual, resolveValue(value),
                    "Text mismatch. Expected:'" + value + "' Actual:'" + actual + "'");
                log.info("  VerifyText OK: '{}'", value);
                break;

            case VERIFY_CONTAINS:
                String actualText = page.getText(locator);
                Assert.assertTrue(actualText.contains(resolveValue(value)),
                    "Text '" + value + "' not found in '" + actualText + "'");
                log.info("  VerifyContains OK: '{}'", value);
                break;

            case SCROLL:
                page.scrollToElement(locator);
                log.info("  Scrolled to: {}", locator);
                break;

            case HOVER:
                page.hoverOver(locator);
                log.info("  Hovered: {}", locator);
                break;

            case ACCEPT_ALERT:
                page.acceptAlert();
                log.info("  Alert accepted");
                break;

            case DISMISS_ALERT:
                page.dismissAlert();
                log.info("  Alert dismissed");
                break;

            default:
                throw new UnsupportedOperationException(
                    "ActionType '" + actionType + "' not implemented in dispatch()");
        }
    }

    private void executeBrowserAction(ActionType action, String value, String pageName) {
        switch (action) {
            case NAVIGATE:
                pageRegistry.getPage(pageName).navigateTo(value);
                break;
            case WAIT:
                safeSleep(Long.parseLong(value));
                break;
            default:
                throw new UnsupportedOperationException("Browser action not supported: " + action);
        }
    }

    private String resolveValue(String value) {
        if (value == null || value.equalsIgnoreCase("NA")) return "";
        if (value.startsWith("${") && value.endsWith("}")) {
            String key = value.substring(2, value.length() - 1);
            return System.getProperty(key, key);
        }
        return value;
    }

    private void safeSleep(long ms) {
        try { Thread.sleep(ms); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}

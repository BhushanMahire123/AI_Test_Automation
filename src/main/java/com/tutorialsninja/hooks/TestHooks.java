package com.tutorialsninja.hooks;

import com.tutorialsninja.config.ConfigManager;
import com.tutorialsninja.core.driver.DriverFactory;
import com.tutorialsninja.utils.report.ExtentReportManager;
import com.tutorialsninja.utils.report.ScreenshotUtil;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestHooks {

    private static final Logger log = LogManager.getLogger(TestHooks.class);

    @Before(order = 1)
    public void beforeScenario(Scenario scenario) {
        log.info("══════════════════════════════════════");
        log.info("STARTING: {}", scenario.getName());
        log.info("Tags: {}", scenario.getSourceTagNames());
        log.info("══════════════════════════════════════");
        DriverFactory.initDriver();
        DriverFactory.getDriver().get(ConfigManager.getUrl());
        ExtentReportManager.createTest(
            scenario.getName(),
            String.join(", ", scenario.getSourceTagNames()));
    }

    @AfterStep
    public void afterStep(Scenario scenario) {
        if (scenario.isFailed()) {
            try {
                byte[] screenshot = ScreenshotUtil.captureScreenshot(DriverFactory.getDriver());
                if (screenshot != null) {
                    scenario.attach(screenshot, "image/png", "step-failure");
                    ExtentReportManager.attachScreenshot(
                        ScreenshotUtil.saveScreenshot(
                            DriverFactory.getDriver(), scenario.getName()));
                }
            } catch (Exception e) {
                log.error("Screenshot failed: {}", e.getMessage());
            }
        }
    }

    @After(order = 1)
    public void afterScenario(Scenario scenario) {
        try {
            if (scenario.isFailed()) {
                log.error("FAILED: {}", scenario.getName());
                ExtentReportManager.logFail("Scenario FAILED: " + scenario.getName());
            } else {
                log.info("PASSED: {}", scenario.getName());
                ExtentReportManager.logPass("Scenario PASSED: " + scenario.getName());
            }
        } finally {
            DriverFactory.quitDriver();
            log.info("══════════════════════════════════════\n");
        }
    }
}

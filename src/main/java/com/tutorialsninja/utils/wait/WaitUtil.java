package com.tutorialsninja.utils.wait;

import com.tutorialsninja.core.driver.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitUtil {

    private WaitUtil() {}

    public static void waitForPageStability() {
        new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(30))
            .until(d -> ((JavascriptExecutor) d)
                .executeScript("return document.readyState").equals("complete"));
    }

    public static void waitForSpinnerToDisappear(By spinnerLocator) {
        try {
            new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(spinnerLocator));
            new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(30))
                .until(ExpectedConditions.invisibilityOfElementLocated(spinnerLocator));
        } catch (Exception ignored) {}
    }

    public static void hardWait(long ms) {
        try { Thread.sleep(ms); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}

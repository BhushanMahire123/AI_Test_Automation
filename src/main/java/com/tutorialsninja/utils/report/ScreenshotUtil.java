package com.tutorialsninja.utils.report;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotUtil {

    private static final Logger log = LogManager.getLogger(ScreenshotUtil.class);
    private static final String DIR = "target/screenshots/";

    private ScreenshotUtil() {}

    public static byte[] captureScreenshot(WebDriver driver) {
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            log.error("Capture failed: {}", e.getMessage());
            return null;
        }
    }

    public static String saveScreenshot(WebDriver driver, String scenarioName) {
        try {
            new File(DIR).mkdirs();
            String ts      = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String name    = scenarioName.replaceAll("[^a-zA-Z0-9]", "_");
            String path    = DIR + name + "_" + ts + ".png";
            byte[] bytes   = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Files.write(Paths.get(path), bytes);
            log.info("Screenshot saved: {}", path);
            return path;
        } catch (Exception e) {
            log.error("Save failed: {}", e.getMessage());
            return null;
        }
    }
}

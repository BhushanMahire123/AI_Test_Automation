package com.tutorialsninja.utils.report;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.tutorialsninja.config.ConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExtentReportManager {

    private static final Logger log = LogManager.getLogger(ExtentReportManager.class);
    private static ExtentReports extentReports;
    private static final ThreadLocal<ExtentTest> testThread = new ThreadLocal<>();
    private static final String TIMESTAMP = LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));

    static { initReports(); }

    private ExtentReportManager() {}

    private static synchronized void initReports() {
        String path = "target/extent-reports/TutorialsNinja-" + TIMESTAMP + ".html";
        new File("target/extent-reports").mkdirs();
        ExtentSparkReporter spark = new ExtentSparkReporter(path);
        spark.config().setReportName("TutorialsNinja Automation Report");
        spark.config().setDocumentTitle("TutorialsNinja Test Results");
        spark.config().setTheme(Theme.DARK);
        extentReports = new ExtentReports();
        extentReports.attachReporter(spark);
        extentReports.setSystemInfo("Framework", "TutorialsNinja Automation");
        extentReports.setSystemInfo("Browser",   ConfigManager.getBrowser());
        extentReports.setSystemInfo("URL",        ConfigManager.getUrl());
        log.info("ExtentReports initialized: {}", path);
    }

    public static synchronized void createTest(String name, String tags) {
        ExtentTest test = extentReports.createTest(name)
            .assignCategory(tags.isEmpty() ? "Untagged" : tags);
        testThread.set(test);
    }

    public static ExtentTest getTest()        { return testThread.get(); }
    public static void logPass(String msg)    { if (getTest() != null) getTest().pass(msg);    }
    public static void logFail(String msg)    { if (getTest() != null) getTest().fail(msg);    }
    public static void logInfo(String msg)    { if (getTest() != null) getTest().info(msg);    }
    public static void logWarning(String msg) { if (getTest() != null) getTest().warning(msg); }

    public static void attachScreenshot(String path) {
        try {
            if (getTest() != null && path != null)
                getTest().fail("Screenshot:",
                    MediaEntityBuilder.createScreenCaptureFromPath(path).build());
        } catch (Exception e) {
            log.error("Failed to attach screenshot", e);
        }
    }

    public static synchronized void flushReports() {
        if (extentReports != null) {
            extentReports.flush();
            log.info("ExtentReports flushed");
        }
    }
}

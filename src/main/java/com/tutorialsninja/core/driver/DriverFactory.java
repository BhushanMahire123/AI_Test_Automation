package com.tutorialsninja.core.driver;

import com.tutorialsninja.config.ConfigManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

/**
 * DriverFactory
 *
 * Responsibilities:
 *  - Creates browser driver
 *  - Supports Chrome / Firefox / Edge
 *  - Supports headless execution
 *  - ThreadLocal for parallel execution
 *  - Jenkins/CI friendly browser configuration
 */
public final class DriverFactory {

    private static final Logger log =
            LogManager.getLogger(DriverFactory.class);

    private static final ThreadLocal<WebDriver> driverThread =
            new ThreadLocal<>();

    private DriverFactory() {
        // Utility class
    }

    // =========================================================
    // INIT DRIVER
    // =========================================================

    public static void initDriver() {

        String browser = ConfigManager.getBrowser();
        boolean headless = ConfigManager.isHeadless();

        log.info("==============================================");
        log.info("Initializing WebDriver");
        log.info("Browser  : {}", browser);
        log.info("Headless : {}", headless);
        log.info("Thread   : {}", Thread.currentThread().getId());
        log.info("==============================================");

        WebDriver driver = createDriver(browser, headless);

        // Implicit wait
        driver.manage().timeouts().implicitlyWait(
                Duration.ofSeconds(
                        ConfigManager.getImplicitWait()
                )
        );

        // Page load timeout
        driver.manage().timeouts().pageLoadTimeout(
                Duration.ofSeconds(30)
        );

        // Script timeout
        driver.manage().timeouts().scriptTimeout(
                Duration.ofSeconds(30)
        );

        /*
         * IMPORTANT:
         *
         * Do NOT use maximize() in headless mode.
         *
         * ChromeOptions already contains:
         * --window-size=1920,1080
         */
        if (!headless) {
            try {
                driver.manage().window().maximize();
            } catch (Exception e) {
                log.warn(
                        "Could not maximize browser: {}",
                        e.getMessage()
                );
            }
        }

        driverThread.set(driver);

        log.info("WebDriver initialized successfully");
        log.info("Driver : {}", driver);
        log.info("Thread : {}", Thread.currentThread().getId());
    }

    // =========================================================
    // GET DRIVER
    // =========================================================

    public static WebDriver getDriver() {

        WebDriver driver = driverThread.get();

        if (driver == null) {
            throw new IllegalStateException(
                    "WebDriver is not initialized! " +
                    "Call DriverFactory.initDriver() first."
            );
        }

        return driver;
    }

    // =========================================================
    // QUIT DRIVER
    // =========================================================

    public static void quitDriver() {

        WebDriver driver = driverThread.get();

        if (driver != null) {

            try {
                driver.quit();

                log.info(
                        "WebDriver closed successfully | thread={}",
                        Thread.currentThread().getId()
                );

            } catch (Exception e) {

                log.warn(
                        "Error while closing WebDriver: {}",
                        e.getMessage()
                );

            } finally {

                driverThread.remove();
            }
        }
    }

    // =========================================================
    // DRIVER CREATION
    // =========================================================

    private static WebDriver createDriver(
            String browser,
            boolean headless) {

        if (browser == null || browser.trim().isEmpty()) {
            browser = "chrome";
        }

        switch (browser.toLowerCase().trim()) {

            case "firefox":
                return createFirefoxDriver(headless);

            case "edge":
                return createEdgeDriver(headless);

            case "chrome":
            default:
                return createChromeDriver(headless);
        }
    }

    // =========================================================
    // CHROME
    // =========================================================

    private static WebDriver createChromeDriver(
            boolean headless) {

        log.info(
                "Creating ChromeDriver | headless={}",
                headless
        );

        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();

        /*
         * Jenkins / CI friendly arguments
         */
        options.addArguments(
                "--window-size=1920,1080",
                "--disable-gpu",
                "--disable-dev-shm-usage",
                "--no-sandbox",
                "--disable-notifications",
                "--disable-popup-blocking",
                "--remote-allow-origins=*",
                "--disable-extensions",
                "--disable-infobars"
        );

        /*
         * Headless execution
         */
        if (headless) {
            options.addArguments("--headless=new");

            log.info("Chrome running in HEADLESS mode");

        } else {

            log.info("Chrome running in NORMAL mode");
        }

        WebDriver driver = new ChromeDriver(options);

        log.info(
                "ChromeDriver started successfully"
        );

        return driver;
    }

    // =========================================================
    // FIREFOX
    // =========================================================

    private static WebDriver createFirefoxDriver(
            boolean headless) {

        log.info(
                "Creating FirefoxDriver | headless={}",
                headless
        );

        WebDriverManager.firefoxdriver().setup();

        FirefoxOptions options = new FirefoxOptions();

        if (headless) {
            options.addArguments("--headless");
        }

        options.addArguments(
                "--width=1920",
                "--height=1080"
        );

        return new FirefoxDriver(options);
    }

    // =========================================================
    // EDGE
    // =========================================================

    private static WebDriver createEdgeDriver(
            boolean headless) {

        log.info(
                "Creating EdgeDriver | headless={}",
                headless
        );

        WebDriverManager.edgedriver().setup();

        EdgeOptions options = new EdgeOptions();

        options.addArguments(
                "--window-size=1920,1080",
                "--disable-gpu",
                "--disable-dev-shm-usage",
                "--disable-notifications",
                "--disable-popup-blocking"
        );

        if (headless) {
            options.addArguments("--headless=new");
        }

        return new EdgeDriver(options);
    }
}
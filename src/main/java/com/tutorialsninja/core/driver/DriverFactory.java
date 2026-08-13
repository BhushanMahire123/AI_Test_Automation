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
 * DriverFactory — ThreadLocal WebDriver.
 * One driver per thread — safe for parallel execution.
 */
public class DriverFactory {

    private static final Logger log = LogManager.getLogger(DriverFactory.class);
    private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    private DriverFactory() {}

    public static void initDriver() {
        String browser   = ConfigManager.getBrowser();
        boolean headless = ConfigManager.isHeadless();
        log.info("Initializing [{}] driver | headless={}", browser, headless);
        WebDriver driver = createDriver(browser, headless);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigManager.getImplicitWait()));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().window().maximize();
        driverThread.set(driver);
        log.info("Driver ready — thread {}", Thread.currentThread().getId());
    }

    public static WebDriver getDriver() {
        WebDriver driver = driverThread.get();
        if (driver == null)
            throw new IllegalStateException("Driver not initialized! Call DriverFactory.initDriver() first.");
        return driver;
    }

    public static void quitDriver() {
        WebDriver driver = driverThread.get();
        if (driver != null) {
            driver.quit();
            driverThread.remove();
            log.info("Driver closed — thread {}", Thread.currentThread().getId());
        }
    }

    private static WebDriver createDriver(String browser, boolean headless) {
        switch (browser.toLowerCase().trim()) {
            case "firefox": return createFirefoxDriver(headless);
            case "edge":    return createEdgeDriver(headless);
            default:        return createChromeDriver(headless);
        }
    }

    private static WebDriver createChromeDriver(boolean headless) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions opts = new ChromeOptions();
        opts.addArguments("--no-sandbox", "--disable-dev-shm-usage",
            "--disable-gpu", "--window-size=1920,1080", "--remote-allow-origins=*");
        if (headless) opts.addArguments("--headless=new");
        return new ChromeDriver(opts);
    }

    private static WebDriver createFirefoxDriver(boolean headless) {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions opts = new FirefoxOptions();
        if (headless) opts.addArguments("--headless");
        return new FirefoxDriver(opts);
    }

    private static WebDriver createEdgeDriver(boolean headless) {
        WebDriverManager.edgedriver().setup();
        EdgeOptions opts = new EdgeOptions();
        if (headless) opts.addArguments("--headless=new");
        return new EdgeDriver(opts);
    }
}

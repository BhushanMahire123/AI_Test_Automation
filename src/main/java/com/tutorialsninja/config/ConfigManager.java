package com.tutorialsninja.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {

    private static final Properties props = new Properties();

    static {
        try (InputStream in = ConfigManager.class.getClassLoader()
                .getResourceAsStream("config/config.properties")) {
            if (in != null) props.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    private ConfigManager() {}

    public static String get(String key, String defaultValue) {
        String sysProp = System.getProperty(key);
        if (sysProp != null && !sysProp.isBlank()) return sysProp;
        return props.getProperty(key, defaultValue);
    }

    public static String get(String key)        { return get(key, ""); }
    public static String getUrl()               { return get("app.url"); }
    public static String getBrowser()           { return get("browser", "chrome"); }
    public static boolean isHeadless()          { return Boolean.parseBoolean(get("headless", "false")); }
    public static int getExplicitWait()         { return Integer.parseInt(get("explicit.wait", "15")); }
    public static int getImplicitWait()         { return Integer.parseInt(get("implicit.wait", "10")); }
}

package com.tutorialsninja.core.base;

import com.tutorialsninja.pages.account.AccountPage;
import com.tutorialsninja.pages.home.HomePage;
import com.tutorialsninja.pages.login.LoginPage;
import com.tutorialsninja.pages.register.RegisterPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * PageObjectRegistry — Maps BDD "Page" column strings to Page Object instances.
 *
 * Feature file:
 *   | Page       | Locator       |
 *   | LoginPage  | emailInput    |
 *   | HomePage   | loginOption   |
 *
 * To add a new page:
 *   1. Create page class extending BasePage
 *   2. Register it here with register()
 *   3. Use in feature files immediately — no other change needed
 */
public class PageObjectRegistry {

    private static final Logger log = LogManager.getLogger(PageObjectRegistry.class);
    private final Map<String, BasePage> registry = new HashMap<>();

    public PageObjectRegistry() {
        registerPages();
    }

    // ── Register all pages here ───────────────────────────────
    private void registerPages() {
        register("HomePage",     new HomePage());
        register("LoginPage",    new LoginPage());
        register("RegisterPage", new RegisterPage());
        register("AccountPage",  new AccountPage());
        // Add more pages here as project grows
        log.debug("PageObjectRegistry: {} pages registered", registry.size());
    }

    /**
     * Get page object by BDD name string (case-insensitive).
     */
    public BasePage getPage(String pageName) {
        String key = pageName.trim();
        BasePage page = registry.get(key);
        if (page == null) {
            page = registry.entrySet().stream()
                .filter(e -> e.getKey().equalsIgnoreCase(key))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                    "Page '" + pageName + "' not found in PageObjectRegistry.\n" +
                    "Registered pages: " + registry.keySet() + "\n" +
                    "Add it in PageObjectRegistry.registerPages()"));
        }
        log.debug("Page resolved: {} -> {}", pageName, page.getClass().getSimpleName());
        return page;
    }

    private void register(String name, BasePage page) {
        registry.put(name, page);
    }
}

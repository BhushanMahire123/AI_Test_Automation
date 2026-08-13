package com.tutorialsninja.core.base;

import org.openqa.selenium.By;

import java.lang.reflect.Field;

/**
 * LocatorRepository — Resolves BDD locator name strings to Selenium By objects.
 *
 * Uses Java Reflection to look up public By fields in page classes.
 *
 * Feature file:
 *   | Page      | Locator      |
 *   | LoginPage | emailInput   |  --> LoginPage.emailInput (By.id("input-email"))
 *
 * When UI locator changes:
 *   → Update By field in Page class ONLY
 *   → Feature file: NO CHANGE
 *   → Step definition: NO CHANGE
 */
public class LocatorRepository {

    private LocatorRepository() {}

    /**
     * Resolves locator name to By object via reflection on the page class.
     *
     * @param page        BasePage instance (e.g., LoginPage)
     * @param locatorName field name declared in page class (e.g., "emailInput")
     * @return By locator
     */
    public static By resolve(BasePage page, String locatorName) {
        Class<?> clazz = page.getClass();

        // Walk up class hierarchy to support inheritance
        while (clazz != null && clazz != Object.class) {
            try {
                Field field = clazz.getDeclaredField(locatorName.trim());
                field.setAccessible(true);
                Object value = field.get(page);

                if (value instanceof By) {
                    return (By) value;
                } else {
                    throw new IllegalArgumentException(
                        "Field '" + locatorName + "' in " +
                        page.getClass().getSimpleName() +
                        " is not of type By. Found: " +
                        (value == null ? "null" : value.getClass().getSimpleName()));
                }
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            } catch (IllegalAccessException e) {
                throw new RuntimeException(
                    "Cannot access field '" + locatorName + "' in " +
                    page.getClass().getSimpleName(), e);
            }
        }

        throw new IllegalArgumentException(
            "Locator '" + locatorName + "' not found in " +
            page.getClass().getSimpleName() + ".\n" +
            "Declare it as: public By " + locatorName + " = By.id(\"...\");");
    }
}

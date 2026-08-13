package com.tutorialsninja.enums;

/**
 * ElementType — Maps BDD "Element" column to UI element type.
 *
 * Feature file usage:
 * | Summary           | Element   | Action         | Value | Page      | Locator      |
 * | Click Login btn   | Button    | Click          | NA    | LoginPage | loginButton  |
 * | Enter email       | TextField | Type           | test@ | LoginPage | emailInput   |
 * | Verify heading    | Label     | WaitForDisplay | NA    | LoginPage | pageHeading  |
 */
public enum ElementType {
    BUTTON,
    TEXT_FIELD,
    CHECKBOX,
    RADIO_BUTTON,
    DROPDOWN,
    LINK,
    IMAGE,
    LABEL,
    TABLE,
    FRAME,
    BROWSER,
    WAIT,
    OTHERS;

    public static ElementType fromString(String value) {
        if (value == null) throw new IllegalArgumentException("ElementType cannot be null");
        String normalized = value.trim().toUpperCase()
            .replace(" ", "_")
            .replace("/", "_");
        for (ElementType type : values()) {
            if (type.name().equals(normalized)) return type;
        }
        // Aliases
        switch (value.trim().toLowerCase()) {
            case "textfield":
            case "text field":
            case "input":       return TEXT_FIELD;
            case "btn":         return BUTTON;
            case "chk":
            case "check":       return CHECKBOX;
            case "radio":       return RADIO_BUTTON;
            case "select":
            case "ddl":         return DROPDOWN;
            case "a":
            case "anchor":      return LINK;
            case "img":         return IMAGE;
            case "text":
            case "span":
            case "heading":     return LABEL;
            case "frame":
            case "iframe":      return FRAME;
            default:            return OTHERS;
        }
    }
}

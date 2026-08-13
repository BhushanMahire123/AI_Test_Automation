package com.tutorialsninja.enums;

/**
 * ActionType — Maps BDD "Action" column to BasePage method calls.
 */
public enum ActionType {
    CLICK,
    DOUBLE_CLICK,
    JS_CLICK,
    RIGHT_CLICK,
    TYPE,
    CLEAR,
    SELECT,
    GET_TEXT,
    GET_ATTRIBUTE,
    IS_DISPLAYED,
    IS_ENABLED,
    IS_SELECTED,
    WAIT_FOR_DISPLAY,
    WAIT,
    SCROLL,
    HOVER,
    NAVIGATE,
    VERIFY_TEXT,
    VERIFY_CONTAINS,
    ACCEPT_ALERT,
    DISMISS_ALERT;

    public static ActionType fromString(String value) {
        if (value == null) throw new IllegalArgumentException("ActionType cannot be null");
        String normalized = value.trim().toUpperCase().replace(" ", "_");
        for (ActionType type : values()) {
            if (type.name().equals(normalized)) return type;
        }
        // Aliases
        switch (value.trim().toLowerCase()) {
            case "waitfordisplay":
            case "waitforvisible":
            case "waitfordisplayed": return WAIT_FOR_DISPLAY;
            case "jsclick":
            case "javascriptclick":  return JS_CLICK;
            case "doubleclick":      return DOUBLE_CLICK;
            case "rightclick":       return RIGHT_CLICK;
            case "gettext":          return GET_TEXT;
            case "getattribute":     return GET_ATTRIBUTE;
            case "isdisplayed":      return IS_DISPLAYED;
            case "isenabled":        return IS_ENABLED;
            case "isselected":       return IS_SELECTED;
            case "verifytext":       return VERIFY_TEXT;
            case "verifycontains":   return VERIFY_CONTAINS;
            case "acceptalert":      return ACCEPT_ALERT;
            case "dismissalert":     return DISMISS_ALERT;
            default:
                throw new IllegalArgumentException("Unknown ActionType: '" + value + "'");
        }
    }
}

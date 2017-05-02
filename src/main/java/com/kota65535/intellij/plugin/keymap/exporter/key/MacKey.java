package com.kota65535.intellij.plugin.keymap.exporter.key;

/**
 * Created by tozawa on 2017/04/24.
 */
public enum MacKey {
    ESCAPE	 ( "\u238B", "Escape"),
    TAB	 ( "\u21E5", "Tab"),
    TAB_BACK	 ( "\u21E4", null),
    CAPS_LOCK	 ( "\u21EA", null),
    SHIFT	 ( "\u21E7", "Shift"),
    CONTROL	 ( "\u2303", "Ctrl"),
    OPTION      ( "\u2325", "Alt"),
    APPLE       ( "\uF8FF", null),
    COMMAND     ( "\u2318", "Meta"),
    SPACE	 ( "\u2423", "Space"),
    RETURN	 ( "\u23CE", "Return"),
    BACKSPACE	 ( "\u232B", "Backspace"),
    DELETE	 ( "\u2326", "Delete"),
    HOME        ( "\u2196", "Home"),
    END	 ( "\u2198", "End"),
    PAGE_UP	 ( "\u21DE", "Page Up"),
    PAGE_DOWN	 ( "\u21DF", "Page Down"),
    UP	         ( "\u2191", "Up"),
    DOWN	 ( "\u2193", "Down"),
    LEFT	 ( "\u2190", "Left"),
    RIGHT	 ( "\u2192", "Right"),
    NUMBER_LOCK ( "\u21ED", "Num Lock"),
    ENTER	 ( "\u2324", "Enter"),
    EJECT	 ( "\u23CF", null),
    POWER3	 ( "\u233D", null),
    NUM_PAD     ( "\u2328", null),
    ;

    private String originalString;
    private String normalizedString;

    private MacKey(String originalString, String normalizedString) {
        this.originalString = originalString;
        this.normalizedString = normalizedString;
    }

    public String getOriginalString() {
        return originalString;
    }

    public String getNormalizedString() {
        return normalizedString;
    }
}

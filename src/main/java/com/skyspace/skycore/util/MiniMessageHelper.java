package com.skyspace.skycore.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

/**
 * Legacy/helper wrapper. Some older code may reference this.
 */
public class MiniMessageHelper {

    private static final MiniMessage mm = MiniMessage.miniMessage();

    public static Component deserialize(String input) {
        return mm.deserialize(input == null ? "" : input);
    }

    public static String strip(String input) {
        return mm.stripTags(input == null ? "" : input);
    }
}

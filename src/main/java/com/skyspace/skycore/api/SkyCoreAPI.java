package com.skyspace.skycore.api;

import com.skyspace.skycore.SkyCore;
import com.skyspace.skycore.config.ConfigManager;
import com.skyspace.skycore.config.Messages;
import com.skyspace.skycore.data.DataManager;
import com.skyspace.skycore.gui.GuiManager;
import com.skyspace.skycore.util.TextUtils;
import com.skyspace.skycore.util.Utils;

/**
 * Static facade for other plugins to access SkyCore services.
 */
public class SkyCoreAPI {

    private static SkyCore plugin;

    /**
     * Called by SkyCore on enable.
     */
    public static void init(SkyCore instance) {
        plugin = instance;
    }

    public static SkyCore getPlugin() {
        return plugin;
    }

    public static ConfigManager getConfigManager() {
        return plugin.getConfigManager();
    }

    public static Messages getMessages() {
        return plugin.getMessages();
    }

    public static DataManager getDataManager() {
        return plugin.getDataManager();
    }

    public static GuiManager getGuiManager() {
        return plugin.getGuiManager();
    }

    public static TextUtils getTextUtils() {
        return TextUtils.get();
    }

    public static Utils getUtils() {
        return new Utils(); // Only for access; all Utils methods are static anyway.
    }
}

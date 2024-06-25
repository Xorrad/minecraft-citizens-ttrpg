package me.xorrad.ttrpg.localization;

import me.xorrad.lib.configs.Config;
import me.xorrad.ttrpg.TTRPG;
import org.bukkit.ChatColor;

public enum Localization {
    CHARACTER_MENU_TITLE;

    public String format;

    public String format(Object... args) {
        return String.format(ChatColor.translateAlternateColorCodes('&', this.format), args);
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    static Config activeConfig;

    public static Config getActiveConfig() {
        return activeConfig;
    }

    public static void setActiveConfig(Config activeConfig) {
        Localization.activeConfig = activeConfig;
        for(Localization msg : values()) {
            msg.setFormat(activeConfig.getString(msg.name()));
        }
    }
}

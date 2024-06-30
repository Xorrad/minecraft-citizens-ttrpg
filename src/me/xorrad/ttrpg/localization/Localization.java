package me.xorrad.ttrpg.localization;

import me.xorrad.lib.configs.Config;
import org.bukkit.ChatColor;

public enum Localization {
    CHARACTER_MENU_TITLE,

    CULTURE_EXISTS,
    CULTURE_DOESNT_EXISTS,
    CULTURE_CREATED,
    CULTURE_DELETED,
    CULTURE_NO_MANUAL_NAMES,
    CULTURE_NAME_ADDED,
    CULTURE_NAME_REMOVED,
    CULTURE_NAME_DOESNT_EXISTS,
    CULTURE_SHOW,

    NAMETYPE_INVALID,
    NAMEMANAGER_INVALID,
    NAMEMANAGER_CHANGED,
    NAMEMANAGER_TEMPLATE_UNSUPPORTED,
    NAMEMANAGER_TEMPLATE_CHANGED,

    ERROR,
    SEPARATOR,

    STAT_STRENGTH,
    STAT_DEXTERITY,
    STAT_CONSTITUTION,
    STAT_INTELLIGENCE,
    STAT_CHARISMA,
    ;

    private String format;

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

package me.xorrad.ttrpg.localization;

import me.xorrad.lib.configs.Config;
import org.bukkit.ChatColor;

public enum Localization {
    MENU_TITLE_CHARACTER,
    MENU_TITLE_CHANGE_CULTURE,
    MENU_TITLE_CHANGE_FAITH,
    MENU_TITLE_CHANGE_PERSONALITY,

    CULTURE_SELECT,
    FAITH_SELECT,

    CMD_CULTURE_EXISTS,
    CMD_CULTURE_DOESNT_EXIST,
    CMD_CULTURE_CREATED,
    CMD_CULTURE_DELETED,
    CMD_CULTURE_NO_MANUAL_NAMES,
    CMD_CULTURE_NAME_ADDED,
    CMD_CULTURE_NAME_REMOVED,
    CMD_CULTURE_NAME_DOESNT_EXIST,
    CMD_CULTURE_SHOW,
    CMD_CULTURE_INFO,
    CMD_CULTURE_INFO_NAME,
    CMD_CULTURE_INFO_NAMEMANAGER,
    CMD_CULTURE_NAMETYPE_INVALID,
    CMD_CULTURE_NAMEMANAGER_INVALID,
    CMD_CULTURE_NAMEMANAGER_CHANGED,
    CMD_CULTURE_NAMEMANAGER_TEMPLATE_UNSUPPORTED,
    CMD_CULTURE_NAMEMANAGER_TEMPLATE_CHANGED,
    CMD_CULTURE_LIST,

    CMD_FAITH_CREATED,
    CMD_FAITH_DELETED,
    CMD_FAITH_EXISTS,
    CMD_FAITH_DOESNT_EXIST,
    CMD_FAITH_LIST,

    NAME,
    CULTURE,
    FAITH,
    ERROR,
    SEPARATOR,
    NONE,

    DECREASE_BY_ONE,
    INCREASE_BY_ONE,

    STAT_STRENGTH,
    STAT_DEXTERITY,
    STAT_CONSTITUTION,
    STAT_INTELLIGENCE,
    STAT_CHARISMA,

    FAMILY_FATHER,
    FAMILY_MOTHER,
    FAMILY_PARTNER,
    FAMILY_CHILD,
    FAMILY_CHANGED,
    FAMILY_SELECT,

    MOUNT,
    MOUNT_DESC,
    INVENTORY,
    INVENTORY_DESC,
    EQUIP,
    EQUIP_DESC,
    SIT,
    SIT_DESC,
    FOLLOW,
    FOLLOW_DESC,
    SELECT,
    SELECT_DESC,
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

    @Override
    public String toString() {
        return this.format();
    }
}

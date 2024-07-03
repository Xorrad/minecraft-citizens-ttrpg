package me.xorrad.ttrpg.localization;

public enum Language {
    en_US,
    fr_FR;

    public String getPath() {
        return "lang/" + name() + ".yml";
    }

    public static boolean exists(String id) {
        for(Language l : values()) {
            if(l.name().equals(id))
                return true;
        }
        return false;
    }
}

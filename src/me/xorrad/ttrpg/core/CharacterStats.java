package me.xorrad.ttrpg.core;

import org.bukkit.Material;

public enum CharacterStats {
    STRENGTH(Material.IRON_SWORD),
    DEXTERITY(Material.LEATHER_BOOTS),
    CONSTITUTION(Material.COOKED_BEEF),
    INTELLIGENCE(Material.WRITTEN_BOOK),
    CHARISMA(Material.CLOCK);

    public Material icon;

    CharacterStats(Material icon) {
        this.icon = icon;
    }

    public Material getIcon() {
        return icon;
    }
}

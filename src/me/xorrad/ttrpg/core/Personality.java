package me.xorrad.ttrpg.core;

import org.bukkit.Material;

public enum Personality {

    LOYAL("Loyal", "This character stays loyal to his cause.", Material.APPLE),
    DISLOYAL("Disloyal", "This character likes to flip side when thing go awry.", Material.POISONOUS_POTATO),

    GREEDY("Greedy", "This character likes piling up gold.", Material.GOLD_NUGGET),
    GENEROUS("Generous", "This character enjoy spending money for others.", Material.FLOWER_POT),

    DILIGENT("Diligent", "This character thrives for hard work.", Material.IRON_PICKAXE),
    LAZY("Lazy", "This character tries his best to avoid any tiresome business.", Material.BROWN_BED),

    CHASTE("Chaste", "This character dislikes intimate contact, avoiding the temptations of the flesh.", Material.WHITE_CANDLE),
    LUSTFUL("Lustful", "Carnal desires burn hot in this character's core.", Material.REDSTONE),
    ;

    private String name;
    private String description;
    private Material icon;

    Personality(String name, String description, Material icon) {
        this.name = name;
        this.description = description;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Material getIcon() {
        return icon;
    }
}

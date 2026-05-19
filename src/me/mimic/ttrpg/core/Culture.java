package me.mimic.ttrpg.core;

import me.mimic.ttrpg.TTRPG;
import me.mimic.ttrpg.configs.CulturesConfig;
import me.mimic.ttrpg.core.names.NamesManager;

public class Culture {
    private String id;
    private String name;
    private NamesManager namesManager;

    public Culture(String id, String name, NamesManager namesManager) {
        this.id = id;
        this.name = name;
        this.namesManager = namesManager;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public NamesManager getNameManager() {
        return namesManager;
    }

    public void setNamesManager(NamesManager namesManager) {
        this.namesManager = namesManager;
    }

    public void save() {
        CulturesConfig config = (CulturesConfig) TTRPG.getInstance().getConfig("cultures");

        config.set(this.id + ".name", this.name);
        config.set(this.id + ".name-manager.type", this.namesManager.getType().name().toLowerCase());

        namesManager.saveConfig(this.id);

        config.save();
    }
}

package me.mimic.ttrpg.core;

import me.mimic.ttrpg.TTRPG;
import me.mimic.ttrpg.configs.FaithsConfig;

public class Faith {
    private String id;
    private String name;

    public Faith(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void save() {
        FaithsConfig config = (FaithsConfig) TTRPG.getInstance().getConfig("faiths");
        config.set(this.id + ".name", this.name);
        config.save();
    }
}

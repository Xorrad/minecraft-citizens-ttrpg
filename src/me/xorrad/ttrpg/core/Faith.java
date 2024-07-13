package me.xorrad.ttrpg.core;

import me.xorrad.ttrpg.TTRPG;
import me.xorrad.ttrpg.configs.CulturesConfig;
import me.xorrad.ttrpg.configs.FaithsConfig;
import me.xorrad.ttrpg.core.names.NamesManager;

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

package me.mimic.ttrpg.configs;

import me.mimic.lib.configs.Config;
import me.mimic.ttrpg.TTRPG;
import me.mimic.ttrpg.localization.Language;

import java.util.logging.Level;

public class PluginConfig extends Config {

    public PluginConfig() {
        super("config.yml");
    }

    @Override
    public void initDefault() {
        set("language", TTRPG.getInstance().language.name());
        TTRPG.log(Level.INFO, "Created default config file (%s)", this.getName());
    }

    @Override
    public void load() {
        super.load();

        if(contains("language") && Language.exists(getString("language"))) {
            TTRPG.getInstance().setLanguage(Language.valueOf(getString("language")));
            TTRPG.log(Level.INFO, "Switched plugin language to %s", TTRPG.getInstance().language.name());
        }
    }
}

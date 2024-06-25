package me.xorrad.ttrpg.localization;

import me.xorrad.lib.configs.Config;
import me.xorrad.ttrpg.TTRPG;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.logging.Level;

public class LocalizationConfig extends Config {

    public LocalizationConfig() {
        super("localization.yml");
    }

    @Override
    public void initDefault() {
        TTRPG.getInstance().saveResource(this.getName(), true);
        TTRPG.log(Level.INFO, "Created default config file (%s)", this.getName());
    }

    @Override
    public void load() {
        super.load();

        // Add default to file for missing localization.
        Reader reader = new InputStreamReader(TTRPG.getInstance().getResource("localization.yml"));
        YamlConfiguration dc = YamlConfiguration.loadConfiguration(reader);
        for(Localization msg : Localization.values()) {
            if(this.contains(msg.name()) || !dc.contains(msg.name()))
                continue;
            this.set(msg.name(), dc.getString(msg.name()));
        }
        this.save();
    }
}

package me.xorrad.ttrpg.configs;

import me.xorrad.lib.LibMain;
import me.xorrad.lib.configs.Config;
import me.xorrad.ttrpg.TTRPG;
import me.xorrad.ttrpg.localization.Language;
import me.xorrad.ttrpg.localization.Localization;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.logging.Level;

public class LocalizationConfig extends Config {

    public LocalizationConfig() {
        super(TTRPG.getInstance().language.getPath());
    }

    public void setActiveLanguage(Language lang) {
        this.save();
        this.file = new File(LibMain.getInstance().getDataFolder(), lang.getPath());
        this.load();
    }

    @Override
    public void initDefault() {
        for(Language lang : Language.values()) {
            TTRPG.getInstance().saveResource(lang.getPath(), true);
            TTRPG.log(Level.INFO, "Created default config file (%s)", lang.name());
        }
    }

    @Override
    public void load() {
        super.load();

        // Add default to file for missing localization.
        Reader reader = new InputStreamReader(TTRPG.getInstance().getResource(TTRPG.getInstance().language.getPath()));
        YamlConfiguration dc = YamlConfiguration.loadConfiguration(reader);
        for (Localization msg : Localization.values()) {
            if (this.contains(msg.name()) || !dc.contains(msg.name()))
                continue;
            this.set(msg.name(), dc.getString(msg.name()));
        }
        this.save();
    }
}

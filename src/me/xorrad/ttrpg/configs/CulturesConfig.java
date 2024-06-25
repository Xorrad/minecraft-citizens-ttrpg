package me.xorrad.ttrpg.configs;

import me.xorrad.lib.configs.Config;
import me.xorrad.ttrpg.TTRPG;
import me.xorrad.ttrpg.core.Culture;
import me.xorrad.ttrpg.core.names.NamesManager;
import me.xorrad.ttrpg.localization.Localization;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.logging.Level;

public class CulturesConfig extends Config {

    public CulturesConfig() {
        super("cultures.yml");
    }

    @Override
    public void initDefault() {
        TTRPG.getInstance().saveResource(this.getName(), true);
        TTRPG.log(Level.INFO, "Created default config file (%s)", this.getName());
    }

    @Override
    public void load() {
        super.load();

        for(String id : getKeys(false)) {
            try {
                String name = getString(id + ".name");
                NamesManager.Type nameManagerType = NamesManager.Type.valueOf(getString(id + ".name-manager.type").toUpperCase());
                NamesManager namesManager = nameManagerType.getClazz().getConstructor(String.class).newInstance(id);
                TTRPG.getInstance().cultures.put(id, new Culture(id, name, namesManager));
            }
            catch(Exception e) {
                TTRPG.log(Level.INFO, "Could not load culture %s", id);
                e.printStackTrace();
            }
        }
        TTRPG.log(Level.INFO, "Loaded %d cultures", TTRPG.getInstance().cultures.size());
    }
}

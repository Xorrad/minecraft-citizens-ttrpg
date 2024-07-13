package me.xorrad.ttrpg.configs;

import me.xorrad.lib.configs.Config;
import me.xorrad.ttrpg.TTRPG;
import me.xorrad.ttrpg.core.Culture;
import me.xorrad.ttrpg.core.Faith;
import me.xorrad.ttrpg.core.names.NamesManager;

import java.util.logging.Level;

public class FaithsConfig extends Config {

    public FaithsConfig() {
        super("faiths.yml");
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
                TTRPG.getInstance().faiths.put(id, new Faith(id, name));
            }
            catch(Exception e) {
                TTRPG.log(Level.INFO, "Could not load faith %s", id);
                e.printStackTrace();
            }
        }
        TTRPG.log(Level.INFO, "Loaded %d faiths", TTRPG.getInstance().faiths.size());
    }
}

package me.xorrad.ttrpg.configs;

import me.xorrad.lib.configs.Config;
import me.xorrad.ttrpg.TTRPG;
import me.xorrad.ttrpg.core.Warp;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.UUID;
import java.util.logging.Level;

public class WarpsConfig extends Config {

    public WarpsConfig() {
        super("warps.yml");
    }

    @Override
    public void initDefault() {
        TTRPG.log(Level.INFO, "Created default config file (%s)", this.getName());
    }

    @Override
    public void load() {
        super.load();

        for(String name : this.getKeys(false)) {
            UUID author = UUID.fromString(getString(name + ".author"));

            double x = getDouble(name + ".loc.x");
            double y = getDouble(name + ".loc.y");
            double z = getDouble(name + ".loc.z");
            float pitch = (float) getDouble(name + ".loc.pitch");
            float yaw = (float) getDouble(name + ".loc.yaw");
            World world = Bukkit.getWorld(getString(name + ".loc.world"));
            Location location = new Location(world, x, y, z, yaw, pitch);

            TTRPG.getInstance().warps.put(name, new Warp(name, location, author));
        }

        TTRPG.log(Level.INFO, "Loaded %d warps", TTRPG.getInstance().warps.size());
    }
}

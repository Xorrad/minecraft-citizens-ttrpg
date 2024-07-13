package me.xorrad.ttrpg.core;

import me.xorrad.ttrpg.TTRPG;
import me.xorrad.ttrpg.configs.WarpsConfig;
import org.bukkit.Location;

import java.util.UUID;

public class Warp {
    public String name;
    public Location location;
    public UUID author;

    public Warp(String name, Location location, UUID author) {
        this.name = name;
        this.location = location;
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public UUID getAuthor() {
        return author;
    }

    public void setAuthor(UUID author) {
        this.author = author;
    }

    public void save() {
        WarpsConfig config = (WarpsConfig) TTRPG.getInstance().getConfig("warps");

        config.set(name + ".loc.x", location.getX());
        config.set(name + ".loc.y", location.getY());
        config.set(name + ".loc.z", location.getZ());
        config.set(name + ".loc.pitch", location.getPitch());
        config.set(name + ".loc.yaw", location.getYaw());
        config.set(name + ".loc.world", location.getWorld().getName());

        config.set(name + ".author", author.toString());

        config.save();
    }
}

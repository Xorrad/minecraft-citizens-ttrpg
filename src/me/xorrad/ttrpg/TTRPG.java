package me.xorrad.ttrpg;

import me.xorrad.lib.LibMain;
import me.xorrad.lib.configs.Config;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.logging.Level;

public class TTRPG extends JavaPlugin  {
    static TTRPG instance;
    private HashMap<String, Config> configs;

    @Override
    public void onLoad() {
        instance = this;
        LibMain.instance = this;
    }

    @Override
    public void onEnable() {
        this.registerCommands();
        this.registerEvents();
        this.initConfigurations();
    }

    @Override
    public void onDisable() {

    }

    private void registerCommands() {

    }

    private void registerEvents() {
        PluginManager pm = Bukkit.getPluginManager();
    }

    private void initConfigurations() {
        this.configs = new HashMap<String, Config>();
    }

    public void loadConfig(String name, Config config) {
        this.configs.put(name, config);
        config.load();
    }

    public Config getConfig(String name) {
        return this.configs.get(name);
    }

    public static void log(String format, Object... args) {
        log(Level.INFO, format, args);
    }

    public static void log(Level level, String format, Object... args) {
        instance.getLogger().log(level, String.format(format, args));
    }

    public static TTRPG getInstance() {
        return instance;
    }
}

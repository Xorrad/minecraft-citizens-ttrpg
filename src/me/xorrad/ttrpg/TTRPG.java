package me.xorrad.ttrpg;

import me.xorrad.lib.LibMain;
import me.xorrad.lib.configs.Config;
import me.xorrad.ttrpg.commands.CultureCommand;
import me.xorrad.ttrpg.commands.TestCommand;
import me.xorrad.ttrpg.configs.CulturesConfig;
import me.xorrad.ttrpg.core.CharacterStats;
import me.xorrad.ttrpg.core.Culture;
import me.xorrad.ttrpg.core.traits.CharacterStatsTrait;
import me.xorrad.ttrpg.events.PlayerEvents;
import me.xorrad.ttrpg.localization.Localization;
import me.xorrad.ttrpg.configs.LocalizationConfig;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.CitizensEnableEvent;
import net.citizensnpcs.api.trait.TraitInfo;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.logging.Level;

public class TTRPG extends JavaPlugin  {
    static TTRPG instance;

    private HashMap<String, Config> configs;
    public HashMap<String, Culture> cultures;

    @Override
    public void onLoad() {
        instance = this;
        LibMain.instance = this;
    }

    @Override
    public void onEnable() {
        this.initVariables();
        this.initDependencies();
        this.registerCommands();
        this.registerEvents();
        this.initConfigurations();
    }

    @Override
    public void onDisable() {

    }

    private void initVariables() {
        this.cultures = new HashMap<>();
    }

    private void initDependencies() {
        Bukkit.getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onCitizensEnable(CitizensEnableEvent ev) {
                CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(CharacterStatsTrait.class));
            }
        }, this);
    }

    private void registerCommands() {
        new TestCommand().register();
        new CultureCommand().register();
    }

    private void registerEvents() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerEvents(), this);
    }

    private void initConfigurations() {
        this.configs = new HashMap<String, Config>();

        this.loadConfig("localization", new LocalizationConfig());
        Localization.setActiveConfig(this.getConfig("localization"));

        this.loadConfig("cultures", new CulturesConfig());
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

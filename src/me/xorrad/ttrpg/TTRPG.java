package me.xorrad.ttrpg;

import me.xorrad.lib.LibMain;
import me.xorrad.lib.configs.Config;
import me.xorrad.ttrpg.commands.CultureCommand;
import me.xorrad.ttrpg.commands.FaithCommand;
import me.xorrad.ttrpg.commands.TestCommand;
import me.xorrad.ttrpg.configs.CulturesConfig;
import me.xorrad.ttrpg.configs.FaithsConfig;
import me.xorrad.ttrpg.core.Culture;
import me.xorrad.ttrpg.core.Faith;
import me.xorrad.ttrpg.core.traits.FaithTrait;
import me.xorrad.ttrpg.core.traits.FamilyTrait;
import me.xorrad.ttrpg.core.traits.StatsTrait;
import me.xorrad.ttrpg.core.traits.CultureTrait;
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
    public HashMap<String, Faith> faiths;

    @Override
    public void onLoad() {
        instance = this;
        LibMain.instance = this;
    }

    @Override
    public void onEnable() {
        this.initVariables();
        this.registerCommands();
        this.registerEvents();
        this.initConfigurations();
        this.initDependencies();
    }

    @Override
    public void onDisable() {

    }

    private void initVariables() {
        this.cultures = new HashMap<>();
        this.faiths = new HashMap<>();
    }

    private void initDependencies() {
        CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(StatsTrait.class));
        CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(CultureTrait.class));
        CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(FaithTrait.class));
        CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(FamilyTrait.class));
    }

    private void registerCommands() {
        new TestCommand().register();
        new CultureCommand().register();
        new FaithCommand().register();
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
        this.loadConfig("faiths", new FaithsConfig());
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

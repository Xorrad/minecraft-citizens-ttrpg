package me.xorrad.ttrpg.core.names.types;

import me.xorrad.ttrpg.TTRPG;
import me.xorrad.ttrpg.configs.CulturesConfig;
import me.xorrad.ttrpg.core.Culture;
import me.xorrad.ttrpg.core.names.NameType;
import me.xorrad.ttrpg.core.names.NamesManager;
import me.xorrad.ttrpg.generators.RinkworksGenerator;

import java.util.ArrayList;
import java.util.HashMap;

public class RinkworksNamesManager extends NamesManager {
    private HashMap<NameType, String> templates;
    private HashMap<NameType, RinkworksGenerator> generators;

    public RinkworksNamesManager(String configPath) {
        super(Type.RINKWORKS, configPath);

        this.templates = new HashMap<>();
        this.generators = new HashMap<>();

        this.loadConfig(configPath);
        this.loadData();
    }

    public String getTemplate(NameType nameType)
    {
        return this.templates.get(nameType);
    }

    public void setTemplate(NameType nameType, String template) {
        this.templates.put(nameType, template);
        this.generators.put(nameType, new RinkworksGenerator(template));
    }

    @Override
    public String get(NameType nameType) {
        return this.generators.get(nameType).toString();
    }

    @Override
    public ArrayList<String> getResume() {
        ArrayList<String> lines = new ArrayList<>();

        lines.add("§eType: §7" + this.type.name().toLowerCase());

        for(NameType type : NameType.values())
        {
            lines.add("§e" + type.name() + ": \"§7" + this.templates.get(type) + "§e\"");
        }

        return lines;
    }

    @Override
    public void loadConfig(String configPath) {
        CulturesConfig config = (CulturesConfig) TTRPG.getInstance().getConfig("cultures");

        for(NameType nameType : NameType.values()) {
            String key = configPath + ".name-manager.template." + nameType.name().toLowerCase();
            if(config.contains(key)) {
                this.setTemplate(nameType, config.getString(key));
            }
        }
    }

    @Override
    public void saveConfig(String configPath) {
        CulturesConfig config = (CulturesConfig) TTRPG.getInstance().getConfig("cultures");

        for(NameType nameType : NameType.values())
            config.set(configPath + ".name-manager.template." + nameType.name().toLowerCase(), this.templates.get(nameType));

        config.save();
    }

    @Override
    public void setDefaultConfig(Culture culture) {
        String template = "ss";

        for(NameType nameType : NameType.values())
            this.setTemplate(nameType, template);
    }

    @Override
    public void loadData()
    {
    }
}

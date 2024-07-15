package me.xorrad.ttrpg.core.names.types;

import me.xorrad.ttrpg.TTRPG;
import me.xorrad.ttrpg.configs.CulturesConfig;
import me.xorrad.ttrpg.core.Culture;
import me.xorrad.ttrpg.core.names.IListNamesManager;
import me.xorrad.ttrpg.core.names.NameType;
import me.xorrad.ttrpg.core.names.NamesManager;
import org.bukkit.Bukkit;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;

public class ListNamesManager extends NamesManager implements IListNamesManager {
    private String filePath;
    private HashMap<NameType, ArrayList<String>> names;

    public ListNamesManager(String configPath) {
        super(NamesManager.Type.LIST, configPath);

        this.loadConfig(configPath);
        this.loadData();
    }

    public File getFile(NameType nameType) {
        return new File(TTRPG.getInstance().getDataFolder() + "/" + this.filePath, nameType.name().toLowerCase() + ".txt");
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String get(NameType nameType) {
        int count = names.get(nameType).size();

        if(count == 0) {
            TTRPG.log(Level.INFO, "Warning! A list name generator does not contain any value.");
            return "";
        }

        int index = this.random.nextInt(count);
        return names.get(nameType).get(index);
    }

    @Override
    public ArrayList<String> getResume() {
        ArrayList<String> lines = new ArrayList<>();

        lines.add("§eType: §7" + this.type.name().toLowerCase());
        lines.add("§eDirectory: §7\"" + this.filePath + "\"");

        for(NameType type : NameType.values()) {
            int n = this.names.get(type).size();
            List<String> values = this.names.get(type).subList(0, Math.min(5, n));
            lines.add("§e" + type.name() + " (" + this.names.get(type).size() + "): §7" + String.join(",", values) + (n > 5 ? "..." : ""));
        }

        return lines;
    }

    @Override
    public void loadConfig(String configPath) {
        CulturesConfig config = (CulturesConfig) TTRPG.getInstance().getConfig("cultures");

        this.filePath = config.getString(configPath + ".name-manager.file-path");
    }

    @Override
    public void saveConfig(String configPath) {
        CulturesConfig config = (CulturesConfig) TTRPG.getInstance().getConfig("cultures");

        config.set(configPath + ".name-manager.file-path", this.filePath);

        for(NameType nameType : NameType.values()) {
            /*if(this.names.get(nameType).size() == 0)
                continue;*/

            try {
                File file = this.getFile(nameType);

                if(!file.exists()) {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }

                BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));

                for(String name : this.names.get(nameType)) {
                    writer.write(name);
                    writer.newLine();
                }

                writer.close();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }

        config.save();
    }

    @Override
    public void setDefaultConfig(Culture culture) {
        this.filePath = "names/" + culture.getId().toLowerCase();
    }

    @Override
    public void loadData() {
        this.names = new HashMap<NameType, ArrayList<String>>();

        for(NameType nameType : NameType.values()) {
            this.names.put(nameType, new ArrayList<String>());

            File file = this.getFile(nameType);

            try {
                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            try {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    String name = scanner.nextLine();
                    this.names.get(nameType).add(name);
                }
                scanner.close();
            }
            catch (FileNotFoundException e) {
                TTRPG.log(Level.INFO, "Could not find file (%s) for %s.", this.filePath, nameType.name());
                try {
                    file.mkdirs();
                    file.createNewFile();
                }
                catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                //e.printStackTrace();
            }
        }
    }

    @Override
    public ArrayList<String> getNames(NameType nameType)
    {
        return this.names.get(nameType);
    }

    @Override
    public void addName(NameType nameType, String name) {
        if(this.names.get(nameType).contains(name))
            return;

        this.names.get(nameType).add(name);
    }

    @Override
    public void removeName(NameType nameType, String name)
    {
        this.names.get(nameType).remove(name);
    }

    @Override
    public boolean containsName(NameType nameType, String name)
    {
        return this.names.get(nameType).contains(name);
    }
}

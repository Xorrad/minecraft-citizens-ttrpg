package me.xorrad.ttrpg.core.names.types;

import me.xorrad.ttrpg.TTRPG;
import me.xorrad.ttrpg.configs.CulturesConfig;
import me.xorrad.ttrpg.core.Culture;
import me.xorrad.ttrpg.core.names.IListNamesManager;
import me.xorrad.ttrpg.core.names.NameType;
import me.xorrad.ttrpg.core.names.NamesManager;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;

public class NameyNamesManager extends NamesManager {

    public NameyNamesManager(String configPath) {
        super(Type.NAMEY, configPath);
    }

    public String getNameyURL(NameType type) {
        return switch (type) {
            case MALE -> "https://namey.muffinlabs.com/name.json?count=1&type=male&with_surname=false&frequency=all";
            case FEMALE -> "https://namey.muffinlabs.com/name.json?count=1&type=female&with_surname=false&frequency=all";
            case FAMILY -> "https://namey.muffinlabs.com/name.json?count=1&type=surname&frequency=all";
        };
    }

    @Override
    public String get(NameType nameType) {
        String nameyURL = getNameyURL(nameType);
        URL url = null;
        InputStream is = null;
        try {
            url = new URL(nameyURL);
            is = url.openStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String inputLine = reader.readLine();
            is.close();
            return inputLine.substring(2, inputLine.length() - 2);
        }
        catch (Exception e)
        {
            TTRPG.log(Level.INFO, "Error! Failed to query name to namey.muffinlabs.com.");
        }

        return "";
    }

    @Override
    public ArrayList<String> getResume() {
        ArrayList<String> lines = new ArrayList<>();
        lines.add("§eType: §7" + this.type.name().toLowerCase());
        return lines;
    }

    @Override
    public void loadConfig(String configPath) {

    }

    @Override
    public void saveConfig(String configPath) {

    }

    @Override
    public void setDefaultConfig(Culture culture) {

    }

    @Override
    public void loadData() {

    }
}

package me.xorrad.ttrpg.core.names;

import me.xorrad.ttrpg.core.Culture;
import me.xorrad.ttrpg.core.names.types.ListNamesManager;
import me.xorrad.ttrpg.core.names.types.NameyNamesManager;
import me.xorrad.ttrpg.core.names.types.RinkworksNamesManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public abstract class NamesManager {
    protected NamesManager.Type type;
    protected Random random;

    public NamesManager(NamesManager.Type type, String configPath) {
        this.type = type;
        this.random = new Random();
    }

    public Type getType() {
        return type;
    }

    public String get() {
        //Get a random nameType.
        NameType nameType = NameType.values()[this.random.nextInt(NameType.values().length)];
        return this.get(nameType);
    }

    public abstract String get(NameType nameType);
    public abstract ArrayList<String> getResume();

    public abstract void loadConfig(String configPath);
    public abstract void saveConfig(String configPath);
    public abstract void setDefaultConfig(Culture culture);
    public abstract void loadData();

    public enum Type {
        LIST(ListNamesManager.class),
        RINKWORKS(RinkworksNamesManager.class),
        NAMEY(NameyNamesManager.class);

        private Class<? extends NamesManager> clazz;

        Type(Class<? extends NamesManager> clazz) {
            this.clazz = clazz;
        }

        public Class<? extends NamesManager> getClazz() {
            return clazz;
        }

        public static boolean exists(String str) {
            for(Type v : values()) {
                if(v.name().equalsIgnoreCase(str))
                    return true;
            }

            return false;
        }

        public static List<String> getNames() {
            return Arrays.stream(values()).map(Enum::name).collect(Collectors.toList());
        }
    }
}

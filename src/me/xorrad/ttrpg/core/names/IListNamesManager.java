package me.xorrad.ttrpg.core.names;

import java.util.ArrayList;

public interface IListNamesManager {
    ArrayList<String> getNames(NameType nameType);
    void addName(NameType nameType, String name);
    void removeName(NameType nameType, String name);
    boolean containsName(NameType nameType, String name);
}

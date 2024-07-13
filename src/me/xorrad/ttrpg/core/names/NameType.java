package me.xorrad.ttrpg.core.names;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum NameType
{
    MALE,
    FEMALE,
    FAMILY;

    public static boolean exists(String str) {
        for(NameType v : values()) {
            if(v.name().equalsIgnoreCase(str))
                return true;
        }

        return false;
    }

    public static List<String> getNames() {
        return Arrays.stream(values()).map(Enum::name).collect(Collectors.toList());
    }
}

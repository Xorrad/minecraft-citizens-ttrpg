package me.xorrad.mu.utilities;

import java.util.Random;
import java.util.UUID;

public class MathUtil
{
    public static Random random;

    static {
        random = new Random();
    }

    public static int random(int min, int max) {
        return random.nextInt(max + 1 - min) + min;
    }

    public static String toString(long value) {
        if(value >= 0)
            return "+" + String.valueOf(value);

        return String.valueOf(value);
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        }
        catch(Exception e) {
            return false;
        }

        return true;
    }

    public static boolean isUUID(String input) {
        try {
            UUID uuid = UUID.fromString(input);
        }
        catch (IllegalArgumentException exception) {
            return false;
        }

        return true;
    }
}

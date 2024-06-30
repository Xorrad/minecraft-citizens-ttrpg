package me.xorrad.ttrpg.core.traits;

import me.xorrad.ttrpg.core.CharacterStats;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitName;
import net.citizensnpcs.api.util.DataKey;

import java.util.HashMap;

@TraitName("character_stats")
public class CharacterStatsTrait extends Trait {

    private final HashMap<CharacterStats, Integer> stats;

    public CharacterStatsTrait() {
        super("character_stats");
        this.stats = new HashMap<>();
    }

    public int getStat(CharacterStats stat) {
        return this.stats.getOrDefault(stat, 10);
    }

    public void setStat(CharacterStats stat, int value) {
        this.stats.put(stat, value);
    }

    public int getStatModifier(CharacterStats stat) {
        int n = this.getStat(stat);
        return (int) Math.floor(n / 2.0) - 5;
    }

    @Override
    public void load(DataKey key) {
        for(CharacterStats stat : CharacterStats.values()) {
            this.stats.put(stat, key.getInt(stat.name().toLowerCase(), 10));
        }
    }

    @Override
    public void save(DataKey key) {
        for(CharacterStats stat : CharacterStats.values()) {
            key.setInt(stat.name().toLowerCase(), this.stats.getOrDefault(stat, 10));
        }
    }
}

package me.xorrad.ttrpg.core.traits;

import me.xorrad.ttrpg.TTRPG;
import me.xorrad.ttrpg.core.Personality;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitName;
import net.citizensnpcs.api.util.DataKey;

import java.util.ArrayList;
import java.util.stream.Collectors;

@TraitName("personalitytrait")
public class PersonalityTrait extends Trait {
    private ArrayList<Personality> personalities;

    public PersonalityTrait() {
        super("personalitytrait");
        this.personalities = new ArrayList<>();
    }

    public void addPersonality(Personality p) {
        if(!this.personalities.contains(p))
            this.personalities.add(p);
    }

    public void removePersonality(Personality p) {
        this.personalities.remove(p);
    }

    public ArrayList<Personality> getPersonalities() {
        return personalities;
    }

    @Override
    public void load(DataKey key) {
        String[] ss = key.getString("personalities", "").split(",");

        for(String s : ss) {
            if(s.isBlank())
                continue;
            try {
                Personality p = Personality.valueOf(s);
                this.personalities.add(p);
            }
            catch(Exception ignored) {}
        }
    }

    @Override
    public void save(DataKey key) {
        key.setString("personalities", this.personalities.stream().map(Personality::name).collect(Collectors.joining(",")));
    }
}

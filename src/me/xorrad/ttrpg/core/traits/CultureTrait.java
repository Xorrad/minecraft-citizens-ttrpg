package me.xorrad.ttrpg.core.traits;

import me.xorrad.ttrpg.TTRPG;
import me.xorrad.ttrpg.core.CharacterStats;
import me.xorrad.ttrpg.core.Culture;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitName;
import net.citizensnpcs.api.util.DataKey;

import java.util.HashMap;

@TraitName("culturetrait")
public class CultureTrait extends Trait {
    private String cultureId;

    public CultureTrait() {
        super("culturetrait");
        this.cultureId = "";
    }

    public Culture getCulture() {
        return TTRPG.getInstance().cultures.getOrDefault(this.cultureId, null);
    }

    public void setCulture(Culture culture) {
        this.cultureId = (culture == null) ? "" : culture.getId();
    }

    @Override
    public void load(DataKey key) {
        this.cultureId = key.getString("id", "");
    }

    @Override
    public void save(DataKey key) {
        key.setString("id", this.cultureId);
    }
}

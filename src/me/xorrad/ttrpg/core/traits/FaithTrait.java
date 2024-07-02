package me.xorrad.ttrpg.core.traits;

import me.xorrad.ttrpg.TTRPG;
import me.xorrad.ttrpg.core.Culture;
import me.xorrad.ttrpg.core.Faith;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitName;
import net.citizensnpcs.api.util.DataKey;

@TraitName("faithtrait")
public class FaithTrait extends Trait {
    private Faith faith;

    public FaithTrait() {
        super("faithtrait");
        this.faith = null;
    }

    public Faith getFaith() {
        return this.faith;
    }

    public void setFaith(Faith faith) {
        this.faith = faith;
    }

    @Override
    public void load(DataKey key) {
        this.faith = TTRPG.getInstance().faiths.getOrDefault(key.getString("id"), null);
    }

    @Override
    public void save(DataKey key) {
        key.setString("id", this.faith == null ? "" : this.faith.getId());
    }
}

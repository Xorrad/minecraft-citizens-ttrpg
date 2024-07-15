package me.xorrad.ttrpg.util;

import me.xorrad.ttrpg.core.Culture;
import me.xorrad.ttrpg.core.traits.CultureTrait;
import net.citizensnpcs.Citizens;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class CitizensUtil {
    public static NPC spawnNPC(String name, Location location) {
        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, name);
        npc.setProtected(false);
        npc.data().setPersistent(NPC.Metadata.COLLIDABLE, false);
        npc.setAlwaysUseNameHologram(false);
        npc.spawn(location);
        return npc;
    }

    public static NPC spawnNPC(String name, Location location, Culture culture) {
        NPC npc = spawnNPC(name, location);
        npc.getOrAddTrait(CultureTrait.class).setCulture(culture);
        ((Citizens) CitizensAPI.getPlugin()).storeNPCs(true);
        return npc;
    }
}

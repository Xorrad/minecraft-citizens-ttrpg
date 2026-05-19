package me.mimic.ttrpg.util;

import me.mimic.ttrpg.core.Culture;
import me.mimic.ttrpg.core.traits.CultureTrait;
import net.citizensnpcs.Citizens;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.AttributeTrait;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;

public class CitizensUtil {
    public static NPC spawnNPC(String name, Location location) {
        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, name);
        npc.setProtected(false);
        npc.data().setPersistent(NPC.Metadata.COLLIDABLE, false);
        npc.setAlwaysUseNameHologram(false);
        ((AttributeTrait) npc.getOrAddTrait(AttributeTrait.class)).setAttributeValue(Attribute.SCALE, MathUtil.random(0.9, 1.1));
        npc.spawn(location);
        ((Citizens) CitizensAPI.getPlugin()).storeNPCs();
        return npc;
    }

    public static NPC spawnNPC(String name, Location location, Culture culture) {
        NPC npc = spawnNPC(name, location);
        npc.getOrAddTrait(CultureTrait.class).setCulture(culture);
        ((Citizens) CitizensAPI.getPlugin()).storeNPCs();
        return npc;
    }
}

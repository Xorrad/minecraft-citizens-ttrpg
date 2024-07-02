package me.xorrad.ttrpg.events;

import me.xorrad.ttrpg.menus.CharacterMenu;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.MobType;
import net.citizensnpcs.nms.v1_20_R1.entity.EntityHumanNPC;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;

public class PlayerEvents implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

    }

    @EventHandler
    public void onClick(PlayerInteractEntityEvent event) {
        if(!event.getHand().equals(EquipmentSlot.HAND))
            return;

        Player player = event.getPlayer();
        NPC npc = CitizensAPI.getNPCRegistry().getNPC(event.getRightClicked());

        if (player.getEquipment().getItem(EquipmentSlot.HAND).getType().equals(Material.STICK))
            return;

        if (npc == null || !npc.getTraitNullable(MobType.class).getType().equals(EntityType.PLAYER))
            return;

        event.setCancelled(true);
        new CharacterMenu(npc).open(player);
    }

}

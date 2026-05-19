package me.mimic.ttrpg.events;

import me.mimic.ttrpg.TTRPG;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class NpcsEvents implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event)
    {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if(!event.getHand().equals(EquipmentSlot.HAND))
            return;

        if(item.getItemMeta() == null || !item.getItemMeta().hasDisplayName() || !item.getItemMeta().getDisplayName().equalsIgnoreCase("§eBaguette de selection"))
            return;

        NPC npc = CitizensAPI.getNPCRegistry().getNPC(event.getRightClicked());
        if (npc == null)
            return;

        if(!TTRPG.getInstance().selectedNPCS.containsKey(player))
            TTRPG.getInstance().selectedNPCS.put(player, new ArrayList<>());

        if(TTRPG.getInstance().selectedNPCS.get(player).contains(npc)) {
            TTRPG.getInstance().selectedNPCS.get(player).remove(npc);
            player.sendMessage("§a(§c-§a) §e" + npc.getName() + " §a(ID " + npc.getId() + ").");
        }
        else {
            TTRPG.getInstance().selectedNPCS.get(player).add(npc);
            player.sendMessage("§a(+) §e" + npc.getName() + " §a(ID " + npc.getId() + ").");
        }

        event.setCancelled(true);
    }
}
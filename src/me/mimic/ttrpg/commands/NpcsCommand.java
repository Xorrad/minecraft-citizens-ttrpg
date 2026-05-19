package me.mimic.ttrpg.commands;

import me.mimic.lib.commands.CommandGroup;
import me.mimic.lib.commands.CommandParameter;
import me.mimic.ttrpg.TTRPG;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.FollowTrait;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class NpcsCommand extends CommandGroup {

    public NpcsCommand() {
        super("npcs");

        newSubCommand("wand")
                .playerOnly(true)
                .execute(NpcsCommand::wand);

        newSubCommand("follow")
                .playerOnly(true)
                .execute(NpcsCommand::follow);

        newSubCommand("tphere")
                .playerOnly(true)
                .execute(NpcsCommand::tphere);

        newSubCommand("list")
                .playerOnly(true)
                .execute(NpcsCommand::list);

        newSubCommand("clear")
                .playerOnly(true)
                .execute(NpcsCommand::clear);

        newSubCommand("names")
                .playerOnly(true)
                .execute(NpcsCommand::hideNames);
    }

    public static void wand(CommandSender sender, Object[] objects) {
        Player player = (Player) sender;
        ItemStack wand = new ItemStack(Material.STICK);
        ItemMeta meta = wand.getItemMeta();
        meta.setDisplayName("§eBaguette de selection");
        wand.setItemMeta(meta);
        player.getInventory().addItem(wand);
        player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1.0F, 1.0F);
    }

    public static void follow(CommandSender sender, Object[] objects) {
        Player player = (Player) sender;

        if(!TTRPG.getInstance().selectedNPCS.containsKey(player)) {
            return;
        }

        long followingCount =
                TTRPG.getInstance().selectedNPCS.get(player).stream()
                    .filter(npc -> { return ((FollowTrait) npc.getOrAddTrait(FollowTrait.class)).isEnabled(); })
                    .count();

        boolean followingMajority = (followingCount >= TTRPG.getInstance().selectedNPCS.get(player).size());

        ArrayList<String> followingList = new ArrayList<>();
        for(NPC npc : TTRPG.getInstance().selectedNPCS.get(player)) {
            FollowTrait trait = (FollowTrait) npc.getOrAddTrait(FollowTrait.class);
            followingList.add(npc.getName());
            trait.follow(followingMajority ? null : player);
        }

        if(followingMajority)
            player.sendMessage("§e" + String.join("§a, §e", followingList) + "§a are now following §e" + player.getName() + "§a.");
        if(!followingMajority)
            player.sendMessage("§e" + String.join("§a, §e", followingList) + "§a are no longer following anyone.");
        if(!followingList.isEmpty())
            player.sendMessage("§aNo NPCs are selected.");
    }

    public static void tphere(CommandSender sender, Object[] objects) {
        Player player = (Player) sender;
        if(!TTRPG.getInstance().selectedNPCS.containsKey(player))
            return;

        ArrayList<String> npcsList = new ArrayList<>();
        for(NPC npc : TTRPG.getInstance().selectedNPCS.get(player)) {
            npcsList.add(npc.getName());
            npc.teleport(player.getLocation(), PlayerTeleportEvent.TeleportCause.COMMAND);
        }

        if(!npcsList.isEmpty())
            player.sendMessage("§e" + String.join("§a, §e", npcsList) + "§a have been teleported to §e" + player.getName() + "§a.");
        else
            player.sendMessage("§aNo NPCs are selected.");
    }

    public static void list(CommandSender sender, Object[] objects) {
        Player player = (Player) sender;

        if(!TTRPG.getInstance().selectedNPCS.containsKey(player)) {
            player.sendMessage("§aNo NPCs are selected.");
            return;
        }

        ArrayList<String> npcsList = new ArrayList<>();
        for(NPC npc : TTRPG.getInstance().selectedNPCS.get(player)) {
            npcsList.add(npc.getName());
        }

        if(!npcsList.isEmpty())
            player.sendMessage("§e" + String.join("§a, §e", npcsList) + "§a are currently selected.");
        else
            player.sendMessage("§aNo NPCs are selected.");
    }

    public static void clear(CommandSender sender, Object[] objects) {
        Player player = (Player) sender;
        if(TTRPG.getInstance().selectedNPCS.containsKey(player))
            TTRPG.getInstance().selectedNPCS.remove(player);
        player.sendMessage("§aNPCs are no longer being selected.");
    }

    public static void hideNames(CommandSender sender, Object[] objects) {
        Player player = (Player) sender;
        String arg = objects.length > 0 ? (String) objects[0] : "";

        if(!arg.equalsIgnoreCase("--false") && !arg.equalsIgnoreCase("--true") && !arg.equalsIgnoreCase("--hover")) {
            player.sendMessage("§cInvalid syntax! Usage: /npcs names --false,--true,--hover");
            return;
        }
        String newState = arg.substring(2);

        for(NPC npc : CitizensAPI.getNPCRegistry().sorted()) {
            String old = npc.data().get(NPC.Metadata.NAMEPLATE_VISIBLE, "true");
            if (!old.equals(newState)) {
                npc.data().setPersistent(NPC.Metadata.NAMEPLATE_VISIBLE, newState);
                npc.scheduleUpdate(NPC.NPCUpdate.PACKET);
            }
        }

        switch(newState) {
            case "hover":
                player.sendMessage("§aNPCs names are only displayed on hover.");
                break;
            case "true":
                player.sendMessage("§aNPCs names are always displayed.");
                break;
            case "false":
                player.sendMessage("§aNPCs names are no longer displayed.");
                break;
        }
    }
}

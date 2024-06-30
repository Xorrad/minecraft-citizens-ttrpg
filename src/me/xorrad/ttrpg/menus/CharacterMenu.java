package me.xorrad.ttrpg.menus;

import me.xorrad.lib.ui.Item;
import me.xorrad.lib.ui.ItemClickResult;
import me.xorrad.lib.ui.Menu;
import me.xorrad.ttrpg.core.CharacterStats;
import me.xorrad.ttrpg.core.traits.CharacterStatsTrait;
import me.xorrad.ttrpg.localization.Localization;
import me.xorrad.ttrpg.util.SkinUtil;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.command.exception.CommandException;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Inventory;
import net.citizensnpcs.api.util.Messaging;
import net.citizensnpcs.editor.Editor;
import net.citizensnpcs.editor.EquipmentEditor;
import net.citizensnpcs.trait.Controllable;
import net.citizensnpcs.trait.SitTrait;
import net.citizensnpcs.util.Util;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class CharacterMenu {

    public static void open(Player player, NPC npc) {
        Menu menu = new Menu()
            .title(Localization.CHARACTER_MENU_TITLE.format(npc.getName()))
            .size(6)
            .onClick((p, item, event) -> { return true; })
            .add(new Item()
                    .material(Material.PLAYER_HEAD)
                    .name("§eName")
                    .lore("§e" + npc.getName())
                    .head(SkinUtil.getNPCProfile(npc))
            )
            .add(new Item()
                    .material(Material.FLOWER_BANNER_PATTERN)
                    .name("§eCulture")
                    .lore("§ePLACEHOLDER")
                    .hideAttributes()
            )
            .add(new Item()
                    .material(Material.NETHER_STAR)
                    .name("§eReligion")
                    .lore("§ePLACEHOLDER")
            );

        int i = 0;
        for(CharacterStats stat : CharacterStats.values()) {
            CharacterStatsTrait trait = npc.getOrAddTrait(CharacterStatsTrait.class);
            menu.set(9 + i, new Item()
                .material(stat.getIcon())
                .name("§e" + Localization.valueOf("STAT_" + stat.name()).format())
                .lore("§e" + trait.getStat(stat))
                .hideAttributes()
            );
            i++;
        }

        for(i = 0; i < 9; i++) {
            menu.set(2*9 + i, new Item().material(Material.WHITE_STAINED_GLASS_PANE));
        }

        for(i = 0; i < 9; i++) {
            menu.set(4*9 + i, new Item().material(Material.WHITE_STAINED_GLASS_PANE));
        }

        menu.set(5*9, new Item()
                        .material(Material.SADDLE)
                        .name("Mount")
                        .lore("§eClick to mount character.")
                        .onClick((p, m) -> {
                            if (!npc.isSpawned()) {
                                player.sendMessage("§cFailed to mount character because it is not spawned.");
                                return ItemClickResult.NO_RESULT;
                            }

                            if (!npc.hasTrait(Controllable.class))
                                npc.addTrait(new Controllable(true));
                            Controllable trait = (Controllable) npc.getOrAddTrait(Controllable.class);
                            trait.setEnabled(true);
                            trait.mount(player);
                            return ItemClickResult.CLOSE_INVENTORY;
                        })
                );

        menu.set(5*9+1, new Item()
                .material(Material.CHEST)
                .name("Inventory")
                .lore("§eClick to open this character's inventory.")
                .onClick((p, m) -> {
                    if (!npc.hasTrait(Inventory.class)) {
                        player.sendMessage("§cFailed to open the inventory for this character.");
                        return ItemClickResult.NO_RESULT;
                    }
                    Inventory trait = (Inventory) npc.getTraitNullable(Inventory.class);
                    menu.setClosed(true);
                    trait.openInventory(player);
                    return ItemClickResult.OPEN_INVENTORY;
                })
        );

        menu.set(5*9+2, new Item()
                .material(Material.IRON_CHESTPLATE)
                .name("Equipment")
                .lore("§eClick to edit this character's equipment.")
                .onClick((p, m) -> {
                    if (!npc.isSpawned()) {
                        player.sendMessage("§cFailed to edit character because it is not spawned.");
                        return ItemClickResult.NO_RESULT;
                    }
                    menu.setClosed(true);
                    Editor.enterOrLeave(player, new EquipmentEditor(player, npc));
                    return ItemClickResult.OPEN_INVENTORY;
                })
        );

        menu.set(5*9+3, new Item()
                .material(Material.OAK_STAIRS)
                .name("Sit")
                .lore("§eClick to sit this character.")
                .onClick((p, m) -> {
                    if (!npc.isSpawned()) {
                        player.sendMessage("§cFailed to edit character because it is not spawned.");
                        return ItemClickResult.NO_RESULT;
                    }
                    SitTrait trait = (SitTrait)npc.getOrAddTrait(SitTrait.class);
                    boolean toSit = !trait.isSitting();

                    if (!toSit){
                        trait.setSitting((Location)null);
                    }
                    else {
                        Location at = npc.getStoredLocation();
                        Block block = at.getWorld().getHighestBlockAt(at);
                        at.setY(block.getY() + 1);
                        if(block.getType().name().contains("STAIRS") || block.getType().name().contains("SLAB"))
                            at.setY(block.getY() + 0.5);
                        trait.setSitting(at);
                    }
                    CitizensAPI.getNPCRegistry().saveToStore();
                    return ItemClickResult.NO_RESULT;
                })
        );

        menu.open(player);
    }

}

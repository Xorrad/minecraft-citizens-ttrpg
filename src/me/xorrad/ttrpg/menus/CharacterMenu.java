package me.xorrad.ttrpg.menus;

import me.xorrad.lib.ui.Item;
import me.xorrad.lib.ui.ItemClickResult;
import me.xorrad.lib.ui.Menu;
import me.xorrad.ttrpg.TTRPG;
import me.xorrad.ttrpg.core.CharacterStats;
import me.xorrad.ttrpg.core.Culture;
import me.xorrad.ttrpg.core.traits.StatsTrait;
import me.xorrad.ttrpg.core.traits.CultureTrait;
import me.xorrad.ttrpg.localization.Localization;
import me.xorrad.ttrpg.util.SkinUtil;
import net.citizensnpcs.Citizens;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Inventory;
import net.citizensnpcs.editor.Editor;
import net.citizensnpcs.editor.EquipmentEditor;
import net.citizensnpcs.trait.Controllable;
import net.citizensnpcs.trait.FollowTrait;
import net.citizensnpcs.trait.SitTrait;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class CharacterMenu {

    public static void open(Player player, NPC npc) {
        Culture culture = npc.getOrAddTrait(CultureTrait.class).getCulture();
        boolean hasCulture = (culture != null);

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
                    .material(hasCulture ? Material.FLOWER_BANNER_PATTERN : Material.BARRIER)
                    .name("§eCulture")
                    .lore(hasCulture ? "§e" + culture.getName() : "§cNone")
                    .hideAttributes()
                    .leftClick((p, m) -> {
                        openChangeCulture(player, npc);
                        return ItemClickResult.OPEN_INVENTORY;
                    })
                    .rightClick((p, m) -> {
                        npc.getOrAddTrait(CultureTrait.class).setCulture(null);
                        ((Citizens) CitizensAPI.getPlugin()).storeNPCs(true);
                        m.set(1, m.get(1).material(Material.BARRIER).lore("§cNone"));
                        return ItemClickResult.NO_RESULT;
                    })
            )
            .add(new Item()
                    .material(Material.NETHER_STAR)
                    .name("§eReligion")
                    .lore("§ePLACEHOLDER")
            );

        addCharacterStats(menu, npc);

        for(int i = 0; i < 9; i++) {
            menu.set(2*9 + i, new Item().material(Material.WHITE_STAINED_GLASS_PANE));
        }

        for(int i = 0; i < 9; i++) {
            menu.set(4*9 + i, new Item().material(Material.WHITE_STAINED_GLASS_PANE));
        }

        menu.set(5*9, new Item()
                        .material(Material.SADDLE)
                        .name("Mount")
                        .lore("§eClick to mount character.")
                        .leftClick((p, m) -> {
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
                .leftClick((p, m) -> {
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
                .leftClick((p, m) -> {
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
                .leftClick((p, m) -> {
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
                    ((Citizens) CitizensAPI.getPlugin()).storeNPCs(true);
                    return ItemClickResult.NO_RESULT;
                })
        );

        menu.set(5*9+4, new Item()
                .material(Material.FEATHER)
                .name("Follow")
                .lore("§eClick to make this character follow/unfollow you.")
                .leftClick((p, m) -> {
                    if (!npc.isSpawned()) {
                        player.sendMessage("§cFailed to edit character because it is not spawned.");
                        return ItemClickResult.NO_RESULT;
                    }
                    FollowTrait trait = (FollowTrait)npc.getOrAddTrait(FollowTrait.class);
                    boolean toFollow = !trait.isEnabled();
                    trait.follow(toFollow ? player : null);
                    ((Citizens) CitizensAPI.getPlugin()).storeNPCs(true);
                    return ItemClickResult.NO_RESULT;
                })
        );

        menu.open(player);
    }

    public static void addCharacterStats(Menu menu, NPC npc) {
        int i = 0;
        for(CharacterStats stat : CharacterStats.values()) {
            StatsTrait trait = npc.getOrAddTrait(StatsTrait.class);

            int mod = trait.getStatModifier(stat);
            String statModifierText = ((mod < 0) ? "§c" : (mod > 0) ? "§a+" : "§7") + mod;

            Item item = new Item()
                    .material(stat.getIcon())
                    .name("§e" + Localization.valueOf("STAT_" + stat.name()).format())
                    .lore("§e" + trait.getStat(stat) + " §7(" + statModifierText + "§7)",
                            "",
                            "§7Left-click to §cdecrease§7 by one.",
                            "§7Right-click to §aincrease§7 by one.")
                    .hideAttributes();

            item.leftClick((p, m) -> {
                int val = Math.max(0, trait.getStat(stat) - 1);
                trait.setStat(stat, val);
                ((Citizens) CitizensAPI.getPlugin()).storeNPCs(true);
                addCharacterStats(m, npc);
                return ItemClickResult.NO_RESULT;
            });

            item.rightClick((p, m) -> {
                trait.setStat(stat, trait.getStat(stat) + 1);
                ((Citizens) CitizensAPI.getPlugin()).storeNPCs(true);
                addCharacterStats(m, npc);
                return ItemClickResult.NO_RESULT;
            });
            menu.set(9 + i, item);
            i++;
        }
    }

    public static void openChangeCulture(Player player, NPC npc) {
        Culture npcCulture = npc.getOrAddTrait(CultureTrait.class).getCulture();

        Menu menu = new Menu()
                .title("§eChoose a culture")
                .size(6)
                .onClick((p, item, event) -> { return true; });

        for(Culture culture : TTRPG.getInstance().cultures.values()) {
            menu.add(new Item()
                .material((culture == npcCulture) ? Material.NETHER_STAR : Material.FLOWER_BANNER_PATTERN)
                .name("§e" + culture.getName())
                .lore("§7Left-click to select this culture.")
                .hideAttributes()
                .leftClick((p, m) -> {
                    npc.getOrAddTrait(CultureTrait.class).setCulture(culture);
                    ((Citizens) CitizensAPI.getPlugin()).storeNPCs(true);
                    menu.setClosed(true);
                    open(player, npc);
                    return ItemClickResult.NO_RESULT;
                })
            );
        }

        menu.open(player);
    }

}

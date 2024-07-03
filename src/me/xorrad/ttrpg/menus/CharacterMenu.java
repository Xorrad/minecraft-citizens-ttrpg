package me.xorrad.ttrpg.menus;

import me.xorrad.lib.ui.Item;
import me.xorrad.lib.ui.ItemClickResult;
import me.xorrad.lib.ui.Menu;
import me.xorrad.ttrpg.TTRPG;
import me.xorrad.ttrpg.core.CharacterStats;
import me.xorrad.ttrpg.core.Culture;
import me.xorrad.ttrpg.core.Faith;
import me.xorrad.ttrpg.core.traits.FaithTrait;
import me.xorrad.ttrpg.core.traits.FamilyTrait;
import me.xorrad.ttrpg.core.traits.StatsTrait;
import me.xorrad.ttrpg.core.traits.CultureTrait;
import me.xorrad.ttrpg.localization.Localization;
import me.xorrad.ttrpg.util.SkinUtil;
import net.citizensnpcs.Citizens;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Inventory;
import net.citizensnpcs.api.trait.trait.MobType;
import net.citizensnpcs.editor.Editor;
import net.citizensnpcs.editor.EquipmentEditor;
import net.citizensnpcs.trait.Controllable;
import net.citizensnpcs.trait.FollowTrait;
import net.citizensnpcs.trait.SitTrait;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.function.Consumer;

import static java.awt.SystemColor.menu;

public class CharacterMenu extends Menu {
    public NPC npc;

//    +----------+-----------+--------------+--------------+----------+---------+---------+---------+---------+
//    | Name     | Culture   | Faith        |              |          |         |         |         |         |
//    +----------+-----------+--------------+--------------+----------+---------+---------+---------+---------+
//    | Strength | Dexterity | Constitution | Intelligence | Charisma |         |         |         |         |
//    +----------+-----------+--------------+--------------+----------+---------+---------+---------+---------+
//    | Greedy   | Loyal     | Chaste       |              |          |         |         |         |         |
//    +----------+-----------+--------------+--------------+----------+---------+---------+---------+---------+
//    | Father   | Mother    | Partner      | Child 1      | Child 2  | Child 3 | Child 4 | Child 5 | Child 6 |
//    +----------+-----------+--------------+--------------+----------+---------+---------+---------+---------+
//    | #        | #         | #            | #            | #        | #       | #       | #       | #       |
//    +----------+-----------+--------------+--------------+----------+---------+---------+---------+---------+
//    | Mount    | Inv       | Equip        | Sit          | Follow   | Select  |         |         |         |
//    +----------+-----------+--------------+--------------+----------+---------+---------+---------+---------+

    public CharacterMenu(NPC npc) {
        super();
        this.npc = npc;

        // Initialize the menu.
        this.title(Localization.MENU_TITLE_CHARACTER.format(npc.getName()));
        this.size(6);
        this.onClick((p, item, event) -> { return true; });

        this.update();
    }

    private void update() {
        this.updateInfo();
        this.updateStats();
        this.updateFamily();
        this.updateActions();
        this.updateRowBorders();
    }

    private void reopen(Player p) {
        this.destroy();
        this.update();
        this.open(p);
    }

    // Update characters information items
    // such as: name, culture, faith...
    private void updateInfo() {
        final int row = 0;

        Culture culture = npc.getOrAddTrait(CultureTrait.class).getCulture();
        Faith faith = npc.getOrAddTrait(FaithTrait.class).getFaith();
        boolean hasCulture = (culture != null);
        boolean hasFaith = (faith != null);

        this.set(row*9 + 0, new Item()
            .material(Material.PLAYER_HEAD)
            .name(Localization.NAME.format())
            .lore("§f" + npc.getName())
            .head(SkinUtil.getNPCProfile(npc))
        );

        this.set(row*9 + 1, new Item()
            .material(hasCulture ? Material.FLOWER_BANNER_PATTERN : Material.BARRIER)
            .name(Localization.CULTURE.format())
            .lore(hasCulture ? "§f" + culture.getName() : Localization.NONE.format())
            .hideAttributes()
            .leftClick((p, m) -> {
                openChangeCulture(p);
                return ItemClickResult.OPEN_INVENTORY;
            })
            .rightClick((p, m) -> {
                npc.getOrAddTrait(CultureTrait.class).setCulture(null);
                this.saveNPC();
                this.updateInfo();
                return ItemClickResult.NO_RESULT;
            })
        );

        this.set(row*9 + 2, new Item()
            .material(hasFaith ? Material.END_CRYSTAL : Material.BARRIER)
            .name(Localization.FAITH.format())
            .lore(hasFaith ? "§f" + faith.getName() : Localization.NONE.format())
            .leftClick((p, m) -> {
                openChangeFaith(p);
                return ItemClickResult.OPEN_INVENTORY;
            })
            .rightClick((p, m) -> {
                npc.getOrAddTrait(FaithTrait.class).setFaith(null);
                this.saveNPC();
                this.updateInfo();
                return ItemClickResult.NO_RESULT;
            })
        );
    }

    // Update character stats items such as:
    // strength, dexterity, intelligence, etc...
    private void updateStats() {
        final int row = 1;

        int i = 0;
        for(CharacterStats stat : CharacterStats.values()) {
            StatsTrait trait = npc.getOrAddTrait(StatsTrait.class);

            int mod = trait.getStatModifier(stat);
            String statModifierText = ((mod < 0) ? "§c" : (mod > 0) ? "§a+" : "§7") + mod;

            this.set(row*9 + i, new Item()
                .material(stat.getIcon())
                .name(Localization.valueOf("STAT_" + stat.name()).format())
                .lore("§f" + trait.getStat(stat) + " §7(" + statModifierText + "§7)",
                        "",
                        Localization.DECREASE_BY_ONE.format(),
                        Localization.INCREASE_BY_ONE.format())
                .hideAttributes()
                .leftClick((p, m) -> {
                    int val = Math.max(0, trait.getStat(stat) - 1);
                    trait.setStat(stat, val);
                    this.saveNPC();
                    this.updateStats();
                    return ItemClickResult.NO_RESULT;
                })
                .rightClick((p, m) -> {
                    trait.setStat(stat, trait.getStat(stat) + 1);
                    this.saveNPC();
                    this.updateStats();
                    return ItemClickResult.NO_RESULT;
                })
            );
            i++;
        }
    }

    // Update items for character family members
    // such as: father, mother, partner and children.
    private void updateFamily() {
        final int row = 3;

        FamilyTrait family = npc.getOrAddTrait(FamilyTrait.class);

        int i = 0;
        for(FamilyTrait.Role role : FamilyTrait.Role.values()) {
            this.set(row * 9 + i, makeFamilyHeadItem("§e" + Localization.valueOf("FAMILY_" + role.name()).format(), family.getRelative(role))
                    .leftClick((p, m) -> {
                        openChangeFamily(p, newRelative -> {
                            family.setRelative(role, newRelative);
                            p.sendMessage(Localization.FAMILY_CHANGED.format(npc.getName(), role.name().toLowerCase(), newRelative.getName()));
                            this.saveNPC();
                        });
                        return ItemClickResult.CLOSE_INVENTORY;
                    })
                    .rightClick((p, m) -> {
                        family.setRelative(role, null);
                        this.saveNPC();
                        this.updateFamily();
                        return ItemClickResult.NO_RESULT;
                    })
            );
            i++;
        }

        // Clear the children item slots beforehand
        // because if there aren't any children anymore,
        // it won't clear the former ones.
        for(int j = i; j < 9; j++) {
            this.set(row * 9 + j, new Item().material(Material.AIR));
        }

        for(NPC child : family.getChildren()) {
            this.set(row * 9 + i, makeFamilyHeadItem(Localization.FAMILY_CHILD.format(), child));
            i++;
        }
    }

    // Update items related to actions and operations
    // such as: follow, mount, inventory, etc...
    private void updateActions() {
        final int row = 5;

        this.set(row*9 + 0, new Item()
                .material(Material.SADDLE)
                .name(Localization.MOUNT.format())
                .lore(Localization.MOUNT_DESC.format())
                .leftClick((p, m) -> {
                    if (!npc.isSpawned()) {
                        p.sendMessage("§cFailed to mount character because it is not spawned.");
                        return ItemClickResult.NO_RESULT;
                    }
                    Controllable trait = (Controllable) npc.getOrAddTrait(Controllable.class);
                    trait.setEnabled(true);
                    trait.mount(p);
                    return ItemClickResult.CLOSE_INVENTORY;
                })
        );

        this.set(row*9 + 1, new Item()
                .material(Material.CHEST)
                .name(Localization.INVENTORY.format())
                .lore(Localization.INVENTORY_DESC.format())
                .leftClick((p, m) -> {
                    if (!npc.hasTrait(Inventory.class)) {
                        p.sendMessage("§cFailed to open the inventory for this character.");
                        return ItemClickResult.NO_RESULT;
                    }
                    Inventory trait = (Inventory) npc.getTraitNullable(Inventory.class);
                    this.setClosed(true);
                    trait.openInventory(p);
                    return ItemClickResult.OPEN_INVENTORY;
                })
        );

        this.set(row*9 + 2, new Item()
                .material(Material.IRON_CHESTPLATE)
                .name(Localization.EQUIP.format())
                .lore(Localization.EQUIP_DESC.format())
                .leftClick((p, m) -> {
                    if (!npc.isSpawned()) {
                        p.sendMessage("§cFailed to edit character because it is not spawned.");
                        return ItemClickResult.NO_RESULT;
                    }
                    this.setClosed(true);
                    Editor.enterOrLeave(p, new EquipmentEditor(p, npc));
                    return ItemClickResult.OPEN_INVENTORY;
                })
        );

        this.set(row*9 + 3, new Item()
                .material(Material.OAK_STAIRS)
                .name(Localization.SIT.format())
                .lore(Localization.SIT_DESC.format())
                .leftClick((p, m) -> {
                    if (!npc.isSpawned()) {
                        p.sendMessage("§cFailed to edit character because it is not spawned.");
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
                    this.saveNPC();
                    return ItemClickResult.NO_RESULT;
                })
        );

        this.set(row*9 + 4, new Item()
                .material(Material.FEATHER)
                .name(Localization.FOLLOW.format())
                .lore(Localization.FOLLOW_DESC.format())
                .leftClick((p, m) -> {
                    if (!npc.isSpawned()) {
                        p.sendMessage("§cFailed to edit character because it is not spawned.");
                        return ItemClickResult.NO_RESULT;
                    }

                    FollowTrait trait = (FollowTrait) npc.getOrAddTrait(FollowTrait.class);
                    boolean following = !trait.isEnabled();
                    trait.follow(following ? p : null);

                    if(!following) npc.removeTrait(FollowTrait.class);

                    this.saveNPC();
                    return ItemClickResult.NO_RESULT;
                })
        );

        this.set(row*9 + 5, new Item()
                .material(Material.STICK)
                .name(Localization.SELECT.format())
                .lore(Localization.SELECT_DESC.format())
                .leftClick((p, m) -> {
                    p.performCommand("npc select " + npc.getId());
                    return ItemClickResult.NO_RESULT;
                })
        );
    }

    // Add glass pane borders between specific rows.
    private void updateRowBorders() {
        addBorder(2);
        addBorder(4);
    }

    private void addBorder(int row) {
        for(int i = 0; i < 9; i++) {
            this.set(row*9 + i, new Item().material(Material.WHITE_STAINED_GLASS_PANE));
        }
    }

    public void openChangeCulture(Player player) {
        Culture npcCulture = npc.getOrAddTrait(CultureTrait.class).getCulture();

        Menu menu = new Menu()
                .title(Localization.MENU_TITLE_CHANGE_CULTURE.format(npc.getName()))
                .size(6)
                .onClick((p, item, event) -> { return true; });

        for(Culture culture : TTRPG.getInstance().cultures.values()) {
            menu.add(new Item()
                .material((culture == npcCulture) ? Material.NETHER_STAR : Material.FLOWER_BANNER_PATTERN)
                .name("§e" + culture.getName())
                .lore(Localization.CULTURE_SELECT.format())
                .hideAttributes()
                .leftClick((p, m) -> {
                    npc.getOrAddTrait(CultureTrait.class).setCulture(culture);
                    this.saveNPC();
                    menu.setClosed(true);
                    this.reopen(player);
                    return ItemClickResult.NO_RESULT;
                })
            );
        }

        menu.open(player);
    }

    public void openChangeFaith(Player player) {
        Faith npcFaith = npc.getOrAddTrait(FaithTrait.class).getFaith();

        Menu menu = new Menu()
                .title(Localization.MENU_TITLE_CHANGE_FAITH.format())
                .size(6)
                .onClick((p, item, event) -> { return true; });

        for(Faith faith : TTRPG.getInstance().faiths.values()) {
            menu.add(new Item()
                .material((faith == npcFaith) ? Material.NETHER_STAR : Material.END_CRYSTAL)
                .name("§e" + faith.getName())
                .lore(Localization.FAITH.format())
                .hideAttributes()
                .leftClick((p, m) -> {
                    npc.getOrAddTrait(FaithTrait.class).setFaith(faith);
                    this.saveNPC();
                    menu.setClosed(true);
                    this.reopen(player);
                    return ItemClickResult.NO_RESULT;
                })
            );
        }

        menu.open(player);
    }

    public void openChangeFamily(Player player, Consumer<NPC> applyFunction) {
        player.closeInventory();
        player.sendMessage(Localization.FAMILY_SELECT.format());

        Bukkit.getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onClick(EntityDamageByEntityEvent event) {
                if(!CitizensAPI.getNPCRegistry().isNPC(event.getEntity()))
                    return;
                if(!event.getDamager().equals(player))
                    return;
                Player player = (Player) event.getDamager();
                NPC target = CitizensAPI.getNPCRegistry().getNPC(event.getEntity());

                if (target == null)
                    return;

                event.setCancelled(true);
                HandlerList.unregisterAll(this);
                applyFunction.accept(target);
            }
        }, TTRPG.getInstance());
    }

    private Item makeFamilyHeadItem(String title, NPC relative) {
        if(relative == null) {
            return new Item()
                .material(Material.BARRIER)
                .name(title)
                .lore(Localization.NONE.format());
        }

        return new Item()
                .material(Material.PLAYER_HEAD)
                .name(title)
                .lore("§f" + relative.getName())
                .head(SkinUtil.getNPCProfile(relative));
    }

    private void saveNPC() {
        ((Citizens) CitizensAPI.getPlugin()).storeNPCs(true);
    }

//    @EventHandler
//    @Override
//    public void onInventoryClose(InventoryCloseEvent event) {
//        super.onInventoryClose(event);
//    }
//
//    @EventHandler
//    @Override
//    public void onInventoryClick(InventoryClickEvent event) {
//        super.onInventoryClick(event);
//    }
}

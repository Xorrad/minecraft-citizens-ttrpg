package me.xorrad.ttrpg.menus;

import me.xorrad.lib.ui.Item;
import me.xorrad.lib.ui.ItemClickResult;
import me.xorrad.lib.ui.Menu;
import me.xorrad.ttrpg.localization.Localization;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class CharacterMenu {

    public static void open(Player player) {
        new Menu()
                .title(Localization.CHARACTER_MENU_TITLE.format(player.getName()))
                .size(2)
                .add(new Item().material(Material.IRON_SWORD))
                .add(new Item()
                        .material(Material.BOOK)
                        .amount(10)
                        .name("§aAccounts")
                        .lore("§eHello world")
                        .onClick((p, menu) -> {
                            p.sendMessage("hello world");
                            return ItemClickResult.NO_RESULT;
                        })
                )
                .onClick((p, item, event) -> {
                    return true;
                })
                .open(player);
    }

}

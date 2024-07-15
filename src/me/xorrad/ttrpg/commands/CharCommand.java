package me.xorrad.ttrpg.commands;

import me.xorrad.lib.commands.CommandGroup;
import me.xorrad.lib.commands.CommandParameter;
import me.xorrad.ttrpg.TTRPG;
import me.xorrad.ttrpg.core.Culture;
import me.xorrad.ttrpg.core.Warp;
import me.xorrad.ttrpg.core.names.NameType;
import me.xorrad.ttrpg.util.CitizensUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class CharCommand extends CommandGroup {

    public CharCommand() {
        super("char");

        newSubCommand("new")
                .playerOnly(true)
                .param(new CommandParameter("culture", CommandParameter.Type.STRING))
                .param(new CommandParameter("gender", CommandParameter.Type.STRING, "MALE"))
                .complete((sender, args) -> {
                    if(args.length == 1) return TTRPG.getInstance().cultures.values().stream().map(Culture::getId).filter(s -> s.startsWith(args[0].toUpperCase())).toList();
                    if(args.length == 2) return Arrays.stream(NameType.values()).map(NameType::name).filter(s -> s.startsWith(args[1].toUpperCase())).toList();
                    return Collections.emptyList();
                })
                .execute(CharCommand::newCharacterWithCulture);
    }

    public static void newCharacterWithCulture(CommandSender sender, Object[] params) {
        Player player = (Player) sender;
        String cultureId = ((String) params[0]).toUpperCase();
        String nameTypeStr = (String) params[1];

        if(!TTRPG.getInstance().cultures.containsKey(cultureId)) {
            sender.sendMessage("§eThe culture §7" + cultureId + "§e does not exist.");
            return;
        }

        if(!NameType.exists(nameTypeStr)) {
            sender.sendMessage("§eThe nameType §7" + nameTypeStr + "§e is not valid.");
            return;
        }

        Culture culture = TTRPG.getInstance().cultures.get(cultureId);
        NameType nameType = NameType.valueOf(nameTypeStr.toUpperCase());

        new BukkitRunnable() {
            public void run() {
                String name = culture.getNameManager().get(nameType) + " " + culture.getNameManager().get(NameType.FAMILY);
                CitizensUtil.spawnNPC(name, player.getLocation(), culture);
                player.sendMessage("§eA NPC named §7\"" + name + "\" §e(from culture §7" + culture.getName() + "§e) been created at your location.");
                this.cancel();
            }
        }.runTask((Plugin) TTRPG.getInstance());

    }
}

package me.xorrad.ttrpg.commands;

import me.xorrad.lib.commands.CommandGroup;
import me.xorrad.lib.commands.CommandParameter;
import me.xorrad.ttrpg.TTRPG;
import me.xorrad.ttrpg.core.Warp;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;

public class WarpCommand extends CommandGroup {

    public WarpCommand() {
        super("warp");

        newDefaultCommand()
                .playerOnly(true)
                .param(new CommandParameter("name", CommandParameter.Type.STRING))
                .complete((sender, args) -> {
                    ArrayList<String> l = new ArrayList<>();
                    l.addAll(this.getCommandNames());
                    l.addAll(TTRPG.getInstance().warps.values().stream().map(Warp::getName).toList());
                    return l;
                })
                .execute(WarpCommand::teleport);

        newSubCommand("set")
                .playerOnly(false)
                .param(new CommandParameter("name", CommandParameter.Type.STRING))
                .execute(WarpCommand::set);

        newSubCommand("delete")
                .playerOnly(false)
                .param(new CommandParameter("name", CommandParameter.Type.STRING))
                .complete((sender, args) -> {
                    if(args.length == 1) return TTRPG.getInstance().warps.values().stream().map(Warp::getName).toList();
                    return Collections.emptyList();
                })
                .execute(WarpCommand::delete);

        newSubCommand("list")
                .playerOnly(false)
                .execute(WarpCommand::listWarps);
    }

    public static void teleport(CommandSender sender, Object[] params) {
        String name = (String) params[0];
        Warp warp = TTRPG.getInstance().warps.get(name);

        if(warp == null) {
            sender.sendMessage("§eThe warp §7" + name + "§e does not exist.");
            return;
        }

        Player player = (Player) sender;
        player.teleport(warp.getLocation());
        sender.sendMessage("§eYou have been teleported to §7" + name + "§e.");
    }

    public static void listWarps(CommandSender sender, Object[] params) {
        sender.sendMessage("§e--------------------");
        sender.sendMessage("§eAvailable warps:");

        if(sender instanceof Player) {
            for(String name : TTRPG.getInstance().warps.keySet()) {
                TextComponent message = new TextComponent("§e- §7§n" + name);
                message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/warp " + name));
                message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§eTeleport to " + name + ".").create()));
                ((Player) sender).spigot().sendMessage(message);
            }
        }
        else {
            for(String name : TTRPG.getInstance().warps.keySet())
                sender.sendMessage("§e- §7" + name);
        }

        sender.sendMessage("§e--------------------");
    }

    public static void set(CommandSender sender, Object[] params) {
        Player player = (Player) sender;
        String name = (String) params[0];
        Location location = player.getLocation();
        Warp warp = new Warp(name, location, player.getUniqueId());

        TTRPG.getInstance().warps.put(name, warp);
        warp.save();

        sender.sendMessage("§eThe warp §7" + name + "§e has been saved.");
    }

    public static void delete(CommandSender sender, Object[] params) {
        String name = (String) params[0];

        if(!TTRPG.getInstance().warps.containsKey(name)) {
            sender.sendMessage("§eThe warp §7" + name + "§e does not exist.");
            return;
        }

        TTRPG.getInstance().getConfig("warps").set(name, null);
        TTRPG.getInstance().getConfig("warps").save();
        TTRPG.getInstance().warps.remove(name);

        sender.sendMessage("§eThe warp §7" + name + "§e has been deleted.");
    }
}

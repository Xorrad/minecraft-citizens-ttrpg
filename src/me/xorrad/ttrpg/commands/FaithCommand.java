package me.xorrad.ttrpg.commands;

import me.xorrad.lib.commands.CommandGroup;
import me.xorrad.lib.commands.CommandParameter;
import me.xorrad.ttrpg.TTRPG;
import me.xorrad.ttrpg.core.Culture;
import me.xorrad.ttrpg.core.Faith;
import me.xorrad.ttrpg.core.names.IListNamesManager;
import me.xorrad.ttrpg.core.names.NameType;
import me.xorrad.ttrpg.core.names.NamesManager;
import me.xorrad.ttrpg.core.names.types.ListNamesManager;
import me.xorrad.ttrpg.localization.Localization;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FaithCommand extends CommandGroup {

    public FaithCommand() {
        super("faith");

        newSubCommand("new")
                .playerOnly(false)
                .param(new CommandParameter("id", CommandParameter.Type.STRING))
                .param(new CommandParameter("name", CommandParameter.Type.STRING, ""))
                .execute(FaithCommand::newFaith);

        newSubCommand("delete")
                .playerOnly(false)
                .param(new CommandParameter("id", CommandParameter.Type.STRING))
                .complete((sender, args) -> {
                    if(args.length == 1) return getFaithsList();
                    return Collections.emptyList();
                })
                .execute(FaithCommand::deleteFaith);

        newSubCommand("list")
                .playerOnly(false)
                .execute(FaithCommand::listFaiths);
    }

    public static List<String> getFaithsList() {
        return TTRPG.getInstance().faiths.values()
                .stream()
                .map(Faith::getId)
                .toList();
    }

    public static void newFaith(CommandSender sender, Object[] params) {
        String faithId = ((String) params[0]).toUpperCase();
        String faithName = ((String) params[1]).isEmpty() ? faithId : (String) params[1];

        if(TTRPG.getInstance().faiths.containsKey(faithId)) {
            sender.sendMessage(Localization.CULTURE_EXISTS.format(faithId));
            return;
        }

        Faith faith = new Faith(faithId, faithName);
        faith.save();
        TTRPG.getInstance().faiths.put(faithId, faith);
        sender.sendMessage(Localization.CULTURE_CREATED.format(faithId));
    }

    public static void deleteFaith(CommandSender sender, Object[] objects) {
        String faithId = ((String) objects[0]).toUpperCase();

        if(!TTRPG.getInstance().faiths.containsKey(faithId)) {
            sender.sendMessage(Localization.CULTURE_DOESNT_EXISTS.format(faithId));
            return;
        }

        TTRPG.getInstance().faiths.remove(faithId);

        TTRPG.getInstance().getConfig("faiths").set(faithId, null);
        TTRPG.getInstance().getConfig("faiths").save();

        sender.sendMessage(Localization.CULTURE_DELETED.format(faithId));
    }

    public static void listFaiths(CommandSender sender, Object[] objects)
    {
        sender.sendMessage(Localization.SEPARATOR.format());
        sender.sendMessage("§eFaiths:");

        for(Faith faith : TTRPG.getInstance().faiths.values()) {
            sender.sendMessage("§e- §7§n" + faith.getId() + " \"" + faith.getName() + "\"");
        }

        sender.sendMessage(Localization.SEPARATOR.format());
    }
}

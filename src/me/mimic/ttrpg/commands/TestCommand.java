package me.mimic.ttrpg.commands;

import me.mimic.lib.commands.CommandGroup;
import me.mimic.lib.commands.CommandParameter;
import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.Collections;

public class TestCommand extends CommandGroup {

    public TestCommand() {
        super("test");

        newDefaultCommand().execute((sender, params) -> {
            sender.sendMessage("The default command for " + this.getName() + " has been executed.");
        });

        newSubCommand("args")
                .playerOnly(true)
                .param(new CommandParameter("name", CommandParameter.Type.STRING))
                .param(new CommandParameter("amount", CommandParameter.Type.INT))
                .execute((sender, params) -> {
                    sender.sendMessage("§aname: §f" + ((String) params[0]));
                    sender.sendMessage("§aamount: §f" + ((int) params[1]));
                })
                .complete((sender, params) -> {
                    if(params.length == 1)
                        return Bukkit.getOnlinePlayers().stream().map(p -> p.getName()).toList();
                    if(params.length > 1)
                        return Arrays.stream(new int[]{1, 2, 3, 4, 5, 6}).mapToObj(String::valueOf).toList();
                    return Collections.<String>emptyList();
                });

        newSubCommand("gui")
                .playerOnly(true)
                .execute((sender, params) -> {
                    //CharacterMenu.open((Player) sender);
                });
    }
}

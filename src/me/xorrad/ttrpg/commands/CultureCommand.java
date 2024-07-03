package me.xorrad.ttrpg.commands;

import me.xorrad.lib.commands.CommandGroup;
import me.xorrad.lib.commands.CommandParameter;
import me.xorrad.ttrpg.TTRPG;
import me.xorrad.ttrpg.core.Culture;
import me.xorrad.ttrpg.core.names.IListNamesManager;
import me.xorrad.ttrpg.core.names.NameType;
import me.xorrad.ttrpg.core.names.NamesManager;
import me.xorrad.ttrpg.core.names.types.ListNamesManager;
import me.xorrad.ttrpg.core.names.types.RinkworksNamesManager;
import me.xorrad.ttrpg.localization.Localization;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CultureCommand extends CommandGroup {

    public CultureCommand() {
        super("culture");

        //newDefaultCommand().execute((sender, params) -> {});

        newSubCommand("new")
                .playerOnly(false)
                .param(new CommandParameter("id", CommandParameter.Type.STRING))
                .param(new CommandParameter("name", CommandParameter.Type.STRING, ""))
                .execute(CultureCommand::newCulture);

        newSubCommand("delete")
                .playerOnly(false)
                .param(new CommandParameter("id", CommandParameter.Type.STRING))
                .complete((sender, args) -> {
                    if(args.length == 1) return getCulturesList();
                    return Collections.emptyList();
                })
                .execute(CultureCommand::deleteCulture);

        newSubCommand("add_name")
                .playerOnly(false)
                .param(new CommandParameter("id", CommandParameter.Type.STRING))
                .param(new CommandParameter("type", CommandParameter.Type.STRING))
                .param(new CommandParameter("name", CommandParameter.Type.STRING))
                .complete((sender, args) -> {
                    if(args.length == 1) return getCulturesList();
                    if(args.length == 2) return Arrays.stream(NameType.values()).map(Enum::name).toList();
                    return Collections.emptyList();
                })
                .execute(CultureCommand::addName);

        newSubCommand("remove_name")
                .playerOnly(false)
                .param(new CommandParameter("id", CommandParameter.Type.STRING))
                .param(new CommandParameter("type", CommandParameter.Type.STRING))
                .param(new CommandParameter("name", CommandParameter.Type.STRING))
                .complete((sender, args) -> {
                    if(args.length == 1) return getCulturesList();
                    if(args.length == 2) return Arrays.stream(NameType.values()).map(Enum::name).toList();
                    if(NameType.exists(args[1])) {
                        NameType nameType = NameType.valueOf(args[1]);
                        Culture culture = TTRPG.getInstance().cultures.get(args[0]);
                        if (args.length == 3 && culture != null && culture.getNameManager() instanceof IListNamesManager) {
                            return ((ListNamesManager) culture.getNameManager()).getNames(nameType);
                        }
                    }
                    return Collections.emptyList();
                })
                .execute(CultureCommand::removeName);

        newSubCommand("list")
                .playerOnly(false)
                .execute(CultureCommand::listCultures);

        newSubCommand("info")
                .playerOnly(false)
                .param(new CommandParameter("id", CommandParameter.Type.STRING))
                .complete((sender, args) -> {
                    if(args.length == 1) return getCulturesList();
                    return Collections.emptyList();
                })
                .execute(CultureCommand::info);

        newSubCommand("type")
                .playerOnly(false)
                .param(new CommandParameter("id", CommandParameter.Type.STRING))
                .param(new CommandParameter("generator", CommandParameter.Type.STRING))
                .complete((sender, args) -> {
                    if(args.length == 1) return getCulturesList();
                    if(args.length == 2) return Arrays.stream(NamesManager.Type.values()).map(Enum::name).toList();
                    return Collections.emptyList();
                })
                .execute(CultureCommand::changeType);

        newSubCommand("generate")
                .playerOnly(false)
                .param(new CommandParameter("id", CommandParameter.Type.STRING))
                .param(new CommandParameter("type", CommandParameter.Type.STRING))
                .param(new CommandParameter("count", CommandParameter.Type.INT, 5))
                .complete((sender, args) -> {
                    if(args.length == 1) return getCulturesList();
                    if(args.length == 2) return Arrays.stream(NameType.values()).map(Enum::name).toList();
                    return Collections.emptyList();
                })
                .execute(CultureCommand::generateNames);

        newSubCommand("rinkswork")
                .playerOnly(false)
                .param(new CommandParameter("id", CommandParameter.Type.STRING))
                .param(new CommandParameter("type", CommandParameter.Type.STRING))
                .param(new CommandParameter("template", CommandParameter.Type.STRING))
                .complete((sender, args) -> {
                    if(args.length == 1) return getCulturesList();
                    return Collections.emptyList();
                })
                .execute(CultureCommand::changeRinkworksTemplate);
    }

    public static List<String> getCulturesList() {
        return TTRPG.getInstance().cultures.values()
                .stream()
                .map(Culture::getId)
                .toList();
    }

    public static void newCulture(CommandSender sender, Object[] params) {
        String cultureId = ((String) params[0]).toUpperCase();
        String cultureName = ((String) params[1]).isEmpty() ? cultureId : (String) params[1];

        if(TTRPG.getInstance().cultures.containsKey(cultureId)) {
            sender.sendMessage(Localization.CMD_CULTURE_EXISTS.format(cultureId));
            return;
        }

        ListNamesManager nameManager = new ListNamesManager(cultureId);
        nameManager.setFilePath("names/" + cultureId.toLowerCase());
        Culture culture = new Culture(cultureId, cultureName, nameManager);
        culture.save();
        TTRPG.getInstance().cultures.put(cultureId, culture);
        sender.sendMessage(Localization.CMD_CULTURE_CREATED.format(cultureId));
    }

    public static void deleteCulture(CommandSender sender, Object[] objects) {
        String cultureId = ((String) objects[0]).toUpperCase();

        if(!TTRPG.getInstance().cultures.containsKey(cultureId)) {
            sender.sendMessage(Localization.CMD_CULTURE_DOESNT_EXIST.format(cultureId));
            return;
        }

        TTRPG.getInstance().cultures.remove(cultureId);

        TTRPG.getInstance().getConfig("cultures").set(cultureId, null);
        TTRPG.getInstance().getConfig("cultures").save();

        sender.sendMessage(Localization.CMD_CULTURE_DELETED.format(cultureId));
    }

    public static void addName(CommandSender sender, Object[] objects) {
        String cultureId = ((String) objects[0]).toUpperCase();
        String nameTypeStr = (String) objects[1];
        String name = (String) objects[2];

        if(!NameType.exists(nameTypeStr)) {
            sender.sendMessage(Localization.CMD_CULTURE_NAMETYPE_INVALID.format(nameTypeStr));
            return;
        }

        if(!TTRPG.getInstance().cultures.containsKey(cultureId)) {
            sender.sendMessage(Localization.CMD_CULTURE_DOESNT_EXIST.format(cultureId));
            return;
        }

        Culture culture = TTRPG.getInstance().cultures.get(cultureId);
        NameType nameType = NameType.valueOf(nameTypeStr.toUpperCase());

        if(!(culture.getNameManager() instanceof IListNamesManager)) {
            sender.sendMessage(Localization.CMD_CULTURE_NO_MANUAL_NAMES.format(cultureId));
            return;
        }

        ((IListNamesManager) culture.getNameManager()).addName(nameType, name);
        culture.save();
        sender.sendMessage(Localization.CMD_CULTURE_NAME_ADDED.format(name, cultureId, nameType.name()));
    }

    public static void removeName(CommandSender sender, Object[] objects) {
        String cultureId = ((String) objects[0]).toUpperCase();
        String nameTypeStr = (String) objects[1];
        String name = (String) objects[2];

        if(!NameType.exists(nameTypeStr)) {
            sender.sendMessage(Localization.CMD_CULTURE_NAMETYPE_INVALID.format(nameTypeStr));
            return;
        }

        if(!TTRPG.getInstance().cultures.containsKey(cultureId)) {
            sender.sendMessage(Localization.CMD_CULTURE_DOESNT_EXIST.format(cultureId));
            return;
        }

        Culture culture = TTRPG.getInstance().cultures.get(cultureId);
        NameType nameType = NameType.valueOf(nameTypeStr.toUpperCase());

        if(!(culture.getNameManager() instanceof IListNamesManager)) {
            sender.sendMessage(Localization.CMD_CULTURE_NO_MANUAL_NAMES.format(cultureId));
            return;
        }

        IListNamesManager manager = (IListNamesManager) culture.getNameManager();

        if(!manager.containsName(nameType, name)) {
            sender.sendMessage(Localization.CMD_CULTURE_NAME_DOESNT_EXIST.format(name, cultureId, nameType.name()));
            return;
        }

        manager.removeName(nameType, name);
        culture.save();
        sender.sendMessage(Localization.CMD_CULTURE_NAME_REMOVED.format(name, cultureId, nameType.name()));
    }

    public static void listCultures(CommandSender sender, Object[] objects)
    {
        sender.sendMessage(Localization.SEPARATOR.format());
        sender.sendMessage(Localization.CMD_CULTURE_LIST.format());

        if(sender instanceof Player)
        {
            for(Culture culture : TTRPG.getInstance().cultures.values())
            {
                TextComponent message = new TextComponent("§e- §7§n" + culture.getId() + "§r§7 \"" + culture.getName() + "\"");
                message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/culture info " + culture.getId()));
                message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Localization.CMD_CULTURE_SHOW.format(culture.getId())).create()));
                ((Player) sender).spigot().sendMessage(message);
            }
        }
        else
        {
            for(Culture culture : TTRPG.getInstance().cultures.values())
                sender.sendMessage("§e- §7§n" + culture.getId() + " \"" + culture.getName() + "\"");
        }

        sender.sendMessage(Localization.SEPARATOR.format());
    }

    public static void info(CommandSender sender, Object[] objects) {
        String cultureId = ((String) objects[0]).toUpperCase();

        if(!TTRPG.getInstance().cultures.containsKey(cultureId)) {
            sender.sendMessage(Localization.CMD_CULTURE_DOESNT_EXIST.format(cultureId));
            return;
        }

        Culture culture = TTRPG.getInstance().cultures.get(cultureId);

        sender.sendMessage(Localization.SEPARATOR.format());
        sender.sendMessage(Localization.CMD_CULTURE_INFO.format(culture.getId()));
        sender.sendMessage(Localization.CMD_CULTURE_INFO_NAME.format(culture.getName()));
        sender.sendMessage(Localization.CMD_CULTURE_INFO_NAMEMANAGER.format());
        for(String s : culture.getNameManager().getResume())
            sender.sendMessage("   " + s);
        sender.sendMessage(Localization.SEPARATOR.format());
    }

    public static void changeType(CommandSender sender, Object[] objects) {
        String cultureId = ((String) objects[0]).toUpperCase();
        String typeStr = ((String) objects[1]).toUpperCase();

        if(!TTRPG.getInstance().cultures.containsKey(cultureId)) {
            sender.sendMessage(Localization.CMD_CULTURE_DOESNT_EXIST.format(cultureId));
            return;
        }

        if(!NamesManager.Type.exists(typeStr)) {
            sender.sendMessage(Localization.CMD_CULTURE_NAMEMANAGER_INVALID.format(typeStr));
            return;
        }

        Culture culture = TTRPG.getInstance().cultures.get(cultureId);
        NamesManager.Type type = NamesManager.Type.valueOf(typeStr);
        try {
            NamesManager namesManager = type.getClazz().getConstructor(String.class).newInstance(culture.getId());
            namesManager.setDefaultConfig(culture);
            culture.setNamesManager(namesManager);
            culture.save();

            sender.sendMessage(Localization.CMD_CULTURE_NAMEMANAGER_CHANGED.format(culture.getId(), type.name().toUpperCase()));
        }
        catch (Exception e) {
            e.printStackTrace();
            sender.sendMessage(Localization.ERROR.format());
        }
    }

    public static void generateNames(CommandSender sender, Object[] objects) {
        String cultureId = ((String) objects[0]).toUpperCase();
        String nameTypeStr = ((String) objects[1]).toUpperCase();
        int n = (int) objects[2];

        if(!TTRPG.getInstance().cultures.containsKey(cultureId)) {
            sender.sendMessage(Localization.CMD_CULTURE_DOESNT_EXIST.format(cultureId));
            return;
        }

        if(!NameType.exists(nameTypeStr)) {
            sender.sendMessage(Localization.CMD_CULTURE_NAMETYPE_INVALID.format(nameTypeStr));
            return;
        }

        Culture culture = TTRPG.getInstance().cultures.get(cultureId);
        NameType type = NameType.valueOf(nameTypeStr);

        for(int i = 0; i < n; i++)
        {
            String generatedName = culture.getNameManager().get(type);

            if(type != NameType.FAMILY)
                generatedName += " " + culture.getNameManager().get(NameType.FAMILY);

            sender.sendMessage("  §e" + (i+1) + ". §7\"" + generatedName + "\"");
        }
    }

    public static void changeRinkworksTemplate(CommandSender sender, Object[] objects) {
        String cultureId = ((String) objects[0]).toUpperCase();
        String nameTypeStr = ((String) objects[1]).toUpperCase();
        String template = (String) objects[2];

        if(!TTRPG.getInstance().cultures.containsKey(cultureId)) {
            sender.sendMessage(Localization.CMD_CULTURE_DOESNT_EXIST.format(cultureId));
            return;
        }

        if(!NameType.exists(nameTypeStr)) {
            sender.sendMessage(Localization.CMD_CULTURE_NAMETYPE_INVALID.format(nameTypeStr));
            return;
        }

        Culture culture = TTRPG.getInstance().cultures.get(cultureId);
        NameType type = NameType.valueOf(nameTypeStr);

        if(!(culture.getNameManager() instanceof RinkworksNamesManager)) {
            sender.sendMessage(Localization.CMD_CULTURE_NAMEMANAGER_TEMPLATE_UNSUPPORTED.format(nameTypeStr));
            return;
        }

        ((RinkworksNamesManager) culture.getNameManager()).setTemplate(type, template);
        culture.save();

        sender.sendMessage(Localization.CMD_CULTURE_NAMEMANAGER_TEMPLATE_CHANGED.format(type.name().toLowerCase(), culture.getId(), template));
    }
}

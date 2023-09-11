package be.isach.musicalmobs.commands;

import be.isach.musicalmobs.config.MessageManager;
import be.isach.musicalmobs.util.PlayerUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

/**
 * Created by sacha on 21/07/15.
 */
public class CommandsManager implements CommandExecutor {

    /* {@see java.util.ArrayList} of the commands. */
    private ArrayList<MusicalMobsCommand> cmds = new ArrayList<MusicalMobsCommand>();

    /**
     * CommandsManager constructor.
     * Loads all the commands.
     */
    public CommandsManager() {
        cmds.add(new Create());
        cmds.add(new Delete());
        cmds.add(new ForceStart());
        cmds.add(new ForceStop());
        cmds.add(new Join());
        cmds.add(new Leave());
        cmds.add(new Reload());
        cmds.add(new Set());
        cmds.add(new Infos());
        cmds.add(new List());
    }

    /**
     * Triggered when a player executed the command /ms | /musicalmobs.
     *
     * @param sender       Source of the command.
     * @param cmd          {@see org.bukkit.command.Command} which was executed
     * @param commandLabel Alias of the command which was used.
     * @param args         Passed command arguments
     * @return {@code true} if a valid command, otherwise {@code false}
     */
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command is only available for players!");
            return true;
        }

        Player p = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("musicalmobs")
                || cmd.getName().equalsIgnoreCase("ms")) {
            if (args.length == 0) {
                if(p.hasPermission("musicalmobs.setup")) {
                    p.sendMessage("");
                    p.sendMessage("§f§l------------*+ §oMusical Sheeps §f§l+*------------");
                    PlayerUtils.sendRawMessageWithCommand(p, "§6§l/ms §a§lcreate §f| §a§lc §7- " + MessageManager.getMessage("Commands.Create"), "/ms create");
                    PlayerUtils.sendRawMessageWithCommand(p, "§6§l/ms §a§ldelete §f| §a§ld §7- " + MessageManager.getMessage("Commands.Delete"), "/ms delete ");
                    PlayerUtils.sendRawMessageWithCommand(p, "§6§l/ms §a§lset §f| §a§ls §7- " + MessageManager.getMessage("Commands.Set"), "/ms set ");
                    PlayerUtils.sendRawMessageWithCommand(p, "§6§l/ms §a§linfo §f| §a§li §7- " + MessageManager.getMessage("Commands.Infos"), "/ms info ");
                    PlayerUtils.sendRawMessageWithCommand(p, "§6§l/ms §a§lreload §f| §a§lr §7- " + MessageManager.getMessage("Commands.Reload"), "/ms reload");
                    PlayerUtils.sendRawMessageWithCommand(p, "§6§l/ms §a§ljoin §f| §a§lj §7- " + MessageManager.getMessage("Commands.Join"), "/ms join ");
                    PlayerUtils.sendRawMessageWithCommand(p, "§6§l/ms §a§lleave §f| §a§ll §7- " + MessageManager.getMessage("Commands.Leave"), "/ms leave");
                    PlayerUtils.sendRawMessageWithCommand(p, "§6§l/ms §a§lfstart §f| §a§lstart §7- " + MessageManager.getMessage("Commands.Force-Start"), "/ms fstart ");
                    PlayerUtils.sendRawMessageWithCommand(p, "§6§l/ms §a§lfstop §f| §a§lstop §7- " + MessageManager.getMessage("Commands.Force-Stop"), "/ms fstop ");
                    PlayerUtils.sendRawMessageWithCommand(p, "§6§l/ms §a§llist §7- " + MessageManager.getMessage("Commands.List"), "/ms list");
                    p.sendMessage("");
                } else {
                    p.sendMessage("");
                    p.sendMessage("§f§l------------*+ §oMusical Sheeps §f§l+*------------");
                    PlayerUtils.sendRawMessageWithCommand(p, "§6§l/ms §a§ljoin §f| §a§lj §7- " + MessageManager.getMessage("Commands.Join"), "/ms join ");
                    PlayerUtils.sendRawMessageWithCommand(p, "§6§l/ms §a§lleave §f| §a§ll §7- " + MessageManager.getMessage("Commands.Leave"), "/ms leave");
                    p.sendMessage("");
                }
                return true;
            }

            MusicalMobsCommand c = getCommand(args[0]);

            if (c == null) {
                sender.sendMessage("");
                sender.sendMessage(MessageManager.getMessage("Command-Not-Exist"));
                sender.sendMessage("");
                return true;
            }

            Vector<String> a = new Vector<String>(Arrays.asList(args));
            a.remove(0);
            args = a.toArray(new String[a.size()]);

            c.onCommand(p, args);

            return true;
        }
        return true;
    }

    /**
     * Get the aliases.
     *
     * @param cmd The MusicalSheep {@see org.bukkit.command.Command}.
     * @return the aliases.
     */
    private String aliases(MusicalMobsCommand cmd) {
        String fin = "";

        for (String a : cmd.getAliases()) {
            fin += a + " | ";
        }

        return fin.substring(0, fin.lastIndexOf(" | "));
    }

    /**
     * Gets the {@see org.bukkit.command.Command}.
     *
     * @param name The {@see org.bukkit.command.Command} name.
     * @return The {@see org.bukkit.command.Command}.
     */
    private MusicalMobsCommand getCommand(String name) {
        for (MusicalMobsCommand cmd : cmds) {
            if (cmd.getClass().getSimpleName().equalsIgnoreCase(name)) return cmd;
            for (String alias : cmd.getAliases()) if (name.equalsIgnoreCase(alias)) return cmd;
        }
        return null;
    }
}
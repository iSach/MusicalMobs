package be.isach.musicalmobs.commands;

import be.isach.musicalmobs.MusicalMobs;
import be.isach.musicalmobs.arena.Arena;
import be.isach.musicalmobs.arena.ArenaManager;
import be.isach.musicalmobs.config.MessageManager;
import be.isach.musicalmobs.config.SettingsManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * Created by sacha on 21/07/15.
 *
 * Used to set the infos of an arena.
 */
public class Set extends MusicalMobsCommand {

    public Set() {
        super("§7Set something", "", "set", "s");
    }

    @Override
    public void onCommand(Player p, String[] args) {

        if(!p.hasPermission("musicalmobs.setup")) {
            p.sendMessage(MessageManager.getMessage("No-Permission"));
            return;
        }

        if (args == null || args.length < 2) {
            p.sendMessage("");
            p.sendMessage("§c§lWrong usage!");
            p.sendMessage("§f§l/ms set <id> §7minplayers <minplayers>");
            p.sendMessage("                   §7maxplayers <maxplayers>");
            p.sendMessage("                   §7displayname <display_name>");
            p.sendMessage("                   §7lobbycountdown <lobbycountdown>");
            p.sendMessage("                   §7song <song>");
            p.sendMessage("                   §7playerspawn");
            p.sendMessage("                   §7sheepspawn");
            p.sendMessage("");
            return;
        } else {
            if (ArenaManager.getInstance().getArena(Integer.parseInt(args[0])) == null) {
                p.sendMessage("");
                p.sendMessage("§c§l§oThe arena " + args[0] + " doesn't exist!");
                p.sendMessage("");
                return;
            }
            Arena a = ArenaManager.getInstance().getArena(Integer.parseInt(args[0]));
            int id = a.getID();

            String subarg = args[1].toLowerCase();
            switch (subarg) {
                case "minplayers":
                    if (args.length < 3) {
                        p.sendMessage("");
                        p.sendMessage("§c§lWrong usage!");
                        p.sendMessage("§f§l/ms set <id> minplayers <minplayers>");
                        p.sendMessage("");
                        p.sendMessage("§7§oExample: /ms set 1 minplayers 4");
                        p.sendMessage("");
                        return;
                    }
                    SettingsManager.getArenas().set("arenas." + id + ".minPlayers", Integer.parseInt(args[2]));
                    p.sendMessage("§6§lMin Players of Arena " + id + " set to " + args[2] + "!");
                    break;
                case "maxplayers":
                    if (args.length < 3) {
                        p.sendMessage("");
                        p.sendMessage("§c§lWrong usage!");
                        p.sendMessage("§f§l/ms set <id> maxplayers <minplayers>");
                        p.sendMessage("");
                        p.sendMessage("§7§oExample: /ms set 1 maxplayers 4");
                        p.sendMessage("");
                        return;
                    }
                    SettingsManager.getArenas().set("arenas." + id + ".maxPlayers", Integer.parseInt(args[2]));
                    p.sendMessage("§6§lMax Players of Arena " + id + " set to " + args[2] + "!");
                    MusicalMobs.reloadSigns();
                    break;
                case "song":
                    if (args.length < 3) {
                        p.sendMessage("");
                        p.sendMessage("§c§lWrong usage!");
                        p.sendMessage("§f§l/ms set <id> song <song>");
                        p.sendMessage("");
                        p.sendMessage("§7§oExample: /ms set 1 song NyanCat");
                        p.sendMessage("§c§lDo not put .nbs!");
                        p.sendMessage("");
                        return;
                    }
                    SettingsManager.getArenas().set("arenas." + id + ".song", args[2]);
                    p.sendMessage("§6§lSong of Arena " + id + " set to " + args[2] + "!");
                    break;
                case "displayname":
                    if (args.length < 3) {
                        p.sendMessage("");
                        p.sendMessage("§c§lWrong usage!");
                        p.sendMessage("§f§l/ms set <id> displayname <display_name>");
                        p.sendMessage("");
                        p.sendMessage("§7§oExample: /ms set 1 displayname &3&lArena_One");
                        p.sendMessage("");
                        p.sendMessage("§7\"_\" are replaced with spaces.");
                        p.sendMessage("");
                        return;
                    }
                    SettingsManager.getArenas().set("arenas." + id + ".displayName", args[2].replaceAll("_", " ").replaceAll("&", "§"));
                    p.sendMessage("§6§lDisplay Name of Arena " + id + " set to " + args[2].replaceAll("_", " ").replaceAll("&", "§") + "§6§l!");
                    MusicalMobs.reloadSigns();
                    break;
                case "lobbycountdown":
                    if (args.length < 3) {
                        p.sendMessage("");
                        p.sendMessage("§c§lWrong usage!");
                        p.sendMessage("§f§l/ms set <id> lobbycountdown <lobbycountdown>");
                        p.sendMessage("");
                        p.sendMessage("§7§oExample: /ms set 1 lobbycountdown 10");
                        p.sendMessage("");
                        return;
                    }
                    SettingsManager.getArenas().set("arenas." + id + ".lobbyCountdown", Integer.valueOf(args[2]));
                    p.sendMessage("§6§lLobby Countdown of Arena " + id + " set to " + args[2] + "!");
                    break;
                case "sheepspawn":
                    ConfigurationSection s = SettingsManager.getArenas().createConfigurationSection("arenas." + id + ".sheepSpawn");

                    s.set("world", p.getWorld().getName());
                    s.set("x", p.getLocation().getX());
                    s.set("y", p.getLocation().getY());
                    s.set("z", p.getLocation().getZ());

                    SettingsManager.getArenas().set("arenas." + id + ".sheepSpawn", s);

                    p.sendMessage("");
                    p.sendMessage("§6§lSheep Spawn of Arena " + id + " set!");
                    p.sendMessage("§f§lInfos: - §7World: §f" + p.getLocation().getWorld().getName());
                    p.sendMessage("          - §7X: §f" + p.getLocation().getX());
                    p.sendMessage("          - §7Y: §f" + p.getLocation().getY());
                    p.sendMessage("          - §7Z: §f" + p.getLocation().getZ());
                    p.sendMessage("");
                    break;
                case "playerspawn":
                    ConfigurationSection cs = SettingsManager.getArenas().createConfigurationSection("arenas." + id + ".playerSpawn");

                    cs.set("world", p.getWorld().getName());
                    cs.set("x", p.getLocation().getX());
                    cs.set("y", p.getLocation().getY());
                    cs.set("z", p.getLocation().getZ());

                    SettingsManager.getArenas().set("arenas." + id + ".playerSpawn", cs);

                    p.sendMessage("");
                    p.sendMessage("§6§lPlayer Spawn of Arena " + id + " set!");
                    p.sendMessage("§f§lInfos: - §7World: §f" + p.getLocation().getWorld().getName());
                    p.sendMessage("          - §7X: §f" + p.getLocation().getX());
                    p.sendMessage("          - §7Y: §f" + p.getLocation().getY());
                    p.sendMessage("          - §7Z: §f" + p.getLocation().getZ());
                    p.sendMessage("");
                    break;
                default:
                    p.sendMessage("");
                    p.sendMessage("§c§lWrong usage!");
                    p.sendMessage("§f§l/ms set <id> §7minplayers <minplayers>");
                    p.sendMessage("                   §7maxplayers <maxplayers>");
                    p.sendMessage("                   §7displayname <display_name>");
                    p.sendMessage("                   §7lobbycountdown <lobbycountdown>");
                    p.sendMessage("                   §7playerspawn");
                    p.sendMessage("                   §7sheepspawn");
                    p.sendMessage("");
                    break;
            }

        }

    }
}

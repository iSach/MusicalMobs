package be.isach.musicalmobs.commands;

import be.isach.musicalmobs.arena.Arena;
import be.isach.musicalmobs.config.MessageManager;
import be.isach.musicalmobs.config.SettingsManager;
import be.isach.musicalmobs.arena.ArenaManager;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

/**
 * Created by sacha on 23/07/15.
 *
 * Used to get the infos about an arena.
 */
public class Infos extends MusicalMobsCommand {

    public Infos() {
        super("Gives infos about an arena.", "<id>", "info", "i");
    }

    @Override
    public void onCommand(Player p, String[] args) {

        if(!p.hasPermission("musicalmobs.setup")) {
            p.sendMessage(MessageManager.getMessage("No-Permission"));
            return;
        }
        if (args.length == 0) {
            p.sendMessage("§c§lYou must specify an arena number!");
            return;
        }

        int id = -1;

        try {
            id = Integer.parseInt(args[0]);
        } catch (Exception e) {
            p.sendMessage("§c§lIt's not a valid number!");
            return;
        }

        Arena a = ArenaManager.getInstance().getArena(id);

        if (a == null) {
            p.sendMessage("§c§lThere is no arena with ID " + id + "!");
            return;
        }

        p.sendMessage("");
        p.sendMessage("§f§l§oInfos about arena " + id);
        p.sendMessage("§f§lInfos: §7- Max. Players: §f" + SettingsManager.getArenas().get("arenas." + id + ".maxPlayers"));
        p.sendMessage("          §7- Min. Players: §f" + SettingsManager.getArenas().get("arenas." + id + ".minPlayers"));
        p.sendMessage("          §7- Lobby Countdown: §f" + SettingsManager.getArenas().get("arenas." + id + ".lobbyCountdown"));
        p.sendMessage("          §7- Display Name: §f" + SettingsManager.getArenas().get("arenas." + id + ".displayName"));
        p.sendMessage("          §7- Song: §f" + a.getSong());
        String sheepSpawn = "§c§oNot set yet!";
        if (SettingsManager.getArenas().get("arenas." + id + ".sheepSpawn") != null)
            sheepSpawn = getLocationFromConfig("arenas." + id + ".sheepSpawn");
        p.sendMessage("          §7- Sheep Spawn: " + sheepSpawn);
        String playerSpawn = "§c§oNot set yet!";
        if (SettingsManager.getArenas().get("arenas." + id + ".playerSpawn") != null)
            playerSpawn = getLocationFromConfig("arenas." + id + ".playerSpawn");
        p.sendMessage("          §7- Player Spawn: " + playerSpawn);
        p.sendMessage("§f§lYou can change infos with /ms set");
        p.sendMessage("");


    }

    public String getLocationFromConfig(String path) {
        String world = SettingsManager.getArenas().get(path + ".world");
        DecimalFormat formatter = new DecimalFormat("#.##");
        String x = formatter.format((double)SettingsManager.getArenas().get(path + ".x"));
        String y = formatter.format((double)SettingsManager.getArenas().get(path + ".y"));
        String z = formatter.format((double)SettingsManager.getArenas().get(path + ".z"));
        return world + ", " + x + ", " + y + ", " + z;
    }
}

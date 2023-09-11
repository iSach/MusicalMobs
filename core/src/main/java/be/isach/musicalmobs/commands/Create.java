package be.isach.musicalmobs.commands;

import be.isach.musicalmobs.config.SettingsManager;
import be.isach.musicalmobs.arena.ArenaManager;
import be.isach.musicalmobs.config.MessageManager;
import org.bukkit.entity.Player;

/**
 * Created by sacha on 21/07/15.
 * <p/>
 * Used to create an arena.
 */
public class Create extends MusicalMobsCommand {

    public void onCommand(Player p, String[] args) {

        if(!p.hasPermission("musicalmobs.setup")) {
            p.sendMessage(MessageManager.getMessage("No-Permission"));
            return;
        }
        // The Arena ID.
        int id = 1;


        // Get the right ID. Which is the first missing number from 1.
        while (SettingsManager.getArenas().contains("arenas." + id)) {
            id++;
        }

        SettingsManager.getArenas().createConfigurationSection("arenas." + id);
        SettingsManager.getArenas().set("arenas." + id + ".maxPlayers", 8);
        SettingsManager.getArenas().set("arenas." + id + ".minPlayers", 2);
        SettingsManager.getArenas().set("arenas." + id + ".lobbyCountdown", 20);
        SettingsManager.getArenas().set("arenas." + id + ".displayName", "arena" + id);
        SettingsManager.getArenas().set("arenas." + id + ".song", "NyanCat");

        p.sendMessage("");
        p.sendMessage("§f§l§omusicalmobs > §a§lArena " + id + " created!");
        p.sendMessage("§f§lInfos: §7- Max. Players: §f" + SettingsManager.getArenas().get("arenas." + id + ".maxPlayers"));
        p.sendMessage("          §7- Min. Players: §f" + SettingsManager.getArenas().get("arenas." + id + ".minPlayers"));
        p.sendMessage("          §7- Display Name: §f" + SettingsManager.getArenas().get("arenas." + id + ".displayName"));
        p.sendMessage("          §7- Lobby Countdown: §f" + SettingsManager.getArenas().get("arenas." + id + ".lobbyCountdown"));
        p.sendMessage("          §7- Song: §fNyanCat");
        p.sendMessage("          §7- Sheep Spawn: §c§oNot set yet!");
        p.sendMessage("          §7- Player Spawn: §c§oNot set yet!");
        p.sendMessage("§6§lYou can now set the spawn points!");
        p.sendMessage("§f§lYou can change infos with /ms set");
        p.sendMessage("");

        ArenaManager.getInstance().setupArenas();
    }

    public Create() {
        super("§7Create an arena", "", "create", "c");
    }
}

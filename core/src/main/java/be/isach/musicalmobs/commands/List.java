package be.isach.musicalmobs.commands;

import be.isach.musicalmobs.arena.Arena;
import be.isach.musicalmobs.config.MessageManager;
import be.isach.musicalmobs.config.SettingsManager;
import be.isach.musicalmobs.util.PlayerUtils;
import be.isach.musicalmobs.arena.ArenaManager;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

/**
 * Created by sacha on 25/07/15.
 */
public class List extends MusicalMobsCommand {

    public List() {
        super("List all the arena.", "", "list");
    }

    @Override
    public void onCommand(Player player, String[] args) {

        if(!player.hasPermission("musicalmobs.setup")) {
            player.sendMessage(MessageManager.getMessage("No-Permission"));
            return;
        }

        player.sendMessage("");
        player.sendMessage("§f§l---------*+ §oMusical Sheeps Arenas §f§l+*---------");
        player.sendMessage("§7Hover them for more infos!");
        player.sendMessage("");
        if (ArenaManager.getInstance().getArenas().size() == 0) {
            player.sendMessage("  §c§l§oThere are no arenas!");
            player.sendMessage("");
            return;
        }
        for (Arena a : ArenaManager.getInstance().getArenas()) {
            int id = a.getID();
            String sheepSpawn = "§c§oNot set yet!";
            if (SettingsManager.getArenas().get("arenas." + id + ".sheepSpawn") != null)
                sheepSpawn = getLocationFromConfig("arenas." + id + ".sheepSpawn");
            String playerSpawn = "§c§oNot set yet!";
            if (SettingsManager.getArenas().get("arenas." + id + ".playerSpawn") != null)
                playerSpawn = getLocationFromConfig("arenas." + id + ".playerSpawn");
            String hoverText = "\\n§f§l§oInfos about arena " + id + "\\n" +
                    "§f§lInfos: §7- Max. Players: §f" + SettingsManager.getArenas().get("arenas." + id + ".maxPlayers") + "\\n" +
                    "          §7- Min. Players: §f" + SettingsManager.getArenas().get("arenas." + id + ".minPlayers") + "\\n" +
                    "          §7- Lobby Countdown: §f" + SettingsManager.getArenas().get("arenas." + id + ".lobbyCountdown") + "\\n" +
                    "          §7- Display Name: §f" + SettingsManager.getArenas().get("arenas." + id + ".displayName") + "§r\\n" +
                    "          §7- Song: §f" + a.getSong() + "\\n" +
                    "          §7- Sheep Spawn: §f" + sheepSpawn + "\\n" +
                    "          §7- Player Spawn: §f" + playerSpawn + "\\n" +
                    "          §7- Current players: §f" + a.getCurrentPlayers() + "\\n" +
                    "        §7- Current state: §f" + a.getState() + "\\n" +
                    "§f§lYou can change infos with /ms set\\n§k";
            PlayerUtils.sendRawMessage(player, "§6§l" + a.getID() + ")" + " §a§l" + a.getDisplayName() + "§f §7(" + a.getCurrentPlayers() + "/" + a.getMaxPlayers() + ") " + a.getState(), hoverText);
        }
        player.sendMessage("");
    }

    public String getLocationFromConfig(String path) {
        String world = SettingsManager.getArenas().get(path + ".world");
        DecimalFormat formatter = new DecimalFormat("#.##");
        String x = formatter.format((double) SettingsManager.getArenas().get(path + ".x"));
        String y = formatter.format((double) SettingsManager.getArenas().get(path + ".y"));
        String z = formatter.format((double) SettingsManager.getArenas().get(path + ".z"));
        return world + ", " + x + ", " + y + ", " + z;
    }
}

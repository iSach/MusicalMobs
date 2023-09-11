package be.isach.musicalmobs.commands;

import be.isach.musicalmobs.MusicalMobs;
import be.isach.musicalmobs.arena.Arena;
import be.isach.musicalmobs.arena.ArenaManager;
import be.isach.musicalmobs.config.MessageManager;
import be.isach.musicalmobs.config.SettingsManager;
import be.isach.musicalmobs.listeners.SignsManager;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * Created by sacha on 23/07/15.
 *
 * Delete command, used to delete an arena.
 */
public class Delete extends MusicalMobsCommand {

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

        if (a.getState() != Arena.ArenaState.WAITING) {
            p.sendMessage("§c§lArena is ingame!");
            return;
        }

        for(Sign s : SignsManager.getSigns(a)) {
            MusicalMobs.instance.getServer().getPluginManager().callEvent(new BlockBreakEvent(s.getBlock(), p));
            s.getBlock().breakNaturally();
        }

        for (Player players : a.getPlayers()) {
            a.removePlayer(players, false);
        }

        SettingsManager.getArenas().set("arenas." + id + "", null);

        ArenaManager.getInstance().setupArenas();

        p.sendMessage("Deleted arena " + id + "!");
    }

    public Delete() {
        super("Delete an arena.", "<id>", "delete", "d");
    }
}

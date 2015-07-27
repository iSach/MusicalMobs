package me.isach.musicalsheeps.commands;

import me.isach.musicalsheeps.arena.Arena;
import me.isach.musicalsheeps.arena.ArenaManager;
import me.isach.musicalsheeps.config.MessageManager;
import me.isach.musicalsheeps.config.SettingsManager;
import me.isach.musicalsheeps.core.Core;
import me.isach.musicalsheeps.listeners.SignsManager;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * Created by sacha on 23/07/15.
 *
 * Delete command, used to delete an arena.
 */
public class Delete extends MusicalSheepsCommand {

    public void onCommand(Player p, String[] args) {

        if(!p.hasPermission("musicalsheeps.setup")) {
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
            Core.getPlugin().getServer().getPluginManager().callEvent(new BlockBreakEvent(s.getBlock(), p));
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

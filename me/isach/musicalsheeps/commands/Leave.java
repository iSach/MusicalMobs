package me.isach.musicalsheeps.commands;

import me.isach.musicalsheeps.arena.ArenaManager;
import org.bukkit.entity.Player;

/**
 * Created by sacha on 21/07/15.
 *
 * Used to leave a game.
 */
public class Leave extends MusicalSheepsCommand {

    public void onCommand(Player p, String[] args) {
        if (ArenaManager.getInstance().getArena(p) == null) {
            p.sendMessage("§c§l§oYou're not in a game!");
            return;
        }

        ArenaManager.getInstance().getArena(p).removePlayer(p, true);
    }

    public Leave() {
        super("Leave an arena.", "", "leave", "l");
    }
}

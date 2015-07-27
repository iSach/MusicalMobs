package me.isach.musicalsheeps.commands;

import me.isach.musicalsheeps.arena.Arena;
import me.isach.musicalsheeps.arena.ArenaManager;
import me.isach.musicalsheeps.config.MessageManager;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;

/**
 * Created by sacha on 23/07/15.
 *
 * Used to reload arenas.
 */
public class Reload extends MusicalSheepsCommand {


    public void onCommand(Player p, String[] args) {

        if(!p.hasPermission("musicalsheeps.setup")) {
            p.sendMessage(MessageManager.getMessage("No-Permission"));
            return;
        }
        for (Arena a : ArenaManager.getInstance().getArenas()) {
            try {
                a.round.sp.setPlaying(false);
                for (Sheep s : a.round.sheeps)
                    s.remove();
                a.broadcast("§f§l§oMusical Sheeps > §c§lThe game is stopped because a reload happened!");
                a.stop();
            } catch (Exception exc) {
            }
        }
        ArenaManager.getInstance().setupArenas();
        p.sendMessage("§f§l§oMusical Sheeps > §a§lArenas reloaded!");
    }

    public Reload() {
        super("Reload the arenas.", "", "reload", "r");
    }
}

package be.isach.musicalmobs.commands;

import be.isach.musicalmobs.arena.Arena;
import be.isach.musicalmobs.arena.ArenaManager;
import be.isach.musicalmobs.config.MessageManager;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;

/**
 * Created by sacha on 23/07/15.
 *
 * Used to reload arenas.
 */
public class Reload extends MusicalMobsCommand {


    public void onCommand(Player p, String[] args) {

        if(!p.hasPermission("musicalmobs.setup")) {
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

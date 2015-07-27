package me.isach.musicalsheeps.commands;

import me.isach.musicalsheeps.arena.Arena;
import me.isach.musicalsheeps.arena.ArenaManager;
import org.bukkit.entity.Player;

/**
 * Created by sacha on 23/07/15.
 *
 * Used to join an arena.
 */
public class Join extends MusicalSheepsCommand {

    public void onCommand(Player p, String[] args) {

        if (ArenaManager.getInstance().getArena(p) != null) {
            p.sendMessage("§c§lYou are already in an arena!");
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
            p.sendMessage("§c§l" + args[0] + " is not a number!");
        }

        Arena a = ArenaManager.getInstance().getArena(id);

        if (a == null) {
            p.sendMessage("§c§lThat arena doesn't exist!");
            return;
        }

        if (a.getState() == Arena.ArenaState.DISABLED || a.getState() == Arena.ArenaState.STARTED) {
            p.sendMessage("That arena is " + a.getState().toString().toLowerCase() + "!");
            return;
        }

        a.addPlayer(p);
    }

    public Join() {
        super("Join an arena.", "<id>", "join", "j");
    }
}
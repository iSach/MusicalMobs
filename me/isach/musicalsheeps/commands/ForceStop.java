package me.isach.musicalsheeps.commands;

import me.isach.musicalsheeps.arena.Arena;
import me.isach.musicalsheeps.arena.ArenaManager;
import me.isach.musicalsheeps.config.MessageManager;
import org.bukkit.entity.Player;

import java.awt.*;

/**
 * Created by sacha on 23/07/15.
 *
 * Used to force stop an arena.
 */
public class ForceStop extends MusicalSheepsCommand {

    public void onCommand(Player p, String[] args) {

        if(!p.hasPermission("musicalsheeps.setup")) {
            p.sendMessage(MessageManager.getMessage("No-Permission"));
            return;
        }
        if (args.length == 0) {
            p.sendMessage("§c§lYou must specify an arena ID.");
            return;
        }

        int id = -1;

        try {
            id = Integer.parseInt(args[0]);
        } catch (Exception e) {
            p.sendMessage("§c§l" + args[0] + " is not a valid number!");
            return;
        }

        Arena a = ArenaManager.getInstance().getArena(id);

        if (a == null) {
            p.sendMessage("§c§lThere is no arena with ID " + id + "!");
            return;
        }

        if (a.getState() != Arena.ArenaState.STARTED) {
            p.sendMessage("§c§lArena " + id + " is not running!");
            return;
        }

        a.stop();
        p.sendMessage("§c§lForce stopped arena " + a.getID() + "!");
    }

    public ForceStop() {
        super("Force start an arena.", "<id>", "fstop", "stop");
    }
}

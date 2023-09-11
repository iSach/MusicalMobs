package be.isach.musicalmobs.commands;

import be.isach.musicalmobs.arena.Arena;
import be.isach.musicalmobs.arena.ArenaManager;
import be.isach.musicalmobs.config.MessageManager;
import org.bukkit.entity.Player;

/**
 * Created by sacha on 23/07/15.
 *
 * Used to force stop an arena.
 */
public class ForceStop extends MusicalMobsCommand {

    public void onCommand(Player p, String[] args) {

        if(!p.hasPermission("musicalmobs.setup")) {
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

package be.isach.musicalmobs.commands;

import be.isach.musicalmobs.arena.Arena;
import be.isach.musicalmobs.arena.ArenaManager;
import be.isach.musicalmobs.config.MessageManager;
import org.bukkit.entity.Player;

/**
 * Created by sacha on 21/07/15.
 *
 * Used to force start an arena.
 */
public class ForceStart extends MusicalMobsCommand {

    public void onCommand(Player p, String[] args) {

        if(!p.hasPermission("musicalmobs.setup")) {
            p.sendMessage(MessageManager.getMessage("No-Permission"));
            return;
        }
        if (args.length == 0) {
            p.sendMessage("");
            p.sendMessage("§c§lWrong usage!");
            p.sendMessage("§f§l/ms fstart <ID>");
            p.sendMessage("");
            return;
        }

        int id = -1;

        try {
            id = Integer.parseInt(args[0]);
        } catch (Exception e) {
            p.sendMessage("§c§l§o" + args[0] + "is not a valid number!");
            return;
        }

        Arena a = ArenaManager.getInstance().getArena(id);

        if (a == null) {
            p.sendMessage("§c§l§oThere is no arena with ID " + id + "!");
            return;
        }

        if (a.getState() == Arena.ArenaState.STARTED) {
            p.sendMessage("§c§l§oArena " + id + " has already started!");
            return;
        }

        a.start();

    }

    public ForceStart() {
        super("Force start an arena.", "<id>", "fstart", "start", "forcestart");
    }

}

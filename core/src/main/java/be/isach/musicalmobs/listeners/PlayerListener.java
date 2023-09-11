package be.isach.musicalmobs.listeners;

import be.isach.musicalmobs.arena.ArenaManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by sacha on 21/07/15.
 */
public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (ArenaManager.getInstance().getArena(p) != null) {
            ArenaManager.getInstance().getArena(p).removePlayer(p, true);
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        Player p = (Player)e.getEntity();
        if (ArenaManager.getInstance().getArena(p) != null) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if(e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (ArenaManager.getInstance().getArena(p) != null) {
                e.setCancelled(true);
            }
        }
    }
}

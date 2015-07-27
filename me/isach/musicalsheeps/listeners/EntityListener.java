package me.isach.musicalsheeps.listeners;

import me.isach.musicalsheeps.arena.Arena;
import me.isach.musicalsheeps.arena.ArenaManager;
import me.isach.musicalsheeps.core.Core;
import net.minecraft.server.v1_8_R3.EntitySheep;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftSheep;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;

/**
 * Created by sacha on 21/07/15.
 */
public class EntityListener implements Listener {

    ArrayList<Sheep> sheepsCooldown = new ArrayList<>();

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
        Player p = e.getPlayer();
        if (ArenaManager.getInstance().getArena(p) == null) return;
        if (e.getRightClicked().getType() != EntityType.SHEEP) return;
        final Sheep SHEEP = (Sheep) e.getRightClicked();
        if (sheepsCooldown.contains(SHEEP)) return;
        Arena a = ArenaManager.getInstance().getArena(p);
        if (a.getState() == Arena.ArenaState.STARTED) {
            e.setCancelled(true);
            if (!a.round.sp.isPlaying()) {
                if (e.getRightClicked().getPassenger() == null) {
                    e.getRightClicked().setPassenger(p);
                    return;
                }
            }
            e.getRightClicked().setVelocity(p.getLocation().getDirection().normalize().multiply(0.5d).add(new Vector(0, 1, 0)));
            SHEEP.getWorld().spigot().playEffect(SHEEP.getLocation(), Effect.CLOUD, 0, 0, .25f, 3f, .25f, 0f, 20, 32);
            sheepsCooldown.add(SHEEP);
            Bukkit.getScheduler().runTaskLater(Core.getPlugin(), new Runnable() {
                @Override
                public void run() {
                    sheepsCooldown.remove(SHEEP);
                }
            }, 7);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity().getType() == EntityType.SHEEP) {
            for (Arena a : ArenaManager.getInstance().getArenas()) {
                if ((a.getState() == Arena.ArenaState.STARTED)) {
                    if (a.round.sheeps.contains((Sheep) event.getEntity())) {
                        event.setCancelled(true);
                    }
                }
            }

        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager().getType() != EntityType.PLAYER) return;
        Player p = (Player) e.getDamager();
        if (ArenaManager.getInstance().getArena(p) == null) return;
        if (e.getEntity().getType() != EntityType.SHEEP) return;
        final Sheep SHEEP = (Sheep) e.getEntity();
        if (sheepsCooldown.contains(SHEEP)) return;
        Arena a = ArenaManager.getInstance().getArena(p);
        if (a.getState() == Arena.ArenaState.STARTED) {
            e.setCancelled(true);

            SHEEP.setVelocity(p.getLocation().getDirection().normalize().multiply(0.5d).add(new Vector(0, 1, 0)));
            SHEEP.getWorld().spigot().playEffect(SHEEP.getLocation(), Effect.CLOUD, 0, 0, .25f, 3f, .25f, 0f, 20, 32);
            sheepsCooldown.add(SHEEP);
            Bukkit.getScheduler().runTaskLater(Core.getPlugin(), new Runnable() {
                @Override
                public void run() {
                    sheepsCooldown.remove(SHEEP);
                }
            }, 7);
        }
    }

}

package be.isach.musicalmobs.arena;

import be.isach.musicalmobs.MusicalMobs;
import be.isach.musicalmobs.config.SettingsManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * Created by sacha on 21/07/15.
 */
public class ArenaManager {

    // ArenaManager instance.
    private static ArenaManager instance = new ArenaManager();

    // Arenas list.
    private ArrayList<Arena> arenas = new ArrayList<>();

    /**
     * @return
     */
    public static ArenaManager getInstance() {
        return instance;
    }

    /**
     * Set up the arenas.
     */
    public void setupArenas() {
        if (SettingsManager.getArenas().<ConfigurationSection>get("arenas") == null)
            SettingsManager.getArenas().createConfigurationSection("arenas");

        arenas.clear();

        for (String key : SettingsManager.getArenas().<ConfigurationSection>get("arenas").getKeys(false)) {
            MusicalMobs.instance.getServer().getConsoleSender().sendMessage("§f§l§oMusical Sheeps > §c§lLoading Arena n°" + key + "...");
            arenas.add(new Arena(Integer.parseInt(key)));
        }
    }

    /**
     * Returns the arena with a given ID.
     *
     * @param id
     * @return the arena of the given ID.
     */
    public Arena getArena(int id) {
        for (Arena arena : arenas)
            if (arena.getID() == id) return arena;
        return null;
    }

    /**
     * Returns the arena of a given player.
     *
     * @param player
     * @return the arena of the given player.
     */
    public Arena getArena(Player player) {
        for (Arena arena : arenas)
            if (arena.containsPlayer(player)) return arena;
        return null;
    }

    /**
     * Gets all the arenas.
     *
     * @return a list of all the arenas.
     */
    public ArrayList<Arena> getArenas() {
        return arenas;
    }

    /**
     * Checks if a player is in an arena.
     *
     * @param p
     * @return {@code true} if the given player is in an arena, {@code false} otherwise.
     */
    public boolean isInAnArena(Player p) {
        for (Arena a : getArenas()) {
            if (a.containsPlayer(p))
                return true;
        }
        return false;
    }

}

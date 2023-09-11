package be.isach.musicalmobs.listeners;

import be.isach.musicalmobs.MusicalMobs;
import be.isach.musicalmobs.arena.Arena;
import be.isach.musicalmobs.config.MessageManager;
import be.isach.musicalmobs.config.SettingsManager;
import be.isach.musicalmobs.util.MathsUtils;
import be.isach.musicalmobs.arena.ArenaManager;
import be.isach.musicalmobs.util.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.*;

/**
 * Created by sacha on 21/07/15.
 */
public class SignsManager implements Listener {

    // Signs hashmap containg as value: the sign and as key: the corresponding arena ID.
    private static HashMap<Sign, Integer> signs = new HashMap<>();

    // signsFileIDs hashmap contains as value the ID of the sign in the data file, and as key the corresponding arena ID.
    private static HashMap<Integer, Integer> signsFileIDs = new HashMap<>();

    /**
     * The SignsManager constructor. It loads the signs.
     */
    public SignsManager() {

        // If the data file is empty and doesn't contain the main section signs, we create it.
        if (!SettingsManager.getSigns().contains("signs")) {
            SettingsManager.getSigns().createConfigurationSection("signs");
        }

        // Load all the signs in the data file.
        for (String str : SettingsManager.getSigns().<ConfigurationSection>get("signs").getKeys(false)) {
            MusicalMobs.instance.getServer().getConsoleSender().sendMessage("§f§l§oMusical Sheeps > §c§lLoading Sign n°" + str + "...");
            ConfigurationSection section = SettingsManager.getSigns().get("signs." + str);

            String world = SettingsManager.getSigns().get("signs." + str + ".location.world");
            int x = Integer.parseInt(String.valueOf(SettingsManager.getSigns().get("signs." + str + ".location.x")));
            int y = Integer.parseInt(String.valueOf(SettingsManager.getSigns().get("signs." + str + ".location.y")));
            int z = Integer.parseInt(String.valueOf(SettingsManager.getSigns().get("signs." + str + ".location.z")));
            Location loc = new Location(Bukkit.getWorld(world), x, y, z);
            Sign s = (Sign) loc.getBlock().getState();

            signs.put(s, section.getInt("arenaNumber"));
            signsFileIDs.put(Integer.valueOf(str), section.getInt("arenaNumber"));
        }
    }

    /**
     * Gets all the signs of an arena.
     *
     * @param a
     * @return all the signs of an arena.
     */
    public static ArrayList<Sign> getSigns(Arena a) {
        ArrayList<Sign> s = new ArrayList<>();

        for (Sign sign : signs.keySet()) {
            if (signs.get(sign) == a.getID()) s.add(sign);
        }

        return s;
    }

    /**
     * Called when a player sets a Musical Sheeps sign.
     *
     * @param e
     */
    @EventHandler
    public void onSignChange(SignChangeEvent e) {
        if (e.getLine(0).equalsIgnoreCase("[musicalmobs]")) {

            // Arena ID.
            int id;

            // Checks if the id is a number.
            try {
                id = getNumber(e.getLine(1));
            } catch (Exception ex) {
                e.getPlayer().sendMessage(MessageManager.getMessage("Not-Valid-Arena-ID"));
                if (e.getPlayer().getGameMode() == GameMode.CREATIVE)
                    e.getBlock().setType(Material.AIR);
                else
                    e.getBlock().breakNaturally();
                return;
            }

            // Loads the arena from the gotten ID.
            Arena a = ArenaManager.getInstance().getArena(id);

            // Happens if the arena doesn't exist.
            if (a == null) {
                e.getPlayer().sendMessage(MessageManager.getMessage("Not-Valid-Arena"));
                if (e.getPlayer().getGameMode() == GameMode.CREATIVE)
                    e.getBlock().setType(Material.AIR);
                else
                    e.getBlock().breakNaturally();
                return;
            }

            // The sign ID in the file.
            int signPathNumber = 1;

            // Get the right ID. Which is the first missing number from 1.
            while (SettingsManager.getSigns().contains("signs." + signPathNumber)) {
                signPathNumber++;
            }

            ConfigurationSection locSection = SettingsManager.getSigns().createConfigurationSection("signs." + signPathNumber + ".location");

            locSection.set("world", e.getBlock().getWorld().getName());
            locSection.set("x", e.getBlock().getLocation().getBlockX());
            locSection.set("y", e.getBlock().getLocation().getBlockY());
            locSection.set("z", e.getBlock().getLocation().getBlockZ());

            SettingsManager.getSigns().set("signs." + signPathNumber + ".arenaNumber", a.getID());

            signs.put((Sign) e.getBlock().getState(), a.getID());
            signsFileIDs.put(signPathNumber, a.getID());

            /* Now the sign is registered in signs map, we can simply call reloadSigns() to update its status.
            (After 2 ticks to wait that the sign is updated.*/
            Bukkit.getScheduler().runTaskLater(MusicalMobs.instance, new Runnable() {
                @Override
                public void run() {
                    MusicalMobs.reloadSigns();
                }
            }, 2);
        }
    }

    /**
     * Gets the number in a {@link String}.
     * <p/>
     * Example: lde2xea0Dean1xea5cdan will return 2015.
     *
     * @param s
     * @return the number found in the {@link String}.
     */
    public int getNumber(String s) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (MathsUtils.isInt(c)) {
                sb.append(String.valueOf(c));
            }
        }
        return Integer.valueOf(sb.toString());
    }

    /**
     * Called when a player rights click on a Musical Sheeps sign.
     *
     * @param e
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {

        if (e.getClickedBlock() == null || e.getClickedBlock().getState() == null) return;

        if (e.getClickedBlock().getState() instanceof Sign) {
            Sign s = (Sign) e.getClickedBlock().getState();
            if (s.getLine(0).equalsIgnoreCase("§9§lmusicalmobs")) {
                try {
                    ArenaManager.getInstance().getArena(signs.get(s)).addPlayer(e.getPlayer());
                } catch (Exception exc) {
                    MusicalMobs.instance.getServer().getConsoleSender().sendMessage("§c§l------------------------------------ ERROR -------------------------------------");
                    MusicalMobs.instance.getServer().getConsoleSender().sendMessage("");
                    exc.printStackTrace();
                    MusicalMobs.instance.getServer().getConsoleSender().sendMessage("");
                    MusicalMobs.instance.getServer().getConsoleSender().sendMessage("§c§l-----------------------------------------------------------------------------------------");
                    e.getPlayer().sendMessage(MessageManager.getMessage("Error-Happened"));
                }
            }
        }
    }


    @EventHandler
    public void onRemoveSign(BlockBreakEvent event) {
        if (event.getBlock().getType() == XMaterial.OAK_SIGN.parseMaterial()
                || event.getBlock().getType() == XMaterial.OAK_WALL_SIGN.parseMaterial()) {
            Sign sign = (Sign) event.getBlock().getState();
            if (sign != null) {
                if (signs.containsKey(sign)) {
                    int i = signs.get(sign);
                    signs.remove(sign);
                    int k = -1;
                    for (int j : getKeysByValue(signsFileIDs, i)) {
                        int bX = event.getBlock().getLocation().getBlockX();
                        int bY = event.getBlock().getLocation().getBlockY();
                        int bZ = event.getBlock().getLocation().getBlockZ();
                        int x = (int) SettingsManager.getSigns().get("signs." + j + ".location.x");
                        int y = (int) SettingsManager.getSigns().get("signs." + j + ".location.y");
                        int z = (int) SettingsManager.getSigns().get("signs." + j + ".location.z");
                        if (x == bX && y == bY && z == bZ) {
                            k = j;
                        }
                    }
                    SettingsManager.getSigns().set("signs." + k, null);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onBlockPhysics(BlockPhysicsEvent event) {
        check(event.getBlock());
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent e) {
        check(e.getBlock());
    }

    @EventHandler
    public void onBlockPistonExtend(BlockPistonExtendEvent e) {
        for (Block b : e.getBlocks()) {
            check(b);
        }
        check(e.getBlock());
    }

    @EventHandler
    public void onBlockPistonRetract(BlockPistonRetractEvent e) {
        for (Block b : e.getBlocks()) {
            check(b);
        }
        check(e.getBlock());
    }


    @EventHandler
    public void onLeavesDecay(LeavesDecayEvent e) {
        check(e.getBlock());
    }

    /**
     * Check if a block has around it a musical sheeps sign.
     *
     * @param b
     * @return
     */
    public boolean check(Block b) {
        for (BlockFace f : BlockFace.values()) {
            if (b.getRelative(f).getType() == XMaterial.OAK_SIGN.parseMaterial()
                    || b.getRelative(f).getType() == XMaterial.OAK_WALL_SIGN.parseMaterial()) {
                Sign sign = (Sign) b.getRelative(f).getState();
                if (sign != null) {
                    if (signs.containsKey(sign)) {
                        int i = signs.get(sign);
                        signs.remove(sign);
                        int k = -1;
                        for (int j : getKeysByValue(signsFileIDs, i)) {
                            int bX = b.getRelative(f).getLocation().getBlockX();
                            int bY = b.getRelative(f).getLocation().getBlockY();
                            int bZ = b.getRelative(f).getLocation().getBlockZ();
                            int x = (int) SettingsManager.getSigns().get("signs." + j + ".location.x");
                            int y = (int) SettingsManager.getSigns().get("signs." + j + ".location.y");
                            int z = (int) SettingsManager.getSigns().get("signs." + j + ".location.z");
                            if (x == bX && y == bY && z == bZ) {
                                k = j;
                            }
                        }
                        SettingsManager.getSigns().set("signs." + k, null);


                    }
                }
                return true;
            }
        }
        return false;
    }

    public static <T, E> Set<T> getKeysByValue(Map<T, E> map, E value) {
        Set<T> keys = new HashSet<T>();
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                keys.add(entry.getKey());
            }
        }
        return keys;
    }
}

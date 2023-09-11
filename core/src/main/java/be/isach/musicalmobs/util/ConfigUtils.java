package be.isach.musicalmobs.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Created by sacha on 21/07/15.
 */
public class ConfigUtils {

    /**
     * Gets a location from a config path.
     * @param s
     * @param pitchAndYaw
     * @return the location from the config path.
     */
    public static Location getLocationFromConfig(ConfigurationSection s, boolean pitchAndYaw) {

        try {
            Location loc = new Location(
                    Bukkit.getServer().getWorld(s.getString("world")),
                    s.getDouble("x"),
                    s.getDouble("y"),
                    s.getDouble("z")
            );

            if (pitchAndYaw) {
                loc.setPitch((float) s.getDouble("pitch"));
                loc.setYaw((float) s.getDouble("yaw"));
            }

            return loc;
        } catch (Exception e) {
            // If the path is invalid.
            return null;
        }

    }
}

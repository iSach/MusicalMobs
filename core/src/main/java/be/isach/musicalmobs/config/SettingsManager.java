package be.isach.musicalmobs.config;

import be.isach.musicalmobs.MusicalMobs;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * Created by sacha on 21/07/15.
 */
public class SettingsManager {

    // Arena data file.
    private static SettingsManager arenas = new SettingsManager("arenas");
    // Signs data file.
    private static SettingsManager signs = new SettingsManager("signs");
    // Translation config file.
    private static SettingsManager messages = new SettingsManager("messages");

    private File file;
    private FileConfiguration config;

    /**
     * Creates a new file and defines config and file.
     *
     * @param fileName
     */
    private SettingsManager(String fileName) {
        if (!MusicalMobs.instance.getDataFolder().exists()) MusicalMobs.instance.getDataFolder().mkdir();

        file = new File(MusicalMobs.instance.getDataFolder(), fileName + ".yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * Gets the arena SettingsManager.
     *
     * @return the arena SettingsManager.
     */
    public static SettingsManager getArenas() {
        return arenas;
    }

    /**
     * Gets the signs SettingsManager.
     *
     * @return the signs SettingsManager.
     */
    public static SettingsManager getSigns() {
        return signs;
    }

    /**
     * Gets the messages SettingsManager.
     *
     * @return the messages SettingsManager.
     */
    public static SettingsManager getMessages() {
        return messages;
    }

    /**
     * Sets the value of a given path.
     *
     * @param path
     * @param value
     */
    public void set(String path, Object value) {
        config.set(path, value);
        try {
            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets a value if the config doesn't contain the path.
     * @param path The config path.
     * @param value The value for this path.
     */
    public void addDefault(String path, Object value) {
        if (!config.contains(path))
            set(path, value);
    }

    /**
     * Create and get a configuration section for a given path.
     *
     * @param path
     * @return the configuration section created for the given path.
     */
    public ConfigurationSection createConfigurationSection(String path) {
        ConfigurationSection cs = config.createSection(path);
        try {
            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cs;
    }


    @SuppressWarnings("unchecked")
    public <T> T get(String path) {
        return (T) config.get(path);
    }

    public int getInt(String path) {
        return config.getInt(path);
    }

    /**
     * @param path
     * @return {@code true} if the config contains the path, {@code false} otherwise.
     */
    public boolean contains(String path) {
        return config.contains(path);
    }

}

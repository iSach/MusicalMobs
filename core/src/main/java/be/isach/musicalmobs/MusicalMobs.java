package be.isach.musicalmobs;

import be.isach.musicalmobs.arena.Arena;
import be.isach.musicalmobs.config.MessageManager;
import be.isach.musicalmobs.arena.ArenaManager;
import be.isach.musicalmobs.commands.CommandsManager;
import be.isach.musicalmobs.listeners.BlockListener;
import be.isach.musicalmobs.listeners.EntityListener;
import be.isach.musicalmobs.listeners.PlayerListener;
import be.isach.musicalmobs.listeners.SignsManager;
import be.isach.musicalmobs.nms.ServerVersion;
import be.isach.musicalmobs.nms.VersionManager;
import be.isach.musicalmobs.util.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Created by sacha on 21/07/15.
 */
public class MusicalMobs extends JavaPlugin {

    public static MusicalMobs instance;
    private ServerVersion serverVersion;
    private VersionManager versionManager;
    private Metrics metrics;

    /**
     * Called on plugin start.
     */
    @Override
    public void onEnable() {
        // Sets the config messages.
        new MessageManager();

        // Register Sign Events and load Signs...
        Bukkit.getServer().getPluginManager().registerEvents(new SignsManager(), this);

        // A line to seperate the Loading messages of the signs and of the arenas.
        getServer().getConsoleSender().sendMessage("§7§l---------------------------------");

        // Load arenas...
        ArenaManager.getInstance().setupArenas();

        // Register the listeners.
        Bukkit.getServer().getPluginManager().registerEvents(new EntityListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new BlockListener(), this);

        // Register the command.
        getCommand("musicalmobs").setExecutor(new CommandsManager());

        reloadSigns();

        // Load the song from jar src to plugin directory.
        loadSongs();

        this.metrics = new Metrics(this, 5435);
    }

    /**
     * Called on plugin loading.
     */
    @Override
    public void onLoad() {
        if (instance != null) {
            getLogger().warning("Please do not use /reload or plugin that reload other plugins.");
            return;
        }
        instance = this;
        initModule();
    }

    /**
     * Loads the song from the src/songs path to the plugin directory.
     */
    private void loadSongs() {
        File folder = new File(getDataFolder().getPath() + "/songs/");
        if ((!folder.exists()) || (folder.listFiles().length <= 0)) {
            saveResource("songs/Animals.nbs", true);
            saveResource("songs/DJGotUsFallinInLove.nbs", true);
            saveResource("songs/Fireflies.nbs", true);
            saveResource("songs/GangnamStyle.nbs", true);
            saveResource("songs/NyanCat.nbs", true);
            saveResource("songs/PokemonBattleTheme.nbs", true);
            saveResource("songs/Sweden.nbs", true);
            saveResource("songs/TetrisATheme.nbs", true);
            saveResource("songs/WhatisLove.nbs", true);
        }
    }

    // Update the status of all the signs.
    public static void reloadSigns() {
        for (Arena a : ArenaManager.getInstance().getArenas()) {
            for (Sign s : SignsManager.getSigns(a)) {
                s.setLine(0, "§9§lmusicalmobs");
                s.setLine(1, a.getDisplayName());
                s.setLine(2, "§8" + a.getCurrentPlayers() + "/" + a.getMaxPlayers());
                s.setLine(3, a.getState().toString());
                s.update();
            }
        }
    }

    /**
     * Called when plugin is disabling.
     */
    @Override
    public void onDisable() {

        // Kick the player from their arena.
        for (Player p : Bukkit.getOnlinePlayers()) {
            Arena a = ArenaManager.getInstance().getArena(p);
            if (a != null) {
                a.removePlayer(p, false);
            }
        }
        // Stop the running arenas.
        for (Arena a : ArenaManager.getInstance().getArenas()) {
            try {
                a.round.sp.setPlaying(false);
                for (Sheep s : a.round.sheeps)
                    s.remove();
                a.stop();
            } catch (Exception exc) {
            }
        }
    }

    private void initServerVersion() {
        String mcVersion = "1.8.0";

        try {
            mcVersion = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }

        if (mcVersion.startsWith("v")) {
            try {
                serverVersion = ServerVersion.valueOf(mcVersion);
            } catch (Exception exc) {
                getServer().getConsoleSender().sendMessage("This NMS version isn't supported. (" + mcVersion + ")! Trying with latest NMS module...");
                //Bukkit.getPluginManager().disablePlugin(this);
                serverVersion = ServerVersion.v1_20_R1;
            }
        } else serverVersion = ServerVersion.v1_8_R3;
    }

    private void initModule() {
        initServerVersion();

        System.out.println("[MusicalMobs] Initializing module " + serverVersion);
        versionManager = new VersionManager(serverVersion);
        try {
            versionManager.load();
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            System.out.println("[MusicalMobs] No module found for " + serverVersion + "! MM Disabling...");
        }
        versionManager.getModule().enable();
        System.out.println("[MusicalMobs] Module initialized");
    }

    public VersionManager getVersionManager() {
        return versionManager;
    }
}

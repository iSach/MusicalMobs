package me.isach.musicalsheeps.core;

import me.isach.musicalsheeps.arena.Arena;
import me.isach.musicalsheeps.arena.ArenaManager;
import me.isach.musicalsheeps.commands.CommandsManager;
import me.isach.musicalsheeps.config.MessageManager;
import me.isach.musicalsheeps.listeners.BlockListener;
import me.isach.musicalsheeps.listeners.EntityListener;
import me.isach.musicalsheeps.listeners.PlayerListener;
import me.isach.musicalsheeps.listeners.SignsManager;
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
public class Core extends JavaPlugin {

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
        getCommand("musicalsheeps").setExecutor(new CommandsManager());

        reloadSigns();

        // Load the song from jar src to plugin directory.
        loadSongs();
    }

    /**
     * Called on plugin loading.
     */
    @Override
    public void onLoad() {
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
            saveResource("songs/HoldTheLine.nbs", true);
            saveResource("songs/NothingElseMatters.nbs", true);
            saveResource("songs/NyanCat.nbs", true);
            saveResource("songs/PushingOnwards.nbs", true);
            saveResource("songs/Remedy.nbs", true);
            saveResource("songs/Sweden.nbs", true);
            saveResource("songs/TetrisATheme.nbs", true);
            saveResource("songs/TheEdgeofGlory.nbs", true);
            saveResource("songs/WhatisLove.nbs", true);
        }
    }

    // Update the status of all the signs.
    public static void reloadSigns() {
        for (Arena a : ArenaManager.getInstance().getArenas()) {
            for (Sign s : SignsManager.getSigns(a)) {
                s.setLine(0, "§9§lMusicalSheeps");
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

    /**
     * Gets the Plugin object.
     *
     * @return the Plugin object of Musical Sheeps.
     */
    public static Plugin getPlugin() {
        return Bukkit.getServer().getPluginManager().getPlugin("MusicalSheeps");
    }

}

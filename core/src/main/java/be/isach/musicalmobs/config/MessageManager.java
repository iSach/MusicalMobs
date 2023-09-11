package be.isach.musicalmobs.config;

import net.md_5.bungee.api.ChatColor;

/**
 * Created by sacha on 24/07/15.
 */
public class MessageManager {

    private static SettingsManager settingsManager;

    /**
     * Set up the messages in the config.
     */
    public MessageManager() {
        this.settingsManager = SettingsManager.getMessages();
        loadMessages();
    }

    /**
     * Set up the messages in the config.
     */
    private void loadMessages() {
        addMessage("Spawn-Points-Not-Set", "&c&l&oSpawn points not yet set. Please contact an admin!");
        addMessage("Song-File-Doesnt-Exist", "&c&l&oThe file %song%.nbs doesn't exist in the folder /plugins/musicalmobs/songs/");
        addMessage("Joined-Arena", "&f&l&omusicalmobs > &a&l%player% has joined the game! &7(%current%/%max%)");
        addMessage("Left-Arena", "&f&l&omusicalmobs > &c&l%player% has left the game! &7(%current%/%max%)");
        addMessage("You-Left-Arena", "&f&l&omusicalmobs > &c&l&oYou left the game!");
        addMessage("Game-Starting-In", "&f&l&omusicalmobs > &c&lGame starting in %sec% seconds!");
        addMessage("Not-In-A-Game", "&f&l&omusicalmobs > &c&lGame starting in %sec% seconds!");
        addMessage("GameState.WAITING", "&9&lWAITING");
        addMessage("GameState.COUNTDOWN", "&2&lCOUNTDOWN");
        addMessage("GameState.STARTED", "&4&lSTARTED");
        addMessage("Arena-Full", "&f&l&omusicalmobs > &c&l&oThis arena is full!");
        addMessage("Countdown-Stopped", "&f&l&omusicalmobs > &c&l&oNot enough players! Countdown stopped.");
        addMessage("Arena-Ended", "&f&l&omusicalmobs > &a&lThe arena %id% ended!");
        addMessage("Game-Started", "&f&l&omusicalmobs > &b&lThe game started!");
        addMessage("Get-On-A-Sheep", "&a&l&oGET ON A SHEEP!");
        addMessage("Elimination-In", "&c&lElimination in %t% seconds...");
        addMessage("Player-Eliminated", "&f&l&omusicalmobs > &c&l%player% has been eliminated! &7(%left%/%max%)");
        addMessage("No-One-on-Sheep", "&f&l&omusicalmobs > &c&lNo-one was on a sheep! Game stopped without winner!");
        addMessage("Eliminating", "&c&lEliminating players not on a sheep!");
        addMessage("Already-In-Game", "&f&l&omusicalmobs > &c&lThis arena is in game!");
        addMessage("Command-Not-Exist", "&f&l&omusicalmobs > &c&lThis command doesn't exist!");
        addMessage("Round", "&c&lROUND %round%!");
        addMessage("Not-Valid-Arena-ID", "&f&l&omusicalmobs > §c§lThis is not a valid arena ID.");
        addMessage("Not-Valid-Arena", "&f&l&omusicalmobs > §c§lThis is not a valid arena.");
        addMessage("Error-Happened", "&f&l&omusicalmobs > §c§lAn error happened! Please contact an admin.");
        addMessage("No-Permission", "&f&l&omusicalmobs > &c&lYou don't have the permission!");

        addMessage("Commands.Create", "Create an arena.");
        addMessage("Commands.Delete", "Delete an arena.");
        addMessage("Commands.Force-Start", "Force start an arena.");
        addMessage("Commands.Force-Stop", "Force stop an arena.");
        addMessage("Commands.Infos", "Get the infos about an arena.");
        addMessage("Commands.Join", "Join an arena.");
        addMessage("Commands.Leave", "Leave your current game.");
        addMessage("Commands.List", "Get a list of the arenas.");
        addMessage("Commands.Reload", "Reload arenas.");
        addMessage("Commands.Set", "Set the infos of an arena.");

        SettingsManager.getMessages().set("Broadcast-Win-On-End.broadcast", true);
        addMessage("Broadcast-Win-On-End.message", "&f&l&omusicalmobs > &a&l%player% has won on arena %id%!");
    }

    /**
     * Add a message in the messages.yml file.
     *
     * @param path    The config path.
     * @param message The config value.
     */
    public static void addMessage(String path, String message) {
        settingsManager.addDefault(path, message);
    }

    /**
     * Gets a message.
     *
     * @param path The path of the message in the config.
     * @return a message from a config path.
     */
    public static String getMessage(String path) {
        return ChatColor.translateAlternateColorCodes('&', (String) settingsManager.get(path));
    }


}

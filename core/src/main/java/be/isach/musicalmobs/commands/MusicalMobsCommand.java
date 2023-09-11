package be.isach.musicalmobs.commands;

import org.bukkit.entity.Player;

/**
 * Created by sacha on 21/07/15.
 */
public abstract class MusicalMobsCommand {

    public abstract void onCommand(Player p, String[] args);

    // Message and usage variable.
    private String message, usage;

    // Aliases variable.
    private String[] aliases;

    /**
     * MusicalMobsCommand constructor.
     *
     * @param message
     * @param usage
     * @param aliases
     */
    public MusicalMobsCommand(String message, String usage, String... aliases) {
        this.message = message;
        this.usage = usage;
        this.aliases = aliases;
    }

    /**
     * Gets the message.
     *
     * @return the message.
     */
    public final String getMessage() {
        return message;
    }

    /**
     * Gets the usage.
     *
     * @return the usage.
     */
    public final String getUsage() {
        return usage;
    }

    /**
     * Gets the aliases.
     *
     * @return the aliases.
     */
    public final String[] getAliases() {
        return aliases;
    }

}

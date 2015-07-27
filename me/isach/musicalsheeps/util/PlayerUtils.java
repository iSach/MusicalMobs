package me.isach.musicalsheeps.util;

import me.isach.musicalsheeps.config.MessageManager;
import me.isach.musicalsheeps.core.Core;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * Created by sacha on 25/07/15.
 */
public class PlayerUtils {

    /**
     * Sends a raw message which on click suggests a command.
     * @param p The message receiver.
     * @param text The text.
     * @param command The command to suggest.
     */
    public static void sendRawMessageWithCommand(Player p, String text, String command) {
        Server s = Core.getPlugin().getServer();
        s.dispatchCommand(s.getConsoleSender(), "tellraw " + p.getName() + " [\"\",{\"text\":\"" + text + "\",\"color\":\"white\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"" + command + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"§7§lClick here to pre-enter the command.!\"}]}}}]");
    }

    /**
     * Sends a raw message.
     *
     * @param p         The receiver of the message.
     * @param text      The text.
     * @param hoverText The hover text.
     */
    public static void sendRawMessage(Player p, String text, String hoverText) {
        Server s = Core.getPlugin().getServer();
        s.dispatchCommand(s.getConsoleSender(), "tellraw " + p.getName() + " [\"\",{\"text\":\"" + text + "\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"" + hoverText + "\"}]}}}]");
    }

    /**
     * Sends a message from its path in the messages file.
     * @param p The message receiver.
     * @param path
     */
    public static void sendMessage(Player p, String path) {
        p.sendMessage(MessageManager.getMessage(path));
    }
}

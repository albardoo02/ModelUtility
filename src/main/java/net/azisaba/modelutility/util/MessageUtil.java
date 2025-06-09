package net.azisaba.modelutility.util;

import net.azisaba.modelutility.ModelUtility;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Objects;

public class MessageUtil {

    private final String prefix;
    private final ModelUtility plugin;

    public MessageUtil(ModelUtility plugin) {
        this.plugin = plugin;
        String prefixConfig = plugin.getConfig().getString("prefix");
        if (prefixConfig == null) {
            this.prefix = ChatColor.GRAY + "[" + ChatColor.DARK_AQUA + "§eModelUtil" + ChatColor.GRAY + "] " +ChatColor.RESET;
        } else {
            this.prefix = ChatColor.translateAlternateColorCodes('&', prefixConfig) + " ";
        }
    }

    public void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', message));
    }

    public void errorMessage(CommandSender sender) {
        sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("NoPermission"))));
    }
}

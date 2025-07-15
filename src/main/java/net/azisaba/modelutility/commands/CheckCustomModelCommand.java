package net.azisaba.modelutility.commands;

import net.azisaba.modelutility.ModelUtility;
import net.azisaba.modelutility.util.MessageManager;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public class CheckCustomModelCommand implements CommandExecutor {

    private final ModelUtility plugin;
    private final MessageManager messageManager;

    public CheckCustomModelCommand(ModelUtility plugin, MessageManager messageManager) {
        this.plugin = plugin;
        this.messageManager = messageManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("このコマンドはプレイヤーのみ使用可能です");
            return true;
        }
        Player player = (Player) sender;
        if (command.getName().equals("checkcustommodel")) {
            if (!sender.hasPermission("modelutility.command.checkcustommodel")) {
                messageManager.sendMessage(player, "PermissionError");
                return true;
            }

            ItemStack item = player.getInventory().getItemInMainHand();
            if (item.getType() == Material.AIR) return true;
            ItemMeta meta = item.getItemMeta();
            if (meta == null) return true;
            if (!meta.hasCustomModelData()) return true;
            int id = meta.getCustomModelData();
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("%id%", String.valueOf(id));
            messageManager.sendMessage(player, "ShowCustomModel", placeholders);
            return true;

        }
        return true;
    }
}

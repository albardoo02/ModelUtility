package net.azisaba.modelutility.commands;

import net.azisaba.modelutility.ModelUtility;
import net.azisaba.modelutility.util.MessageUtil;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CheckCustomModelCommand implements CommandExecutor {

    private final ModelUtility plugin;
    private final MessageUtil messageUtil;

    public CheckCustomModelCommand(ModelUtility plugin, MessageUtil messageUtil) {
        this.plugin = plugin;
        this.messageUtil = messageUtil;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("このコマンドはプレイヤーのみ使用可能です");
            return true;
        }
        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("checkcustommodel")) {
            if (!sender.hasPermission("modelutility.command.checkcustommodel")) {
                messageUtil.errorMessage(player);
                return true;
            } else {
                ItemStack item = player.getInventory().getItemInMainHand();
                if (item.getType() == Material.AIR) return true;
                ItemMeta meta = item.getItemMeta();
                if (meta == null) return true;
                if (!meta.hasCustomModelData()) return true;
                int id = meta.getCustomModelData();
                messageUtil.sendMessage(player, "&fカスタムモデルデータ値は&d" + id + "&fです");
                return true;
            }
        }
        return true;
    }
}

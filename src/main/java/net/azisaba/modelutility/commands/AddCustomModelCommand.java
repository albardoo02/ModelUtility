package net.azisaba.modelutility.commands;

import net.azisaba.modelutility.util.MessageManager;
import org.bukkit.command.CommandExecutor;
import net.azisaba.modelutility.ModelUtility;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddCustomModelCommand implements CommandExecutor, TabCompleter {

    private final ModelUtility plugin;
    private final MessageManager messageManager;

    public AddCustomModelCommand(ModelUtility plugin, MessageManager messageManager) {
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
        if (command.getName().equals("addcustommodel")) {
            if (!sender.hasPermission("modelutility.command.addcustommodel")) {
                messageManager.sendMessage(player, "PermissionError");
                return true;
            }

            if (args.length == 0) {
                messageManager.sendMessage(player, "AcmCommandUsage");
            }

            try {
                ItemStack item = player.getInventory().getItemInMainHand();
                ItemMeta meta = item.getItemMeta();
                String target = args[0];
                int id = Integer.parseInt(target);
                assert meta != null;
                meta.setCustomModelData(id);
                item.setItemMeta(meta);

                Map<String, String> placeholders = new HashMap<>();
                placeholders.put("%id%", String.valueOf(id));
                messageManager.sendMessage(player, "SetCustomModel", placeholders);
                return true;
            } catch (NullPointerException | NumberFormatException exception) {
                messageManager.sendMessage(player, "AcmCommandError");
                return true;
            }
        }
        return true;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("acm")) {
            if (args.length == 1) {
                List<String> completions = new ArrayList<>();
                String currentInput = args[0];

                for (int i = 1; i <= 1000; i++) {
                    String numberStr = String.valueOf(i);
                    if (numberStr.startsWith(currentInput)) {
                        completions.add(numberStr);
                    }
                }
                return StringUtil.copyPartialMatches(args[0], completions, new ArrayList<>());
            }
        }
        return java.util.Collections.emptyList();
    }
}

package net.azisaba.modelutility.commands;

import net.azisaba.modelutility.util.MessageUtil;
import org.bukkit.command.CommandExecutor;
import net.azisaba.modelutility.ModelUtility;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AddCustomModelCommand implements CommandExecutor {

    private final ModelUtility plugin;
    private final MessageUtil messageUtil;

    public AddCustomModelCommand(ModelUtility plugin, MessageUtil messageUtil) {
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
        if (command.getName().equalsIgnoreCase("addcustommodel")) {
            if (!sender.hasPermission("modelutility.command.addcustommodel")) {
                messageUtil.errorMessage(player);
                return true;
            }

            if (args.length == 0) {
                messageUtil.sendMessage(player, "&e /acm <モデル番号>");
            }

            try {
                ItemStack item = player.getInventory().getItemInMainHand();
                ItemMeta meta = item.getItemMeta();
                String target = args[0];
                int id = Integer.parseInt(target);
                assert meta != null;
                meta.setCustomModelData(id);
                item.setItemMeta(meta);
                messageUtil.sendMessage(player, "&fカスタムモデルデータ値を&d" + id + "&fに設定しました");
                return true;
            } catch (NullPointerException | NumberFormatException exception) {
                messageUtil.sendMessage(player, "&cコマンドを正しく入力してください");
                return true;
            }
        }
        return true;
    }
}

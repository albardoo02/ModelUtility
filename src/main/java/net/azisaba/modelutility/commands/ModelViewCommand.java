package net.azisaba.modelutility.commands;

import net.azisaba.modelutility.ModelUtility;
import net.azisaba.modelutility.listener.GuiManager;
import net.azisaba.modelutility.util.MessageManager;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

import java.util.*;
import java.util.stream.Collectors;

public class ModelViewCommand implements CommandExecutor, TabCompleter {

    private final ModelUtility plugin;
    private final GuiManager guiManager;
    private final MessageManager messageManager;

    public ModelViewCommand(ModelUtility plugin, GuiManager guiManager, MessageManager messageManager) {
        this.plugin = plugin;
        this.guiManager = guiManager;
        this.messageManager = messageManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("このコマンドはプレイヤーのみが実行できます");
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            ItemStack itemInHand = player.getInventory().getItemInMainHand();
            if (itemInHand == null || itemInHand.getType().isAir()) {
                messageManager.sendMessage(player, "ModelViewCommandUsage");
                return true;
            }
            guiManager.openGui(player, itemInHand.getType(), 1);
            return true;
        }
        if (args.length == 1) {
            Material material = Material.getMaterial(args[0].toUpperCase());
            if (material == null) {
                Map<String, String> placeholders = new HashMap<>();
                placeholders.put("%material%", args[0]);
                messageManager.sendMessage(player, "ModelViewCommandError", placeholders);
                return true;
            }
            guiManager.openGui(player, material, 1);
            return true;
        }
        messageManager.sendMessage(player, "ModelViewCommandUsage");
        return true;
    }

    private static final List<String> MATERIAL_NAMES =
            Arrays.stream(Material.values())
                    .map(material -> material.name().toLowerCase())
                    .collect(Collectors.toList());

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("model")) {
            if (args.length == 1) {
                return StringUtil.copyPartialMatches(args[0], MATERIAL_NAMES, new ArrayList<>());
            }
        }
        return Collections.emptyList();
    }
}

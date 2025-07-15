package net.azisaba.modelutility.listener;

import net.azisaba.modelutility.ModelUtility;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class GuiManager implements Listener {

    private final ModelUtility plugin;

    private static final String GUI_TITLE_CONTAINS = "のモデル一覧";
    private final Map<UUID, Integer> playerPages = new HashMap<>();
    private final Map<UUID, Material> playerViewingItems = new HashMap<>();
    private final Set<UUID> navigatingPlayers = new HashSet<>();

    public GuiManager(ModelUtility plugin) {
        this.plugin = plugin;
    }

    public void openGui(Player player, Material material, int page) {
        playerPages.put(player.getUniqueId(), page);
        playerViewingItems.put(player.getUniqueId(), material);

        String guiTitle = material.name() + GUI_TITLE_CONTAINS + " (" + page + "ページ)";
        Inventory gui = Bukkit.createInventory(null, 54, guiTitle);

        int startIndex = (page - 1) * 45 + 1;
        for (int i = 0; i < 45; i++) {
            int modelData = startIndex + i;
            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setCustomModelData(modelData);
                meta.setDisplayName(ChatColor.WHITE + "CustomModelData: " + ChatColor.AQUA + modelData);
                meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                item.setItemMeta(meta);
            }
            gui.setItem(i, item);
        }

        if (page > 1) {
            ItemStack prevPage = new ItemStack(Material.ARROW);
            ItemMeta prevMeta = prevPage.getItemMeta();
            prevMeta.setDisplayName(ChatColor.GREEN + "前のページ");
            prevMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            prevPage.setItemMeta(prevMeta);
            gui.setItem(48, prevPage);

            ItemStack firstPage = new ItemStack(Material.BUCKET);
            ItemMeta firstMeta = firstPage.getItemMeta();
            firstMeta.setDisplayName(ChatColor.YELLOW + "1ページ目に戻る");
            firstMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            firstPage.setItemMeta(firstMeta);
            gui.setItem(45, firstPage);
        }


        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.setDisplayName(ChatColor.RED + "閉じる");
        closeMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        close.setItemMeta(closeMeta);
        gui.setItem(49, close);

        ItemStack nextPage = new ItemStack(Material.ARROW);
        ItemMeta nextMeta = nextPage.getItemMeta();
        nextMeta.setDisplayName(ChatColor.GREEN + "次のページ");
        nextMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        nextPage.setItemMeta(nextMeta);
        gui.setItem(50, nextPage);


        player.openInventory(gui);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory() == null || !event.getView().getTitle().contains(GUI_TITLE_CONTAINS)) return;
        if (!(event.getWhoClicked() instanceof Player)) return;

        event.setCancelled(true);

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType().isAir()) return;

        Player player = (Player) event.getWhoClicked();
        int currentPage = playerPages.getOrDefault(player.getUniqueId(), 1);
        Material viewingMaterial = playerViewingItems.get(player.getUniqueId());
        if(viewingMaterial == null) return;

        int slot = event.getRawSlot();

        if (slot >= 0 && slot < 45) {
            ItemStack itemToGive = new ItemStack(clickedItem);
            player.getInventory().addItem(itemToGive);
            player.closeInventory();
            return;
        }

        if (slot >= 45 && slot < 54) {
            navigatingPlayers.add(player.getUniqueId());
            switch (slot) {
                case 45:
                    if (currentPage > 1) {
                        openGui(player, viewingMaterial, 1);
                    }
                    break;
                case 48:
                    if (currentPage > 1) {
                        openGui(player, viewingMaterial, currentPage - 1);
                    }
                    break;
                case 49:
                    navigatingPlayers.remove(player.getUniqueId());
                    player.closeInventory();
                    break;
                case 50:
                    openGui(player, viewingMaterial, currentPage + 1);
                    break;
                default:
                    navigatingPlayers.remove(player.getUniqueId());
                    break;
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        UUID playerUUID = event.getPlayer().getUniqueId();

        if (navigatingPlayers.contains(playerUUID)) {
            navigatingPlayers.remove(playerUUID);
            return;
        }
        if (event.getView().getTitle().contains(GUI_TITLE_CONTAINS)) {
            playerPages.remove(playerUUID);
            playerViewingItems.remove(playerUUID);
        }
    }
}
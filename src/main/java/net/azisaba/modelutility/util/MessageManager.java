package net.azisaba.modelutility.util;

import net.azisaba.modelutility.ModelUtility;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

public class MessageManager {

    private final ModelUtility plugin;
    private final Map<String, Map<String, Object>> allMessages = new HashMap<>();
    private String defaultLang;
    private String prefix;

    public MessageManager(ModelUtility plugin) {
        this.plugin = plugin;
        loadAllMessages();
    }

    public void loadAllMessages() {
        allMessages.clear();
        this.defaultLang = plugin.getConfig().getString("lang", "ja");

        this.prefix = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix", ""));

        File langFolder = new File(plugin.getDataFolder(), "lang");
        if (!langFolder.exists()) {
            langFolder.mkdirs();
        }

        File[] langFiles = langFolder.listFiles((dir, name) -> name.matches("message_\\w+\\.yml"));

        if (langFiles == null || langFiles.length == 0) {
            plugin.getLogger().info("言語ファイルが見つからなかったため、デフォルトファイルを生成します");
            plugin.saveResource("lang/message_en.yml", false);
            plugin.saveResource("lang/message_ja.yml", false);
            langFiles = langFolder.listFiles((dir, name) -> name.matches("message_\\w+\\.yml"));
        }

        if (langFiles == null) {
            plugin.getLogger().severe("言語フォルダの読み込みに失敗しました。");
            return;
        }

        for (File langFile : langFiles) {
            String langCode = langFile.getName().replace("message_", "").replace(".yml", "");
            FileConfiguration langConfig = YamlConfiguration.loadConfiguration(langFile);
            Map<String, Object> messages = new HashMap<>();
            for (String key : langConfig.getKeys(true)) {
                if (!langConfig.isConfigurationSection(key)) {
                    messages.put(key, langConfig.get(key));
                }
            }
            allMessages.put(langCode, messages);
            plugin.getLogger().info(langFile.getName() + "を読み込みました");
        }
    }

    public void sendMessage(Player player, String key, Map<String, String> placeholders) {
        String playerLang = getPlayerLanguage(player);
        Object messageObject = getMessageObject(playerLang, key);

        List<String> messagesToSend;
        if (messageObject instanceof List) {
            messagesToSend = (List<String>) messageObject;
        } else {
            messagesToSend = Collections.singletonList(String.valueOf(messageObject));
        }

        for (int i = 0; i < messagesToSend.size(); i++) {
            String line = messagesToSend.get(i);
            String processedLine = replacePlaceholders(line, placeholders);
            String coloredLine = ChatColor.translateAlternateColorCodes('&', processedLine);

            if (i == 0) {
                player.sendMessage(this.prefix + " " + coloredLine);
            } else {
                player.sendMessage(coloredLine);
            }
        }
    }

    public void sendMessage(Player player, String key, String... replacements) {
        if (replacements.length % 2 != 0) {
            plugin.getLogger().warning("プレースホルダーの置換ペアが正しくありません");
            sendMessage(player, key, new HashMap<>());
            return;
        }

        Map<String, String> placeholders = new HashMap<>();
        for (int i = 0; i < replacements.length; i += 2) {
            placeholders.put(replacements[i], replacements[i + 1]);
        }
        sendMessage(player, key, placeholders);
    }

    private String replacePlaceholders(String text, Map<String, String> placeholders) {
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            text = text.replace(entry.getKey(), entry.getValue());
        }
        return text;
    }

    private String getPlayerLanguage(Player player) {
        return player.getLocale().split("_")[0].toLowerCase();
    }

    private Object getMessageObject(String lang, String key) {
        Map<String, Object> messages = allMessages.get(lang);
        if (messages == null) {
            messages = allMessages.get(defaultLang);
        }

        if (messages == null) {
            return "&c言語ファイルが読み込まれていません: " + lang;
        }

        return messages.getOrDefault(key, "&cメッセージが見つかりません: " + key);
    }
}

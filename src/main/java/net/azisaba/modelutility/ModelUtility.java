package net.azisaba.modelutility;

import net.azisaba.modelutility.commands.AddCustomModelCommand;
import net.azisaba.modelutility.commands.CheckCustomModelCommand;
import net.azisaba.modelutility.commands.ModelViewCommand;
import net.azisaba.modelutility.listener.GuiManager;
import net.azisaba.modelutility.util.MessageManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class ModelUtility extends JavaPlugin {

    private MessageManager messageManager;
    private GuiManager guiManager;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        saveIfNotExists("lang/message_ja.yml");
        saveIfNotExists("lang/message_en.yml");

        this.messageManager = new MessageManager(this);
        this.guiManager = new GuiManager(this);

        this.getCommand("addcustommodel").setExecutor(new AddCustomModelCommand(this, messageManager));
        this.getCommand("addcustommodel").setTabCompleter(new AddCustomModelCommand(this, messageManager));
        this.getCommand("checkcustommodel").setExecutor(new CheckCustomModelCommand(this, messageManager));
        this.getCommand("modelview").setExecutor(new ModelViewCommand(this, guiManager, messageManager));
        this.getCommand("modelview").setExecutor(new ModelViewCommand(this, guiManager, messageManager));

        this.getServer().getPluginManager().registerEvents(guiManager, this);


    }

    private void saveIfNotExists(String filename) {
        File file = new File(getDataFolder(), filename);
        if (!file.exists()) {
            saveResource(filename, false);
        }
    }
}

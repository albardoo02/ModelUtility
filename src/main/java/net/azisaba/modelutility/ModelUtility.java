package net.azisaba.modelutility;

import net.azisaba.modelutility.commands.AddCustomModelCommand;
import net.azisaba.modelutility.util.MessageUtil;
import org.bukkit.plugin.java.JavaPlugin;

public final class ModelUtility extends JavaPlugin {

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        MessageUtil messageUtil = new MessageUtil(this);
        this.getCommand("addcustommodel").setExecutor(new AddCustomModelCommand(this, messageUtil));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

package net.azisaba.modelutility;

import net.azisaba.modelutility.commands.AddCustomModelCommand;
import net.azisaba.modelutility.commands.CheckCustomModelCommand;
import net.azisaba.modelutility.commands.CustomModelListCommand;
import net.azisaba.modelutility.util.MessageUtil;
import org.bukkit.plugin.java.JavaPlugin;

public final class ModelUtility extends JavaPlugin {

    @Override
    public void onEnable() {
        MessageUtil messageUtil = new MessageUtil(this);
        
        this.saveDefaultConfig();
        this.getCommand("addcustommodel").setExecutor(new AddCustomModelCommand(this, messageUtil));
        this.getCommand("checkcustommodel").setExecutor(new CheckCustomModelCommand(this, messageUtil));
        this.getCommand("custommodellist").setExecutor(new CustomModelListCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

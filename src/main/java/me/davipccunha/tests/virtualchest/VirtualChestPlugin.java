package me.davipccunha.tests.virtualchest;

import lombok.Getter;
import me.davipccunha.tests.economy.api.EconomyAPI;
import me.davipccunha.tests.virtualchest.command.BauCommand;
import me.davipccunha.tests.virtualchest.listener.InventoryClickListener;
import me.davipccunha.tests.virtualchest.listener.PlayerJoinListener;
import me.davipccunha.tests.virtualchest.model.ChestUser;
import me.davipccunha.utils.cache.RedisCache;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class VirtualChestPlugin extends JavaPlugin {
    private RedisCache<ChestUser> chestUserCache;
    private EconomyAPI economyAPI;

    @Override
    public void onEnable() {
        this.init();
        getLogger().info("Virtual Chest plugin loaded!");
    }

    public void onDisable() {
        getLogger().info("Virtual Chest plugin unloaded!");
    }

    private void init() {
        saveDefaultConfig();
        this.registerListeners(
                new PlayerJoinListener(this),
                new InventoryClickListener(this)
        );
        this.registerCommands();

        this.loadCaches();

        if (Bukkit.getPluginManager().getPlugin("economy") == null)
            getLogger().warning("Economy not found. Buying and upgrading chests will be disabled.");

        this.economyAPI = Bukkit.getServicesManager().load(EconomyAPI.class);
    }

    private void registerListeners(Listener... listeners) {
        PluginManager pluginManager = getServer().getPluginManager();
        for (Listener listener : listeners) pluginManager.registerEvents(listener, this);
    }

    private void registerCommands() {
        getCommand("bau").setExecutor(new BauCommand(this));
    }

    private void loadCaches() {
        this.chestUserCache = new RedisCache<>("chest-users", ChestUser.class);
    }
}

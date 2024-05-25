package me.davipccunha.tests.virtualchest.listener;

import lombok.RequiredArgsConstructor;
import me.davipccunha.tests.virtualchest.VirtualChestPlugin;
import me.davipccunha.tests.virtualchest.model.ChestUser;
import me.davipccunha.utils.cache.RedisCache;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@RequiredArgsConstructor
public class PlayerJoinListener implements Listener {
    private final VirtualChestPlugin plugin;

    @EventHandler(priority = EventPriority.MONITOR)
    private void onPlayerJoin(PlayerJoinEvent event) {
        final RedisCache<ChestUser> cache = plugin.getChestUserCache();
        final String name = event.getPlayer().getName().toLowerCase();

        final ChestUser chestUser = cache.get(name);

        if (chestUser == null)
            cache.add(name, new ChestUser(event.getPlayer().getName()));
    }
}

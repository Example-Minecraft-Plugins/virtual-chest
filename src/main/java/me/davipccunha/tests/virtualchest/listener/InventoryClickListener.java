package me.davipccunha.tests.virtualchest.listener;

import lombok.RequiredArgsConstructor;
import me.davipccunha.tests.economy.api.EconomyAPI;
import me.davipccunha.tests.virtualchest.VirtualChestPlugin;
import me.davipccunha.tests.virtualchest.factory.VirtualChestGUI;
import me.davipccunha.tests.virtualchest.model.ChestUser;
import me.davipccunha.tests.virtualchest.model.VirtualChest;
import me.davipccunha.utils.cache.RedisCache;
import me.davipccunha.utils.item.ItemSerializer;
import me.davipccunha.utils.item.NBTHandler;
import me.davipccunha.utils.messages.ErrorMessages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Objects;

@RequiredArgsConstructor
public class InventoryClickListener implements Listener {
    private final VirtualChestPlugin plugin;

    @EventHandler
    private void onInventoryClick(InventoryClickEvent event) {
        final Inventory inventory = event.getInventory();
        final Player player = (Player) event.getWhoClicked();

        if (inventory == null || player == null) return;
        if (event.getCurrentItem() == null) return;

        final String inventoryName = inventory.getName();

        if (!inventoryName.equals("Baús Virtuais")) return;

        event.setCancelled(true);

        if (!event.getCurrentItem().hasItemMeta()) return;
        if (!event.getCurrentItem().getItemMeta().hasDisplayName()) return;


        final ItemStack clickedItem = event.getCurrentItem();
        final String action = NBTHandler.getNBT(clickedItem, "action");

        if (action == null) return;

        final ChestUser chestUser = this.plugin.getChestUserCache().get(player.getName().toLowerCase());
        if (chestUser == null) {
            player.sendMessage(ErrorMessages.INTERNAL_ERROR.getMessage());
            event.setCancelled(true);
            return;
        }

        switch (action) {
            case "open":
                final int index = Integer.parseInt(NBTHandler.getNBT(clickedItem, "chest-index"));
                final VirtualChest virtualChest = chestUser.getChest(index);
                final Inventory chestInventory = VirtualChestGUI.loadVirtualChest(virtualChest, index);

                // This has to run in the next tick as specified in the documentation
                Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                    player.openInventory(chestInventory);
                }, 1L);

                break;

            case "upgrade":
                if (chestUser.getHalfChestsOwned() == 14) {
                    player.sendMessage("§cVocê já atingiu o limite de baús.");
                    return;
                }

                final EconomyAPI api = this.plugin.getEconomyAPI();
                if (api == null) {
                    player.sendMessage(ErrorMessages.INTERNAL_ERROR.getMessage());
                    return;
                }

                final boolean createdNewChest = chestUser.upgrade();
                final String updateMessage = createdNewChest ?
                        String.format("§aNovo baú #%d comprado com sucesso", chestUser.getChestAmount()) :
                        String.format("§aSeu baú #%d foi melhorado para um baú de 54 slots.", chestUser.getChestAmount());

                // TODO: Implement economy

                this.plugin.getChestUserCache().add(player.getName().toLowerCase(), chestUser);

                player.sendMessage(updateMessage);

                // This has to run in the next tick as specified in the documentation
                Bukkit.getScheduler().runTaskLater(this.plugin, player::closeInventory, 1L);

                break;
        }
    }

    @EventHandler
    private void onVirtualChestUpdate(InventoryClickEvent event) {
        if (!event.getInventory().getName().startsWith("Baú #")) return;

        final Inventory inventory = event.getInventory();
        final Player player = (Player) event.getWhoClicked();
        if (inventory == null || player == null || event.getCurrentItem() == null) return;

        final RedisCache<ChestUser> cache = this.plugin.getChestUserCache();

        final ChestUser chestUser = cache.get(player.getName().toLowerCase());
        if (chestUser == null) {
            player.sendMessage(ErrorMessages.INTERNAL_ERROR.getMessage());
            event.setCancelled(true);
            return;
        }

        final int number = Integer.parseInt(inventory.getName().split("#")[1]);

        // This has to run in the next tick because since the inventory is updated after the click event is called,
        // the inventory contents are not updated yet.
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
            final String serializedInventory = ItemSerializer.serialize(inventory.getContents());

            chestUser.getChest(number - 1).setSerializedInventory(serializedInventory);

            cache.add(player.getName().toLowerCase(), chestUser);
        }, 1L);

    }
}

package me.davipccunha.tests.virtualchest.factory;

import me.davipccunha.tests.virtualchest.model.ChestUser;
import me.davipccunha.utils.inventory.InteractiveInventory;
import me.davipccunha.utils.item.CustomHead;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class VirtualChestMenuGUI {
    public static Inventory buildVirtualChestMenu(ChestUser chestUser) {
        Inventory inventory = Bukkit.createInventory(null, 4 * 9, "Baús Virtuais");

        final ItemStack chest = new ItemStack(Material.CHEST);

        final List<String> lore = List.of(
            "§7Clique para abrir este baú"
        );

        for (int i = 1; i <= chestUser.getChestAmount(); i++) {
            final Map<String, String> nbtTags = Map.of(
                    "action", "open",
                    "chest-index", String.valueOf(i - 1)
            );

            final ItemStack openChest = InteractiveInventory.createActionItem(chest, nbtTags, "§eBaú #" + i, lore);

            inventory.setItem(9 + i, openChest);
        }

        final ItemStack upgradeItem = getUpgradeItem();

        inventory.setItem(31, upgradeItem);

        return inventory;
    }

    private static ItemStack getUpgradeItem() {
        final ItemStack upgradeSkull = CustomHead.getCustomHead(
                null,
                "http://textures.minecraft.net/texture/a99aaf2456a6122de8f6b62683f2bc2eed9abb81fd5bea1b4c23a58156b669",
                UUID.randomUUID());

        final Map<String, String> nbtTags = Map.of(
            "action", "upgrade"
        );

        final List<String> lore = List.of(
            "§7Clique para melhorar o seu baú"
        );

        return InteractiveInventory.createActionItem(upgradeSkull, nbtTags, "§aMelhorar Baú", lore);
    }
}

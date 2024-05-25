package me.davipccunha.tests.virtualchest.factory;

import me.davipccunha.tests.virtualchest.model.VirtualChest;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;

public class VirtualChestGUI {
    public static Inventory loadVirtualChest(VirtualChest chest, int index) {
        final boolean doubled = chest.isDoubled();
        final int size = (doubled ? 6 : 3) * 9;

        final Inventory inventory = Bukkit.createInventory(null, size, "Baú #" + (index + 1));

        inventory.setContents(Arrays.copyOfRange(chest.getItems(), 0, size));

        return inventory;
    }
}

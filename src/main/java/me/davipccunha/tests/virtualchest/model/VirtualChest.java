package me.davipccunha.tests.virtualchest.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.davipccunha.utils.item.ItemSerializer;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
@Getter
public class VirtualChest {
    private final String owner;
    private boolean doubled;

    @Setter
    private String serializedInventory;

    public void upgradeChest() {
        this.doubled = true;
    }

    public ItemStack[] getItems() {
        return ItemSerializer.deserialize(this.serializedInventory);
    }
}

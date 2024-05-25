package me.davipccunha.tests.virtualchest.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ChestUser {
    private final String username;
    private final List<VirtualChest> chests = new ArrayList<>();
    private int halfChestsOwned = 0;

    public ChestUser(String username) {
        this.username = username;
        this.addChest();
    }

    public VirtualChest getChest(int index) {
        return this.chests.get(index);
    }

    public int getChestAmount() {
        return this.chests.size();
    }

    public boolean upgrade() {
        final int lastIndex = this.chests.size() - 1;
        final VirtualChest lastChest = this.chests.get(lastIndex);

        if (lastChest.isDoubled()) {
            this.addChest();
            return true;
        }

        this.upgradeChest(lastIndex);
        return false;
    }

    public void addChest() {
        this.chests.add(new VirtualChest(this.username));
        this.halfChestsOwned += 1;
    }

    public void upgradeChest(int index) {
        this.chests.get(index).upgradeChest();
        this.halfChestsOwned += 1;
    }
}

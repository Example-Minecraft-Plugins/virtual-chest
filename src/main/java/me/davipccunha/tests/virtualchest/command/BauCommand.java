package me.davipccunha.tests.virtualchest.command;

import lombok.RequiredArgsConstructor;
import me.davipccunha.tests.virtualchest.VirtualChestPlugin;
import me.davipccunha.tests.virtualchest.factory.VirtualChestMenuGUI;
import me.davipccunha.tests.virtualchest.model.ChestUser;
import me.davipccunha.utils.messages.ErrorMessages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

@RequiredArgsConstructor
public class BauCommand implements CommandExecutor {
    private final VirtualChestPlugin plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ErrorMessages.EXECUTOR_NOT_PLAYER.getMessage());
            return true;
        }

        final Player player = (Player) sender;

        final ChestUser chestUser = this.plugin.getChestUserCache().get(player.getName().toLowerCase());
        if (chestUser == null) {
            player.sendMessage(ErrorMessages.INTERNAL_ERROR.getMessage());
            return true;
        }

        final Inventory inventory = VirtualChestMenuGUI.buildVirtualChestMenu(chestUser);
        player.openInventory(inventory);

        return true;
    }
}

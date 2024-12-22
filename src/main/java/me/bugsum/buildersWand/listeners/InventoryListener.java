package me.bugsum.buildersWand.listeners;

import me.bugsum.buildersWand.GlobalVariables;
import me.bugsum.buildersWand.items.BuilderWand;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(e.getClickedInventory() == null) {
            return;
        }

        Player player = (Player) e.getWhoClicked();

        if (e.getView().getTitle().contains("Crafting")) {
            if(BuilderWand.wand_positions.get(player.getUniqueId().toString()) == null) {
                return;
            }
            if(BuilderWand.wand_positions.get(player.getUniqueId().toString()).pos1 != null && BuilderWand.wand_positions.get(player.getUniqueId().toString()).pos2 != null && BuilderWand.wand_positions.get(player.getUniqueId().toString()).state == BuilderWand.State.select) {
                e.setCancelled(true);

                if(e.getClickedInventory().getItem(e.getSlot()) == null) {
                    return;
                }

                Material block = e.getClickedInventory().getItem(e.getSlot()).getType();
                if(!block.isBlock()) {
                    player.sendMessage(GlobalVariables.error_prefix + "Clicked item is not a block! Try again.");
                    return;
                }
                if(BuilderWand.fillBlocks(player, BuilderWand.wand_positions.get(player.getUniqueId().toString()).pos1, BuilderWand.wand_positions.get(player.getUniqueId().toString()).pos2, block)) {
                    player.sendMessage(GlobalVariables.success_prefix + "You have started filling a selection!");
                    player.closeInventory();
                    BuilderWand.wand_positions.get(player.getUniqueId().toString()).state = BuilderWand.State.build;
                }
            }
        }
    }
}

package me.bugsum.buildersWand.listeners;

import me.bugsum.buildersWand.GlobalVariables;
import me.bugsum.buildersWand.items.BuilderWand;
import me.bugsum.buildersWand.utils.BuilderUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {
    @EventHandler
    public void onPlayerUse(PlayerInteractEvent e) {
        Player player = (Player) e.getPlayer();

        if (player.getItemInHand().getItemMeta() == null) {
            return;
        }
        if(player.getItemInHand().getItemMeta().getDisplayName().contains("Builder's Wand")) {
            if(BuilderWand.wand_positions.get(player.getUniqueId().toString()) == null) {
                BuilderWand.wand_positions.put(player.getUniqueId().toString(), new BuilderWand.wand_pos());
            }
        }

        if (player.getItemInHand().getItemMeta().getDisplayName().contains("Builder's Wand") && BuilderWand.wand_positions.get(player.getUniqueId().toString()).state == BuilderWand.State.select) {
            if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
                e.setCancelled(true);
                if (BuilderUtils.isInSpawn(e.getClickedBlock().getLocation())) {
                    player.sendMessage(GlobalVariables.error_prefix + "You can not use the builder's wand here.");
                    return;
                }

                if (!BuilderUtils.canPlaceOnIsland(player, e.getClickedBlock().getLocation())) {
                    player.sendMessage(GlobalVariables.error_prefix + "You can not use the builder's wand here.");
                    return;
                }

                if (BuilderWand.wand_positions.get(player.getUniqueId().toString()).pos1 == null) {
                    BuilderWand.wand_positions.get(player.getUniqueId().toString()).pos1 = e.getClickedBlock().getLocation();
                    player.sendMessage(GlobalVariables.alert_prefix + "You have set the first position with the Builder's Wand!");
                } else {
                    BuilderWand.wand_positions.get(player.getUniqueId().toString()).pos2 = e.getClickedBlock().getLocation();
                    player.sendMessage(GlobalVariables.alert_prefix + "You have made a selection with the Builders Wand. Click a block in your inventory to fill the selection or rightclick to remove the selection.");
                }
            } else if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
                if (player.getItemInHand().getItemMeta().getDisplayName().contains("Builder's Wand")) {
                    if (BuilderWand.wand_positions.get(player.getUniqueId().toString()) != null) {
                        player.performCommand("selectionclear");
                    }
                }
            }
        } else if(player.getItemInHand().getItemMeta().getDisplayName().contains("Builder's Wand") && BuilderWand.wand_positions.get(player.getUniqueId().toString()).state == BuilderWand.State.build) {
            player.sendMessage(GlobalVariables.error_prefix + "You can't use the wand while another selection is in progress.");
        }
    }
}

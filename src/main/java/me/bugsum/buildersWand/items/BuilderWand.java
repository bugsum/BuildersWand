package me.bugsum.buildersWand.items;

import me.bugsum.buildersWand.BuildersWand;
import me.bugsum.buildersWand.GlobalVariables;
import me.bugsum.buildersWand.utils.BuilderUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class BuilderWand {
    public enum State {
        select,
        build
    }

    public static class wand_pos {
        public Location pos1;
        public Location pos2;
        public State state = State.select;
        public List<Location> blocks = new ArrayList<>();
        public Material block;
        public boolean isInProgress = false;
        public wand_pos() {
        }

    }

    public static Component name = Component.text("Builder's Wand").color(NamedTextColor.YELLOW);

    public static HashMap<String, wand_pos> wand_positions = new HashMap<>();

    public static boolean fillBlocks(Player sender, Location pos1, Location pos2, Material block) {
        if (pos1.getWorld() != pos2.getWorld()) {
            sender.sendMessage(GlobalVariables.error_prefix + "Locations are not in the same world! Aborting.");
            return false;
        }

        int startX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        int endX = Math.max(pos1.getBlockX(), pos2.getBlockX());
        int startY = Math.min(pos1.getBlockY(), pos2.getBlockY());
        int endY = Math.max(pos1.getBlockY(), pos2.getBlockY());
        int startZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        int endZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());

        // Check each location in the range
        for (int y = startY; y <= endY; y++) {
            for (int z = startZ; z <= endZ; z++) {
                for (int x = startX; x <= endX; x++) {
                    Location currentLocation = new Location(pos1.getWorld(), x, y, z);

                    // Check if the player can place a block at the current location
                    if (!BuilderUtils.canPlaceOnIsland(sender, currentLocation) || BuilderUtils.isInSpawn(currentLocation)) {
                        sender.sendMessage(GlobalVariables.error_prefix + "You cannot place blocks here!");
                        return false;  // Abort if any location is not allowed
                    }
                }
            }
        }

        int totalBlocks = (endX - startX + 1) * (endY - startY + 1) * (endZ - startZ + 1);
        int blocksInInventory = 0;

        for (ItemStack item : sender.getInventory().getContents()) {
            if (item != null && item.getType() == block) {
                blocksInInventory += item.getAmount();
            }
        }

        if (blocksInInventory < totalBlocks) {
            sender.sendMessage(GlobalVariables.error_prefix + "You do not have enough blocks in your inventory! " + totalBlocks + " are needed.");
            return false;
        }

        int remainingBlocks = totalBlocks;

        for (int itemSlot = 0; itemSlot < sender.getInventory().getSize(); itemSlot++) {
            ItemStack item = sender.getInventory().getItem(itemSlot);
            if (item == null || item.getType() != block) {
                continue;
            }

            int removeAmount = Math.min(item.getAmount(), remainingBlocks);
            item.setAmount(item.getAmount() - removeAmount);
            remainingBlocks -= removeAmount;

            if (item.getAmount() == 0) {
                sender.getInventory().setItem(itemSlot, null);
            }

            if (remainingBlocks <= 0) {
                break;
            }
        }

        wand_pos wandPos = wand_positions.computeIfAbsent(sender.getUniqueId().toString(), k -> new wand_pos());
        wandPos.block = block;
        wandPos.blocks.clear();

        for (int y = startY; y <= endY; y++) {
            for (int z = startZ; z <= endZ; z++) {
                for (int x = startX; x <= endX; x++) {
                    wandPos.blocks.add(new Location(pos1.getWorld(), x, y, z));
                }
            }
        }

        return true;
    }


    public static void renderSelections() {
        for(var i : wand_positions.entrySet())  {
            if(i.getValue().pos1 != null && i.getValue().pos2 != null) {
                UUID uuid = UUID.fromString(i.getKey().toString());
                if(BuildersWand.getPlugin().getServer().getPlayer(uuid) != null) {
                    BuildersWand.getPlugin().getServer().getPlayer(uuid).performCommand("renderselection");
                }
            }
        }
    }

    public static void placeBlocks() {
        for(var i : wand_positions.entrySet())  {
            if(i.getValue().pos1 != null && i.getValue().pos2 != null) {
                UUID uuid = UUID.fromString(i.getKey());
                if(BuildersWand.getPlugin().getServer().getPlayer(uuid) != null) {
                    if(wand_positions.get(i.getKey()).state == BuilderWand.State.build) {
                        Player player = BuildersWand.getPlugin().getServer().getPlayer(uuid);
                        Objects.requireNonNull(player).performCommand("renderselection");

                        if (!wand_positions.get(i.getKey()).blocks.isEmpty()) {
                            Location currentLocation = wand_positions.get(i.getKey()).blocks.get(0);

                            // Check if the player can place block at this location
                            if (BuilderUtils.canPlaceOnIsland(player, currentLocation) || !BuilderUtils.isInSpawn(currentLocation)) {
                                currentLocation.getWorld().setBlockData(
                                        currentLocation,
                                        wand_positions.get(i.getKey()).block.createBlockData());
                                wand_positions.get(i.getKey()).blocks.removeFirst();
                            } else {
                                player.sendMessage(GlobalVariables.error_prefix + "You are not allowed to place blocks here.");
                            }
                        } else {
                            wand_positions.remove(i.getKey());
                        }
                    }
                }
            }
        }
    }


    public static void give(Player player) {
        ItemStack wand = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta itemMeta = wand.getItemMeta();

        itemMeta.displayName(name);
        itemMeta.lore(List.of(
                Component.text("A magical tool of the ancient builders.")
                        .color(NamedTextColor.GRAY),
                Component.empty(),
                Component.text("Left-click to select region.")
                        .color(NamedTextColor.GOLD)
                        .decoration(TextDecoration.ITALIC, false)
        ));

        wand.setItemMeta(itemMeta);
        player.getInventory().addItem(wand);
        player.sendMessage(Component.text("You have been given a Builder's Wand!").color(NamedTextColor.GREEN));
    }
}

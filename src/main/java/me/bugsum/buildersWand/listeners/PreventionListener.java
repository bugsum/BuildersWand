package me.bugsum.buildersWand.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class PreventionListener implements Listener {
    @EventHandler
    public void onItemCraft(PrepareItemCraftEvent event) {
        ItemStack result = Objects.requireNonNull(event.getRecipe()).getResult();
        if (result.getType() == Material.BLAZE_POWDER) {
            for (ItemStack item : event.getInventory().getContents()) {
                if (item != null && item.getType() == Material.BLAZE_ROD) {
                    // Prevent crafting Blaze Powder with Blaze Rod
                    event.getInventory().setResult(new ItemStack(Material.AIR));
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onFuelBurn(FurnaceBurnEvent event) {
        ItemStack fuel = event.getFuel();
        if (fuel.getType() == Material.BLAZE_ROD) {
            // Prevent Blaze Rod from being used as fuel
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCraftBrewingStand(PrepareItemCraftEvent event) {
        ItemStack result = Objects.requireNonNull(event.getRecipe()).getResult();
        if (result.getType() == Material.BREWING_STAND) {
            for (ItemStack item : event.getInventory().getContents()) {
                if (item != null && item.getType() == Material.BLAZE_ROD) {
                    // Prevent Brewing Stand craft
                    event.getInventory().setResult(new ItemStack(Material.AIR));
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onCraftEyeOfEnder(PrepareItemCraftEvent event) {
        ItemStack result = Objects.requireNonNull(event.getRecipe()).getResult();
        if (result.getType() == Material.ENDER_EYE) {
            for (ItemStack item : event.getInventory().getContents()) {
                if (item != null && item.getType() == Material.BLAZE_POWDER) {
                    // Prevent crafting Eyes of Ender
                    event.getInventory().setResult(new ItemStack(Material.AIR));
                    return;
                }
            }
        }
    }
}

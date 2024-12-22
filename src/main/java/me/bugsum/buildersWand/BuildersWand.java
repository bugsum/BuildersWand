package me.bugsum.buildersWand;

import me.bugsum.buildersWand.commands.CommandRenderSelection;
import me.bugsum.buildersWand.commands.CommandSelectionClear;
import me.bugsum.buildersWand.commands.GiveCommand;
import me.bugsum.buildersWand.items.BuilderWand;
import me.bugsum.buildersWand.listeners.InventoryListener;
import me.bugsum.buildersWand.listeners.PlayerInteractListener;
import me.bugsum.buildersWand.listeners.PreventionListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class BuildersWand extends JavaPlugin {
    private static BuildersWand instance;

    public void schedule(long RenderTicks, long PlaceTicks) {
        int renderSelectionTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(this,
                BuilderWand::renderSelections, 0L, RenderTicks);
        int placeBlocksTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(this,
                BuilderWand::placeBlocks, 0L, PlaceTicks);
    }

    @Override
    public void onEnable() {
        instance = this;

        Objects.requireNonNull(getCommand("selectionclear")).setExecutor(new CommandSelectionClear());
        Objects.requireNonNull(getCommand("renderselection")).setExecutor(new CommandRenderSelection());
        Objects.requireNonNull(getCommand("getwand")).setExecutor(new GiveCommand());

        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        getServer().getPluginManager().registerEvents(new PreventionListener(), this);

        getServer().getConsoleSender().sendMessage(GlobalVariables.title + "Initialized");

        schedule(5, 1);
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(GlobalVariables.title + "Disabled");
    }

    public static BuildersWand getPlugin() {
        return instance;
    }
}

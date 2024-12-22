package me.bugsum.buildersWand.commands;

import me.bugsum.buildersWand.items.BuilderWand;
import me.bugsum.buildersWand.utils.WandType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GiveCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage("§cOnly players can use this command.");
            return true;
        }

        BuilderWand.give(player);
        return true;
    }
}

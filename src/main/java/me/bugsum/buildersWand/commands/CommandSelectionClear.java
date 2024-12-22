package me.bugsum.buildersWand.commands;

import me.bugsum.buildersWand.GlobalVariables;
import me.bugsum.buildersWand.items.BuilderWand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandSelectionClear implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage(GlobalVariables.error_prefix + "You must run this command as player!");
            return true;
        }

        BuilderWand.wand_positions.remove(player.getUniqueId().toString());
        player.sendMessage(GlobalVariables.success_prefix + "Cleared Selection!");

        return true;
    }
}

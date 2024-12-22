package me.bugsum.buildersWand.utils;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class BuilderUtils {
    public static boolean isInSpawn(Location location) {
        if (location.getWorld().getName().equals("world")) {
            return true;
        }

        return false;
    }

    public static boolean canPlaceOnIsland(Player player, Location location) {
        SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(player);

        if (superiorPlayer != null) {
            Island playerIsland = superiorPlayer.getIsland();
            Island targetIsland = SuperiorSkyblockAPI.getGrid().getIslandAt(location);

            if (targetIsland != null && !targetIsland.equals(playerIsland)) {
                return false;
            }

            return true;
        } else {
            return false;
        }
    }
}

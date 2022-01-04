package be.marijn2341.feroxcore.manager.statistics;

import be.marijn2341.feroxcore.listeners.ArrowShootListener;
import be.marijn2341.feroxcore.listeners.BlockPlaceListener;
import be.marijn2341.feroxcore.listeners.DeathListener;
import be.marijn2341.feroxcore.utils.Utils;
import org.bukkit.entity.Player;

public class GameStatistics {

    public static void sendGameStats(Player player, String matchtijd) {
        player.sendMessage(Utils.color("&8-----"));
        player.sendMessage(Utils.color("&7Total blocks placed (global): &f" + BlockPlaceListener.BLOCKS.get("placed")));
        player.sendMessage(Utils.color("&7Total blocks broken (global): &f" + BlockPlaceListener.BLOCKS.get("broken")));
        player.sendMessage(Utils.color("&7Matchtime: &f" + matchtijd));
        player.sendMessage(Utils.color("&7Your kills: &f" + DeathListener.KILLS.get(player.getUniqueId())));
        player.sendMessage(Utils.color("&7Your deaths: &f" + DeathListener.DEATHS.get(player.getUniqueId())));
        player.sendMessage(Utils.color("&7Arrows shot: &f" + ArrowShootListener.SHOT.get(player.getUniqueId())));
        player.sendMessage(Utils.color("&7Arrows hit: &f" + DeathListener.ARROWSHIT.get(player.getUniqueId())));
        player.sendMessage(Utils.color("&8-----"));
    }
}

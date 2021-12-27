package be.marijn2341.feroxcore.Manager.Statistics;

import be.marijn2341.feroxcore.Listeners.ArrowShootListener;
import be.marijn2341.feroxcore.Listeners.BlockPlaceListener;
import be.marijn2341.feroxcore.Listeners.DeathListener;
import be.marijn2341.feroxcore.Manager.MapManager;
import be.marijn2341.feroxcore.Utils.Utils;
import org.bukkit.entity.Player;

public class GameStatistics {

    public static void sendGameStats(Player player, String matchtijd) {
        player.sendMessage(Utils.color("&8-----"));
        player.sendMessage(Utils.color("&7Total blocks placed (global): &f" + BlockPlaceListener.blocks.get("placed")));
        player.sendMessage(Utils.color("&7Total blocks broken (global): &f" + BlockPlaceListener.blocks.get("broken")));
        player.sendMessage(Utils.color("&7Matchtime: &f" + matchtijd));
        player.sendMessage(Utils.color("&7Your kills: &f" + DeathListener.Kills.get(player.getUniqueId())));
        player.sendMessage(Utils.color("&7Your deaths: &f" + DeathListener.Deaths.get(player.getUniqueId())));
        player.sendMessage(Utils.color("&7Arrows shot: &f" + ArrowShootListener.shot.get(player.getUniqueId())));
        player.sendMessage(Utils.color("&7Arrows hit: &f" + DeathListener.arrowshit.get(player.getUniqueId())));
        player.sendMessage(Utils.color("&8-----"));
    }
}

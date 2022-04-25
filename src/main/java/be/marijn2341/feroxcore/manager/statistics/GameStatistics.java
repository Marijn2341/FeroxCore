package be.marijn2341.feroxcore.manager.statistics;

import be.marijn2341.feroxcore.Main;
import be.marijn2341.feroxcore.utils.Utils;
import org.bukkit.entity.Player;

public class GameStatistics {

    private final Main main = Main.getInstance();

    public void sendGameStats(Player player, String matchtijd) {
        player.sendMessage(Utils.color("&8-----"));
        player.sendMessage(Utils.color("&7Total blocks placed (global): &f" + main.getDataManager().getBlocks().get("placed")));
        player.sendMessage(Utils.color("&7Total blocks broken (global): &f" + main.getDataManager().getBlocks().get("broken")));
        player.sendMessage(Utils.color("&7Matchtime: &f" + matchtijd));
        player.sendMessage(Utils.color("&7Your kills: &f" + main.getDataManager().getKills().get(player.getUniqueId())));
        player.sendMessage(Utils.color("&7Your deaths: &f" + main.getDataManager().getDeaths().get(player.getUniqueId())));
        player.sendMessage(Utils.color("&7Arrows shot: &f" + main.getDataManager().getArrowsShot().get(player.getUniqueId())));
        player.sendMessage(Utils.color("&7Arrows hit: &f" + main.getDataManager().getArrowsHit().get(player.getUniqueId())));
        player.sendMessage(Utils.color("&8-----"));
    }
}

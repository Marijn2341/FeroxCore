package be.marijn2341.feroxcore.Manager;

import be.marijn2341.feroxcore.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.sql.Time;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PlayerManager {

    public static String GetOnlineTime(Player player) {

        int time = player.getStatistic(Statistic.PLAY_ONE_TICK);
        int seconds = time / 20;
        long hours = TimeUnit.SECONDS.toHours(seconds);
        long minutes = TimeUnit.SECONDS.toMinutes(seconds) - (hours * 60);
        String tijd = Utils.color(hours + "h, " + minutes + "min");
        return tijd;
    }

    public static long GetOnlineTimeMi(UUID uuid) {
        Player plr = Bukkit.getPlayer(uuid);
        int time = plr.getStatistic(Statistic.PLAY_ONE_TICK);
        int seconds = time / 20;
        long milliseconds = TimeUnit.SECONDS.toMillis(seconds);
        return milliseconds;
    }

    public static long GetWalkedBlocksPlayer(Player player) {
        return (player.getStatistic(Statistic.WALK_ONE_CM) / 100);
    }

    public static long GetSprintedBlocksPlayer(Player player) {
        return (player.getStatistic(Statistic.SPRINT_ONE_CM) / 100);
    }

    public static long GetJumpsPlayer(Player player) {
        return (player.getStatistic(Statistic.JUMP));
    }
}

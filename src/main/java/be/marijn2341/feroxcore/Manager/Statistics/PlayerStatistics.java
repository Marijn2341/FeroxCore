package be.marijn2341.feroxcore.Manager.Statistics;

import be.marijn2341.feroxcore.Database.Database;
import be.marijn2341.feroxcore.Listeners.ArrowShootListener;
import be.marijn2341.feroxcore.Listeners.DeathListener;

import java.util.UUID;

public class PlayerStatistics {

    public static void updateStats(UUID uuid) {
        int deaths = DeathListener.Deaths.get(uuid);
        Database.UpdateDeathsDB(uuid, deaths);

        int kills = DeathListener.Kills.get(uuid);
        Database.UpdateKillsDB(uuid, kills);

        int shot = ArrowShootListener.shot.get(uuid);
        Database.UpdateArrowsShotDB(uuid, shot);

        int hit = DeathListener.arrowshit.get(uuid);
        Database.UpdateArrowsHitDB(uuid,hit);
    }
}

package be.marijn2341.feroxcore.manager.statistics;

import be.marijn2341.feroxcore.database.Database;
import be.marijn2341.feroxcore.listeners.ArrowShootListener;
import be.marijn2341.feroxcore.listeners.DeathListener;

import java.util.UUID;

public class PlayerStatistics {

    public static void updateStats(UUID uuid) {
        int deaths = DeathListener.DEATHS.get(uuid);
        Database.updateDeathsDB(uuid, deaths);

        int kills = DeathListener.KILLS.get(uuid);
        Database.updateKillsDB(uuid, kills);

        int shot = ArrowShootListener.SHOT.get(uuid);
        Database.updateArrowsShotDB(uuid, shot);

        int hit = DeathListener.ARROWSHIT.get(uuid);
        Database.updateArrowsHitDB(uuid,hit);
    }
}

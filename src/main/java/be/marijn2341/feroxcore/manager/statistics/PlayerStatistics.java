package be.marijn2341.feroxcore.manager.statistics;

import be.marijn2341.feroxcore.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerStatistics {

    private final Main main = Main.getInstance();

    public void updateStats(UUID uuid) {
        int deaths = main.getDataManager().getDeaths().get(uuid);
        main.getDb().updateDeathsDB(uuid, deaths);

        int kills = main.getDataManager().getKills().get(uuid);
        main.getDb().updateKillsDB(uuid, kills);

        int shot = main.getDataManager().getArrowsShot().get(uuid);
        main.getDb().updateArrowsShotDB(uuid, shot);

        int hit = main.getDataManager().getArrowsHit().get(uuid);
        main.getDb().updateArrowsHitDB(uuid, hit);

        Player plr = Bukkit.getPlayer(uuid);
        Bukkit.getLogger().info("De statistics van " + plr.getName() + " zijn geupdated!");
    }
}

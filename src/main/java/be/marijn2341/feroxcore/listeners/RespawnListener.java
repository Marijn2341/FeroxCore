package be.marijn2341.feroxcore.listeners;

import be.marijn2341.feroxcore.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class RespawnListener implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        new BukkitRunnable() {
            @Override
            public void run() {
                e.getEntity().spigot().respawn();
            }
        }.runTaskLater(Main.getInstance(), 10);
    }

    @EventHandler
    public void respawn(PlayerRespawnEvent e) {
        Player player = e.getPlayer();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (main.getDataManager().getTeamRed().contains(player.getUniqueId())) {
                    main.getTeamManager().processToTeam("red", player);
                } else if (main.getDataManager().getTeamBlue().contains(player.getUniqueId())) {
                    main.getTeamManager().processToTeam("blue", player);
                } else {
                    main.getMapManager().teleportToSpawn(player);
                }
            }
        }.runTaskLaterAsynchronously(Main.getInstance(), 10);
    }
}

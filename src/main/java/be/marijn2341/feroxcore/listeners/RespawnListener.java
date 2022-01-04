package be.marijn2341.feroxcore.listeners;

import be.marijn2341.feroxcore.Main;
import be.marijn2341.feroxcore.manager.MapManager;
import be.marijn2341.feroxcore.manager.TeamManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class RespawnListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        new BukkitRunnable() {
            @Override public void run() {
                e.getEntity().spigot().respawn();
            }
        }.runTaskLater(Main.getInstance(), 10);
    }

    @EventHandler
    public void respawn(PlayerRespawnEvent e) {
        Player player = (Player) e.getPlayer();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (TeamManager.TEAMRED.contains(player.getUniqueId())) {
                    TeamManager.processToTeam("red", player);
                } else if (TeamManager.TEAMBLUE.contains(player.getUniqueId())) {
                    TeamManager.processToTeam("blue", player);
                } else {
                    MapManager.teleportToSpawn(player);
                }
            }
        }.runTaskLaterAsynchronously(Main.getInstance(), 10);
    }
}

package be.marijn2341.feroxcore.Listeners;

import be.marijn2341.feroxcore.Main;
import be.marijn2341.feroxcore.Manager.MapManager;
import be.marijn2341.feroxcore.Manager.TeamManager;
import org.bukkit.Color;
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
        }.runTaskLater(Main.getInstance(), 20);
    }

    @EventHandler
    public void respawn(PlayerRespawnEvent e) {
        Player player = (Player) e.getPlayer();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (TeamManager.TeamRed.contains(player.getUniqueId())) {
                    TeamManager.processToTeam("red", player);
                } else if (TeamManager.TeamBlue.contains(player.getUniqueId())) {
                    TeamManager.processToTeam("blue", player);
                } else {
                    MapManager.TeleportToSpawn(player);
                }
            }
        }.runTaskLaterAsynchronously(Main.getInstance(), 10);
    }
}

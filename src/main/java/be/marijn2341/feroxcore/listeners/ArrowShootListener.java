package be.marijn2341.feroxcore.listeners;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import java.util.HashMap;
import java.util.UUID;

public class ArrowShootListener implements Listener {

    public static HashMap<UUID, Integer> SHOT = new HashMap<>();

    @EventHandler
    public void ArrowsShot(ProjectileLaunchEvent e) {
        if (e.getEntity().getShooter() instanceof Player) {
            if (e.getEntity() instanceof Arrow) {
                Player player = ((Player) e.getEntity().getShooter()).getPlayer();
                SHOT.put(player.getUniqueId(), SHOT.get(player.getUniqueId()) + 1);
            }
        }
    }
}
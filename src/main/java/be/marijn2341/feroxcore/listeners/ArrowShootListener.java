package be.marijn2341.feroxcore.listeners;

import be.marijn2341.feroxcore.Main;
import be.marijn2341.feroxcore.manager.DataManager;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class ArrowShootListener implements Listener {

    private Main main = Main.getInstance();

    @EventHandler
    public void ArrowsShot(ProjectileLaunchEvent e) {
        if (e.getEntity().getShooter() instanceof Player) {
            if (e.getEntity() instanceof Arrow) {
                Player player = ((Player) e.getEntity().getShooter()).getPlayer();
                main.getDataManager().getArrowsShot().put(player.getUniqueId(), main.getDataManager().getArrowsShot().get(player.getUniqueId()) + 1);
            }
        }
    }
}
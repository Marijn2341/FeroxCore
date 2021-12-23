package be.marijn2341.feroxcore.Listeners;

import be.marijn2341.feroxcore.Manager.TeamManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

public class FriendlyFire implements Listener {
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && (e.getDamager() instanceof Player || e.getDamager() instanceof Projectile)) {

            Entity Damager = e.getDamager();

            if (e.getDamager() instanceof Projectile) {
                ProjectileSource shooter = ((Projectile) e.getDamager()).getShooter();
                if (!(shooter instanceof Player)) {
                    return;
                } else {
                    Damager = (Entity) shooter;
                }
            }


            if (!TeamManager.TeamRed.contains(Damager.getUniqueId()) && !TeamManager.TeamBlue.contains(Damager.getUniqueId())) {
                e.setCancelled(true);
            }

            if (TeamManager.TeamRed.contains(Damager.getUniqueId()) && TeamManager.TeamRed.contains(e.getEntity().getUniqueId())) {
                e.setCancelled(true);
            }
            if (TeamManager.TeamBlue.contains(Damager.getUniqueId()) && TeamManager.TeamBlue.contains(e.getEntity().getUniqueId())) {
                e.setCancelled(true);
            }

        }
    }
}

package be.marijn2341.feroxcore.listeners;

import be.marijn2341.feroxcore.Main;
import be.marijn2341.feroxcore.manager.DataManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

public class FriendlyFire implements Listener {

    private Main main = Main.getInstance();

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


            if (!main.getDataManager().getTeamRed().contains(Damager.getUniqueId()) && !main.getDataManager().getTeamBlue().contains(Damager.getUniqueId())) {
                e.setCancelled(true);
            }

            if (main.getDataManager().getTeamRed().contains(Damager.getUniqueId()) && main.getDataManager().getTeamRed().contains(e.getEntity().getUniqueId())) {
                e.setCancelled(true);
            }
            if (main.getDataManager().getTeamBlue().contains(Damager.getUniqueId()) && main.getDataManager().getTeamBlue().contains(e.getEntity().getUniqueId())) {
                e.setCancelled(true);
            }

        }
    }
}

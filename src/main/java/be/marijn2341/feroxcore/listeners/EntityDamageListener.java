package be.marijn2341.feroxcore.listeners;

import be.marijn2341.feroxcore.Main;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {
    @EventHandler
    public void onFall(EntityDamageEvent e) {
        if (e.getEntityType() != EntityType.PLAYER) return;
        if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
            if (e.getEntity().getWorld().getName().equals(Main.getInstance().getWorldsConfig().getString("lobby.world"))) {
                e.setCancelled(true);
            }
        }
    }
}

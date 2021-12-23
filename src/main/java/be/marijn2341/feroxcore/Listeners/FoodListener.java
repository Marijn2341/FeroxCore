package be.marijn2341.feroxcore.Listeners;

import be.marijn2341.feroxcore.Main;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onFoodLevelChange (FoodLevelChangeEvent e) {
        if (e.getEntityType() != EntityType.PLAYER) return;
        if (e.getEntity().getWorld().getName().equals(Main.getInstance().getWorldsConfig().getString("lobby.world"))) {
            e.setCancelled (true);
        }
    }
}

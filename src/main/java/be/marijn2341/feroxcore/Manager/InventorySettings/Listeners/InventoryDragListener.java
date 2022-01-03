package be.marijn2341.feroxcore.Manager.InventorySettings.Listeners;

import be.marijn2341.feroxcore.Utils.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;

public class InventoryDragListener implements Listener {

    @EventHandler
    public void onDrag(InventoryDragEvent e) {
        if (e.getInventory().getTitle().equalsIgnoreCase(Utils.color("&6Edit Inventory"))) {
            e.setCancelled(true);
        }
    }
}

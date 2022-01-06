package be.marijn2341.feroxcore.manager.inventorysettings.Listeners;

import be.marijn2341.feroxcore.Main;
import be.marijn2341.feroxcore.database.Database;
import be.marijn2341.feroxcore.manager.inventorysettings.ItemStackSerializer;
import be.marijn2341.feroxcore.utils.Utils;
import be.marijn2341.feroxcore.utils.Gui;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class InventoryClickListener implements Listener {

    private Main main = Main.getInstance();

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (e.getInventory().getTitle().equalsIgnoreCase(Utils.color("&6Settings"))) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null) return;
            if (e.getClickedInventory().getType() == InventoryType.PLAYER) return;
            if (e.getClickedInventory() == null) return;
            if (e.getClickedInventory().getItem(e.getSlot()) == null) return;

            if (e.getSlot() == 13) {
                main.getGui().editInventory(player);
            }
        }

        if (e.getInventory().getTitle().equalsIgnoreCase(Utils.color("&6Edit Inventory"))) {

            if (e.getClickedInventory().getItem(e.getSlot()) == null) return;
            if (e.getClickedInventory() == null) return;
            if (e.getClick().equals(ClickType.SHIFT_LEFT) || e.getClick().equals(ClickType.SHIFT_RIGHT) ||
                    e.getClick().equals(ClickType.NUMBER_KEY) || e.getClick().equals(ClickType.DROP) ||
                    e.getClick().equals(ClickType.CONTROL_DROP) || e.getClick().equals(ClickType.MIDDLE) ||
                    e.getClick().equals(ClickType.DOUBLE_CLICK)) {
                e.setCancelled(true);
                return;
            }

            if (e.getClickedInventory().getType() == InventoryType.PLAYER) {
                e.setCancelled(true);
                return;
            }

            if (e.getSlot() > 17 && !(e.getClickedInventory().getType() == InventoryType.PLAYER)) {
                e.setCancelled(true);
                if (e.getClickedInventory().getItem(e.getSlot()) == null) return;
                if (e.getCurrentItem() == null) return;
            }

            if (!(e.getCurrentItem().getItemMeta().hasDisplayName())) return;

            if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Utils.color("&c&lCancel"))) {
                if (!(player.getOpenInventory().getCursor().getType() == Material.AIR)) {
                    player.sendMessage(Utils.color("&cCan't save the inventory.."));
                    return;
                }
                player.closeInventory();
                player.sendMessage(Utils.color("&cCancelled editing..."));
                return;

            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Utils.color("&a&lSave"))) {
                if (!(player.getOpenInventory().getCursor().getType() == Material.AIR)) {
                    player.sendMessage(Utils.color("&cCan't save the inventory.."));
                    return;
                }
                ArrayList<ItemStack> itemstack = new ArrayList<ItemStack>();
                for (int i = 0; i < 18; i++) {
                    ItemStack item = player.getOpenInventory().getItem(i);
                    itemstack.add(item);
                }
                ItemStackSerializer serializer = Main.getInstance().getSerializer();
                String inv = serializer.serialize(itemstack.toArray(new ItemStack[0]));
                main.getDb().updateInventory(player, inv);
                player.closeInventory();
                player.sendMessage(Utils.color("&aSaved inventory.."));
                return;
            } else {
                return;
            }
        }
    }
}

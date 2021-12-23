package be.marijn2341.feroxcore.Listeners;

import be.marijn2341.feroxcore.Main;
import be.marijn2341.feroxcore.Manager.TeamManager;
import be.marijn2341.feroxcore.Utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (e.getInventory().getTitle().equalsIgnoreCase(Utils.color("&6Team Selector"))) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null) return;
            if (e.getClickedInventory().getType() == InventoryType.PLAYER) return;
            if (e.getClickedInventory() == null) return;
            if (e.getClickedInventory().getItem(e.getSlot()) == null) return;

            if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Utils.color("&9Blue Team"))) {
                if (!(TeamManager.CheckTeamsUnbalanced("blue"))) {
                    TeamManager.addToTeam("blue", player);
                    TeamManager.processToTeam("blue", player);
                    return;
                } else {
                    player.sendMessage(Utils.color("&cYou can't join this team, The teams will be unbalanced if you join this team."));
                    player.closeInventory();
                    return;
                }
            }

            if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Utils.color("&4Red Team"))) {
                if (!(TeamManager.CheckTeamsUnbalanced("red"))) {
                    TeamManager.addToTeam("red", player);
                    TeamManager.processToTeam("red", player);
                    return;
                } else {
                    player.sendMessage(Utils.color("&cYou can't join this team, The teams will be unbalanced if you join this team."));
                    player.closeInventory();
                    return;
                }
            }

            if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Utils.color("&8Random Team"))) {
                TeamManager.addToRandomTeam(player);
                return;
            }

            if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Utils.color("&8Spectate"))) {
                TeamManager.SetAsSpectator(player);
                return;
            }
        }

        if (e.getInventory().getTitle().equalsIgnoreCase(Utils.color("&6Statistics"))) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null) return;
            if (e.getClickedInventory().getType() == InventoryType.PLAYER) return;
            if (e.getClickedInventory() == null) return;
            if (e.getClickedInventory().getItem(e.getSlot()) == null) return;
        }

        if (player.getWorld().getName().equals(Main.getInstance().getWorldsConfig().getString("lobby.world"))) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null) return;
            if (e.getClickedInventory().getType() == InventoryType.PLAYER) return;
            if (e.getClickedInventory() == null) return;
            if (e.getClickedInventory().getItem(e.getSlot()) == null) return;
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        if (player.getWorld().getName().equals(Main.getInstance().getWorldsConfig().getString("lobby.world"))) {
            e.setCancelled(true);
        }
    }
}

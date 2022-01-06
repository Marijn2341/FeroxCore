package be.marijn2341.feroxcore.listeners;

import be.marijn2341.feroxcore.Main;
import be.marijn2341.feroxcore.manager.DataManager;
import be.marijn2341.feroxcore.manager.TeamManager;
import be.marijn2341.feroxcore.utils.Utils;
import be.marijn2341.feroxcore.utils.Gui;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;

public class InventoryClickListener implements Listener {

    private Main main = Main.getInstance();

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
                if (!(main.getTeamManager().checkTeamsUnbalanced("blue"))) {
                    main.getTeamManager().addToTeam("blue", player);
                    main.getTeamManager().processToTeam("blue", player);
                    return;
                } else {
                    player.sendMessage(Utils.color("&cYou can't join this team, The teams will be unbalanced if you join this team."));
                    player.closeInventory();
                    return;
                }
            }

            if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Utils.color("&4Red Team"))) {
                if (!(main.getTeamManager().checkTeamsUnbalanced("red"))) {
                    main.getTeamManager().addToTeam("red", player);
                    main.getTeamManager().processToTeam("red", player);
                    return;
                } else {
                    player.sendMessage(Utils.color("&cYou can't join this team, The teams will be unbalanced if you join this team."));
                    player.closeInventory();
                    return;
                }
            }

            if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Utils.color("&8Random Team"))) {
                main.getTeamManager().addToRandomTeam(player);
                return;
            }

            if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Utils.color("&8Spectate"))) {
                main.getTeamManager().SetAsSpectator(player);
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

        if (e.getInventory().getTitle().equalsIgnoreCase(Utils.color("&6" + player.getName()))) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null) return;
            if (e.getClickedInventory().getType() == InventoryType.PLAYER) return;
            if (e.getClickedInventory() == null) return;
            if (e.getClickedInventory().getItem(e.getSlot()) == null) return;
            if (e.getSlot() == 14) {
                main.getGui().createSettingsMenu(player);
                return;
            }
        }

        if (player.getWorld().getName().equals(main.getDataManager().getLobby().get("lobby").getWorld().getName())) {
            if (!(player.hasPermission("ferox.lobby.build"))) {
                if (!(e.getInventory().getTitle().equalsIgnoreCase(Utils.color("&6Edit Inventory")))) {
                    e.setCancelled(true);
                    if (e.getCurrentItem() == null) return;
                    if (e.getClickedInventory().getType() == InventoryType.PLAYER) return;
                    if (e.getClickedInventory() == null) return;
                    if (e.getClickedInventory().getItem(e.getSlot()) == null) return;
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        if (player.getWorld().getName().equals(main.getDataManager().getLobby().get("lobby").getWorld().getName())) {
            if (!(player.hasPermission("ferox.lobby.build"))) {
                e.setCancelled(true);
            }
        }
    }
}

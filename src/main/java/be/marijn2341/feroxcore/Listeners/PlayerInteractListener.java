package be.marijn2341.feroxcore.Listeners;

import be.marijn2341.feroxcore.Manager.MapManager;
import be.marijn2341.feroxcore.Manager.TeamManager;
import be.marijn2341.feroxcore.Utils.Utils;
import be.marijn2341.feroxcore.Utils.gui;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {
    @EventHandler
    public void onclick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_AIR && e.getItem().getType() == Material.COMPASS) {
            if (MapManager.GameActive) {
                if (!(TeamManager.AllreadyInTeam(e.getPlayer()))) {
                    gui.CreateTeamSelectorMenu(player);
                } else {
                    e.getPlayer().sendMessage(Utils.color("&cYou are allready in a team, Go to the lobby with /lobby and try it again."));
                    return;
                }
            } else {
                e.getPlayer().sendMessage(Utils.color("&cThere is no game active, Wait until a new game starts."));
                return;
            }
        }

        if (e.getAction() == Action.RIGHT_CLICK_AIR && e.getItem().getType() == Material.SKULL_ITEM) {
            if (player.getInventory().getItemInHand().getItemMeta().getDisplayName().equals(Utils.color("&6Statistics"))) {
                gui.CreateStaticsMenu(player);
            }
        }
    }
}

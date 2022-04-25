package be.marijn2341.feroxcore.listeners;

import be.marijn2341.feroxcore.Main;
import be.marijn2341.feroxcore.commands.staff.SetupCommand;
import be.marijn2341.feroxcore.manager.MapManager;
import be.marijn2341.feroxcore.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler
    public void onclick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_AIR && e.getItem().getType() == Material.COMPASS) {
            if (SetupCommand.SETUPMODE.containsKey(player.getUniqueId())) {
                player.sendMessage(Utils.color("&cYou are currently in setup mode, exit with /setup exit or /setup save."));
                return;
            }
            if (MapManager.GAMEACTIVE) {
                if (!(main.getTeamManager().AllreadyInTeam(e.getPlayer()))) {
                    main.getGui().createTeamSelectorMenu(player);
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
            if (player.getInventory().getItemInHand().getItemMeta().getDisplayName().equals(Utils.color("&6" + player.getName()))) {
                main.getGui().createPlayerMenu(player);
            }
        }
    }
}

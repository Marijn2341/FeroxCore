package be.marijn2341.feroxcore.Listeners;

import be.marijn2341.feroxcore.Commands.Staff.SetupCommand;
import be.marijn2341.feroxcore.Database.Database;
import be.marijn2341.feroxcore.Main;
import be.marijn2341.feroxcore.Manager.MapManager;
import be.marijn2341.feroxcore.Manager.PlayerManager;
import be.marijn2341.feroxcore.Manager.TeamManager;
import be.marijn2341.feroxcore.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {
    @EventHandler
    public void PlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        e.setQuitMessage(Utils.color("&8[&4&l-&8] &7" + player.getName()));
        if (TeamManager.TeamBlue.contains(player.getUniqueId())) {
            TeamManager.TeamBlue.remove(player.getUniqueId());
        } else if (TeamManager.TeamRed.contains(player.getUniqueId())) {
            TeamManager.TeamRed.remove(player.getUniqueId());
        } else if (TeamManager.Spectator.contains(player.getUniqueId())) {
            TeamManager.Spectator.remove(player.getUniqueId());
        }

        if (SetupCommand.setupMode.containsKey(player.getUniqueId())) {
            if (Main.getInstance().getWorldsConfig().contains("worlds." + SetupCommand.setupMode.get(player.getUniqueId()).getName())) {
                Main.getInstance().getWorldsConfig().set("worlds." + SetupCommand.setupMode.get(player.getUniqueId()).getName(), null);
                SetupCommand.setupMode.remove(player.getUniqueId());
                return;
            } else {
                SetupCommand.setupMode.remove(player.getUniqueId());
                return;
            }
        }

        Database.SetPlaytimeDB(player.getUniqueId());
    }
}

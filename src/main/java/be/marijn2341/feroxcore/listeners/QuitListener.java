package be.marijn2341.feroxcore.listeners;

import be.marijn2341.feroxcore.commands.staff.SetupCommand;
import be.marijn2341.feroxcore.database.Database;
import be.marijn2341.feroxcore.Main;
import be.marijn2341.feroxcore.manager.TeamManager;
import be.marijn2341.feroxcore.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {
    @EventHandler
    public void PlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        e.setQuitMessage(Utils.color("&8[&4&l-&8] &7" + player.getName()));
        if (TeamManager.TEAMBLUE.contains(player.getUniqueId())) {
            TeamManager.TEAMBLUE.remove(player.getUniqueId());
        } else if (TeamManager.TEAMRED.contains(player.getUniqueId())) {
            TeamManager.TEAMRED.remove(player.getUniqueId());
        } else if (TeamManager.SPECTATORS.contains(player.getUniqueId())) {
            TeamManager.SPECTATORS.remove(player.getUniqueId());
        }

        if (TeamManager.PLAYERS.contains(player.getUniqueId())) {
            TeamManager.PLAYERS.remove(player.getUniqueId());
        }

        if (SetupCommand.SETUPMODE.containsKey(player.getUniqueId())) {
            if (Main.getInstance().getWorldsConfig().contains("worlds." + SetupCommand.SETUPMODE.get(player.getUniqueId()).getName())) {
                Main.getInstance().getWorldsConfig().set("worlds." + SetupCommand.SETUPMODE.get(player.getUniqueId()).getName(), null);
                SetupCommand.SETUPMODE.remove(player.getUniqueId());
                return;
            } else {
                SetupCommand.SETUPMODE.remove(player.getUniqueId());
                return;
            }
        }

        Database.setPlaytimeDB(player.getUniqueId());
    }
}

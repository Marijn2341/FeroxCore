package be.marijn2341.feroxcore.Commands;

import be.marijn2341.feroxcore.Manager.MapManager;
import be.marijn2341.feroxcore.Manager.TeamManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LobbyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (TeamManager.AllreadyInTeam(player)) {
            if (TeamManager.TeamBlue.contains(player.getUniqueId())) {
                TeamManager.TeamBlue.remove(player.getUniqueId());
            } else if (TeamManager.TeamRed.contains(player.getUniqueId())) {
                TeamManager.TeamRed.remove(player.getUniqueId());
            } else if (TeamManager.Spectator.contains(player.getUniqueId())) {
                TeamManager.Spectator.remove(player.getUniqueId());
            }
        }
        MapManager.TeleportToSpawn(player);
        return false;
    }
}

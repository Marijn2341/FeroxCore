package be.marijn2341.feroxcore.Commands;

import be.marijn2341.feroxcore.Commands.Staff.SetupCommand;
import be.marijn2341.feroxcore.Manager.MapManager;
import be.marijn2341.feroxcore.Manager.TeamManager;
import be.marijn2341.feroxcore.Utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LobbyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        // CHECK IF PLAYER IS IN SETUP MODE
        if (SetupCommand.setupMode.containsKey(player.getUniqueId())) {
            player.sendMessage(Utils.color("&cYou are currently in setup mode, exit with /setup exit or /setup save."));
            return false;
        }

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
